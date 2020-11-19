package nl.rjcoding.pathfinding.sensus

import nl.rjcoding.common.Direction
import nl.rjcoding.common.Fractional
import nl.rjcoding.common.frac
import nl.rjcoding.common.geometry
import kotlin.test.Test
import kotlin.test.assertEquals

class GridTests {

    val vec = Fractional.geometry.vector2D

    @Test
    fun freeOuterRowIndicesTest() {
        val grid = Grid<String>(frac(3), frac(2))

        val cellA = grid.addCell(vec(frac(0), frac(0)))!!
        val cellB = grid.addCell(vec(frac(1), frac(0)))!!
        val cellC = grid.addCell(vec(frac(2), frac(0)))!!

        grid.freeOuterRowIndices(frac(0)).also { (min, max) ->
            assertEquals(0, min)
            assertEquals(0, max)
        }

        cellA.attach(Cell.Port(Direction.East, 0))
        grid.freeOuterRowIndices(frac(0)).also { (min, max) ->
            assertEquals(-1, min)
            assertEquals(1, max)
        }

        cellC.attach(Cell.Port(Direction.West, 3))
        grid.freeOuterRowIndices(frac(0)).also { (min, max) ->
            assertEquals(-1, min)
            assertEquals(4, max)
        }

        cellB.attach(Cell.Port(Direction.East, -5))
        grid.freeOuterRowIndices(frac(0)).also { (min, max) ->
            assertEquals(-6, min)
            assertEquals(4, max)
        }

        grid.freeOuterRowIndices(frac(1)).also { (min, max) ->
            assertEquals(0, min)
            assertEquals(0, max)
        }
    }

}