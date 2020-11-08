package nl.rjcoding.pathfinding

import nl.rjcoding.common.Integral
import kotlin.test.Test
import kotlin.test.assertEquals

class Grid(val width: Int, val height: Int) : AStarPathFinder.Implementation<Integral.Vector2D, Int> {

    private val blocked = mutableSetOf<Integral.Vector2D>()

    fun block(pos: Integral.Vector2D) {
        blocked.add(pos)
    }

    override fun add(l: Int, r: Int): Int {
        return l + r
    }

    override fun cost(from: Integral.Vector2D, to: Integral.Vector2D): Int {
        val dx = (to.x - from.x)
        val dy = (to.y - from.y)
        return dx * dx + dy * dy
    }

    override fun heuristic(from: Integral.Vector2D, to: Integral.Vector2D): Int {
        return cost(from, to)
    }

    override fun neighbours(node: Integral.Vector2D): List<Integral.Vector2D> {
        return listOf(
            node + Integral.Vector2D(1, 0),
            node + Integral.Vector2D(0, 1),
            node + Integral.Vector2D(-1, 0),
            node + Integral.Vector2D(0, -1)
        ).filter {
            it.x in 0 until width && it.y in 0 until height && !blocked.contains(it)
        }
    }

    override fun compare(a: Int, b: Int): Int {
        return a - b
    }
}

class AStarPathFinderTest {

    @Test
    fun oneStepTest() {
        val grid = Grid(2, 1)
        val pathFinder = AStarPathFinder(grid)
        val path = pathFinder.find(Integral.Vector2D(0, 0), Integral.Vector2D(1, 0))
        assertEquals(2, path.size)
        assertEquals(Integral.Vector2D(1,0), path.last())
    }

    @Test
    fun emptyBlockedTest() {
        val grid = Grid(3, 1)
        grid.block(Integral.Vector2D(1, 0))
        val pathFinder = AStarPathFinder(grid)
        val path = pathFinder.find(Integral.Vector2D(0, 0), Integral.Vector2D(2, 0))
        assertEquals(0, path.size)
    }

    @Test
    fun blockedTest() {
        val grid = Grid(3, 2)
        grid.block(Integral.Vector2D(1, 0))
        val pathFinder = AStarPathFinder(grid)
        val path = pathFinder.find(Integral.Vector2D(0, 0), Integral.Vector2D(2, 0))
        assertEquals(5, path.size)
        assertEquals(Integral.Vector2D(2,0), path.last())
    }

    @Test
    fun largeWallTest() {
        val grid = Grid(100, 3)
        (0 until 99).forEach { grid.block(Integral.Vector2D(it, 1)) }
        val pathFinder = AStarPathFinder(grid)
        val path = pathFinder.find(Integral.Vector2D(0, 0), Integral.Vector2D(0, 2))
        assertEquals(201, path.size)
        assertEquals(Integral.Vector2D(0,2), path.last())
    }

    @Test
    fun largeWallAndGridTest() {
        val grid = Grid(100, 101)
        (0 until 99).forEach { grid.block(Integral.Vector2D(it, 50)) }
        val pathFinder = AStarPathFinder(grid)
        val path = pathFinder.find(Integral.Vector2D(0, 0), Integral.Vector2D(0, 100))
        assertEquals(299, path.size)
        assertEquals(Integral.Vector2D(0,100), path.last())
    }
}