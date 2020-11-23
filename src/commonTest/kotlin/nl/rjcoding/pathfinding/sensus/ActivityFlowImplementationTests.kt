package nl.rjcoding.pathfinding.sensus

import nl.rjcoding.common.Fractional
import nl.rjcoding.common.frac
import nl.rjcoding.common.geometry
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ActivityFlowImplementationTests {

    val vec = Fractional.geometry.vector2D

    @Test
    fun neighbourSingleOptionTest() {
        val grid = Grid<String>(frac(1), frac(1, 2))
        val impl = ActivityFlowImplementation(grid)

        impl.neighbours(Start(Cell(vec(frac(0), frac(0))))).apply {
            assertEquals(1, size)
            (get(0) as Connection<String>).also { connection ->
                assertEquals(vec(frac(0), frac(0)), connection.from.position)
                assertEquals(vec(frac(1, 2), frac(0)), connection.to.position)
                assertEquals(0, connection.index)
            }
        }
    }

    @Test
    fun neighbourDoubleOptionTest() {
        val grid = Grid<String>(frac(3, 2), frac(1, 2))
        val impl = ActivityFlowImplementation(grid)

        impl.neighbours(Start(Cell(vec(frac(1, 2), frac(0))))).apply {
            val connections = filterIsInstance<Connection<String>>()
            assertEquals(2, connections.size)
            assertTrue { connections.all { it.from.position == vec(frac(1, 2), frac(0)) } }
            assertTrue { connections.any { it.to.position == vec(frac(1), frac(0)) } }
            assertTrue { connections.any { it.to.position == vec(frac(0), frac(0)) } }
        }
    }

}