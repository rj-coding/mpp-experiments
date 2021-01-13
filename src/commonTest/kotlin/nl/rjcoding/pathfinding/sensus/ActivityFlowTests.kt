package nl.rjcoding.pathfinding.sensus

import nl.rjcoding.common.Fractional
import nl.rjcoding.common.frac
import nl.rjcoding.common.geometry
import nl.rjcoding.pathfinding.AStarPathFinder
import kotlin.test.Test
import kotlin.test.assertEquals

class ActivityFlowTests {

    val vec = Fractional.geometry.vector2D

    @Test
    fun horizontalSimple() {
        val grid = Grid<String>(frac(3), frac(1))
        val impl = ActivityFlowImplementation(grid)

        val start = Start(grid.addCell(vec(frac(0), frac(0)), "Start")!!)
        val end = End(grid.addCell(vec(frac(2), frac(0)), "End")!!)

        val pathfinder = AStarPathFinder(impl)
        val result = pathfinder.find(start, end)
        assertEquals(6, result.size)
        assertEquals(vec(frac(0), frac(0)), (result[0] as Start).terminator.position)
        assertEquals(vec(frac(2), frac(0)), (result[5] as End).terminator.position)
    }

    @Test
    fun verticalSimple() {
        val grid = Grid<String>(frac(1), frac(3))
        val impl = ActivityFlowImplementation(grid)

        val start = Start(grid.addCell(vec(frac(0), frac(0)), "Start")!!)
        val end = End(grid.addCell(vec(frac(0), frac(2)), "End")!!)

        val pathfinder = AStarPathFinder(impl)
        val result = pathfinder.find(start, end)
        assertEquals(6, result.size)
        assertEquals(vec(frac(0), frac(0)), (result[0] as Start).terminator.position)
        assertEquals(vec(frac(0), frac(2)), (result[5] as End).terminator.position)
    }

}