package nl.rjcoding.pathfinding.sensus

import nl.rjcoding.pathfinding.AStarPathFinder

class ActivityFlowImplementation(val grid: Grid<String>) : AStarPathFinder.Implementation<Step, Double> {
    override fun add(l: Double, r: Double): Double {
        return l + r
    }

    override fun cost(from: Step, to: Step): Double {
        val dx = (to.position.x - from.position.x)
        val dy = (to.position.y - from.position.y)
        return (dx * dx + dy * dy).toDouble()
    }

    override fun heuristic(from: Step, to: Step): Double {
        return cost(from, to)
    }

    override fun isSame(l: Step, r: Step): Boolean {
        return l.position == r.position
    }

    override fun compare(a: Double, b: Double): Int {
        return a.compareTo(b)
    }

    override fun neighbours(node: Step): List<Step> {
        TODO("Not yet implemented")
    }
}