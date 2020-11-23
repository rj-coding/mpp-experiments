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
        return a.compareTo(b)
    }

    @Suppress("UNCHECKED_CAST")
    override fun neighbours(node: Step<Item>): List<Step<Item>> {
        return when (node) {
            is Start -> {
                val freeRowIndexLimits = grid.freeOuterRowIndices(node.terminator.position.x)
                val freeColIndexLimits = grid.freeOuterColumnIndices(node.terminator.position.y)
                val neighbours = neighbourCells(node.terminator)
                neighbours.flatMap { (direction, neighbour) ->
                    val indexLimits = when (direction.orientation()) {
                        Orientation.Horizontal -> freeRowIndexLimits
                        Orientation.Vertical -> freeColIndexLimits
                    }

                    getFreeConnections(
                        direction,
                        node.terminator,
                        neighbour,
                        indexLimits,
                        node
                    )
                }
            }

            is Connection<*> -> listOf()

            is End<*> -> listOf()

            else -> listOf() // this should not be reached
        }
    }

    private fun stepToPosition(step: Step<Item>): Vector2D<Fraction> {
        return when (step) {
            is Terminator -> step.terminator.position
            is Connection -> step.to.position
        }
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
    ): List<Connection<Item>> {
        val (indexFrom, indexTo) = indexLimits
        return when (indexFrom) {
            indexTo -> {
                if (from.isFree(Cell.Port(direction, indexFrom)) && to.isFree(Cell.Port(direction.opposite(), indexFrom))) {
                    listOf(Connection(from, to, indexFrom, previous))
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