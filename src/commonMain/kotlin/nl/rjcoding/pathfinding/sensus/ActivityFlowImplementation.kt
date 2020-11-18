package nl.rjcoding.pathfinding.sensus

import nl.rjcoding.common.*
import nl.rjcoding.pathfinding.AStarPathFinder

class ActivityFlowImplementation(val grid: Grid<String>) : AStarPathFinder.Implementation<Step, Double> {

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

    override fun cost(from: Step, to: Step): Double {
        val fromPosition = stepToPosition(from)
        val toPosition = stepToPosition(to)
        val dx = (toPosition.x - fromPosition.x)
        val dy = (toPosition.y - fromPosition.y)
        return (dx * dx + dy * dy).toDouble()
    }

    override fun heuristic(from: Step, to: Step): Double {
        return cost(from, to)
    }

    override fun compare(a: Double, b: Double): Int {
        return a.compareTo(b)
    }

    override fun neighbours(node: Step): List<Step> {
        return when (node) {
            is Start<*> -> {
                val junctions = neighbourCells(node.terminator)

                listOf()
            }
            is Connection -> listOf()

            is End<*> -> listOf()

            else -> listOf() // this should not be reached
        }
    }

    private fun stepToPosition(step: Step): Vector2D<Fraction> {
        return when (step) {
            is Terminator<*> -> step.terminator.position
            is Connection -> step.to.position
        }
    }

    private fun getCellOrCreateJunction(position: Vector2D<Fraction>): Cell? {
        return if (grid.isEmpty(position)) {
            grid.addJunction(position)
        } else grid.getCell(position)
    }

    private fun neighbourCells(cell: Cell): Map<Direction, Cell> {
        return neighbourOffsets.mapNotNull { ( direction, offset) ->
            val position = cell.position + offset
            getCellOrCreateJunction(position)?.let {
                direction to it
            }
        }.toMap()
    }
}