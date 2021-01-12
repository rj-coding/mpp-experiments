package nl.rjcoding.pathfinding.sensus

import nl.rjcoding.common.*
import nl.rjcoding.pathfinding.AStarPathFinder

class ActivityFlowImplementation<Item>(val grid: Grid<Item>) : AStarPathFinder.Implementation<Step<Item>, Double> {

    val vec2d = Fractional.geometry.vector2D

    private val neighbourOffsets = listOf(
        Direction.East to vec2d(frac(1,2 ), frac(0)),
        Direction.South to vec2d(frac(0), frac(1,2)),
        Direction.West to vec2d(frac(-1,2), frac(0)),
        Direction.North to vec2d(frac(0), frac(-1,2)),
    ).toMap()

    override fun add(l: Double, r: Double): Double {
        return l + r
    }

    override fun cost(from: Step<Item>, to: Step<Item>): Double {
        val fromPosition = stepToPosition(from)
        val toPosition = stepToPosition(to)
        val dx = (toPosition.x - fromPosition.x)
        val dy = (toPosition.y - fromPosition.y)
        return (dx * dx + dy * dy).toDouble()
    }

    override fun heuristic(from: Step<Item>, to: Step<Item>): Double {
        return cost(from, to)
    }

    override fun compare(a: Double, b: Double): Int {
        return b.compareTo(a)
    }

    @Suppress("UNCHECKED_CAST")
    override fun neighbours(node: Step<Item>): List<Step<Item>> {
        val cell = stepToCell(node)
        val freeRowIndexLimits = grid.freeOuterRowIndices(stepToPosition(node).x)
        val freeColIndexLimits = grid.freeOuterColumnIndices(stepToPosition(node).y)
        val neighbours = neighbourCells(cell).let {
            if (node is Connection) it.filterNot { neighbour -> neighbour == node.from } else it
        }

        return when (node) {
            is Start, is Connection<*> -> {
                neighbours.flatMap { (direction, neighbour) ->
                    val indexLimits = when (direction.orientation()) {
                        Orientation.Horizontal -> freeRowIndexLimits
                        Orientation.Vertical -> freeColIndexLimits
                    }

                    getFreeConnections(
                        direction,
                        cell,
                        neighbour,
                        indexLimits,
                        node
                    )
                }
            }

            else -> listOf()
        }
    }

    private fun stepToCell(step: Step<Item>): Cell<Item> {
        return when (step) {
            is Terminator -> step.terminator
            is Connection -> step.to
        }
    }

    private fun stepToPosition(step: Step<Item>): Vector2D<Fraction> {
        return stepToCell(step).position
    }

    private fun getOrCreateCell(position: Vector2D<Fraction>): Cell<Item>? {
        return if (grid.isEmpty(position)) {
            grid.addCell(position)
        } else grid.getCell(position)
    }

    private fun neighbourCells(cell: Cell<Item>): Map<Direction, Cell<Item>> {
        return neighbourOffsets.mapNotNull { ( direction, offset) ->
            val position = cell.position + offset
            getOrCreateCell(position)?.let {
                direction to it
            }
        }.toMap()
    }

    private fun getFreeConnections(
        direction: Direction,
        from: Cell<Item>,
        to: Cell<Item>,
        indexLimits: Pair<Int, Int>,
        previous: Step<Item>
    ): List<Step<Item>> {
        val (indexFrom, indexTo) = indexLimits
        return when (indexFrom) {
            indexTo -> {
                if (from.isFree(Cell.Port(direction, indexFrom)) && to.isFree(Cell.Port(direction.opposite(), indexFrom))) {
                    val step = if (to.hasItem()) End(to, previous) else Connection(from, to, indexFrom, previous)
                    listOf(step)
                } else {
                    listOf()
                }
            }
            else -> (indexFrom .. indexTo).flatMap { index ->
                getFreeConnections(direction, from, to, index to index, previous)
            }
        }
    }
}