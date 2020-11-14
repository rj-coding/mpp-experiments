package nl.rjcoding.pathfinding

import nl.rjcoding.common.Integral
import kotlin.test.Test
import kotlin.test.assertEquals

data class Node(val position: Integral.Vector2D, override val previous: Node? = null): HasPrevious<Node>, AssertsTarget<Node> {
    val x = position.x
    val y = position.y

    constructor(x: Int, y: Int): this(Integral.Vector2D(x, y))

    operator fun plus(distance: Integral.Vector2D): Node {
        return Node(
            position + distance,
            this
        )
    }

    override fun reachedTarget(target: Node): Boolean {
        return position == target.position
    }

    override fun equals(other: Any?): Boolean {
        return (other as? Node)?.let { otherNode ->
            position.equals(otherNode.position)
        } ?: false
    }

    override fun hashCode(): Int {
        return position.hashCode()
    }
}

class Grid(val width: Int, val height: Int) : AStarPathFinder.Implementation<Node, Int> {

    private val blocked = mutableSetOf<Integral.Vector2D>()

    fun block(pos: Integral.Vector2D) {
        blocked.add(pos)
    }

    override fun add(l: Int, r: Int): Int {
        return l + r
    }

    override fun cost(from: Node, to: Node): Int {
        val dx = (to.x - from.x)
        val dy = (to.y - from.y)
        return dx * dx + dy * dy
    }

    override fun heuristic(from: Node, to: Node): Int {
        return cost(from, to)
    }

    override fun neighbours(node: Node): List<Node> {
        return listOf(
            node + Integral.Vector2D(1, 0),
            node + Integral.Vector2D(0, 1),
            node + Integral.Vector2D(-1, 0),
            node + Integral.Vector2D(0, -1)
        ).filter {
            it.x in 0 until width && it.y in 0 until height && !blocked.contains(it.position)
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
        val path = pathFinder.find(Node(0, 0), Node(1, 0))
        assertEquals(2, path.size)
        assertEquals(Integral.Vector2D(1,0), path.last().position)
    }

    @Test
    fun emptyBlockedTest() {
        val grid = Grid(3, 1)
        grid.block(Integral.Vector2D(1, 0))
        val pathFinder = AStarPathFinder(grid)
        val path = pathFinder.find(Node(0, 0), Node(2, 0))
        assertEquals(0, path.size)
    }

    @Test
    fun blockedTest() {
        val grid = Grid(3, 2)
        grid.block(Integral.Vector2D(1, 0))
        val pathFinder = AStarPathFinder(grid)
        val path = pathFinder.find(Node(0, 0), Node(2, 0))
        assertEquals(5, path.size)
        assertEquals(Integral.Vector2D(2,0), path.last().position)
    }

    @Test
    fun largeWallTest() {
        val grid = Grid(100, 3)
        (0 until 99).forEach { grid.block(Integral.Vector2D(it, 1)) }
        val pathFinder = AStarPathFinder(grid)
        val path = pathFinder.find(Node(0, 0), Node(0, 2))
        assertEquals(201, path.size)
        assertEquals(Integral.Vector2D(0,2), path.last().position)
    }

    @Test
    fun largeWallAndGridTest() {
        val grid = Grid(100, 101)
        (0 until 99).forEach { grid.block(Integral.Vector2D(it, 50)) }
        val pathFinder = AStarPathFinder(grid)
        val path = pathFinder.find(Node(0, 0), Node(0, 100))
        assertEquals(299, path.size)
        assertEquals(Integral.Vector2D(0,100), path.last().position)
    }
}