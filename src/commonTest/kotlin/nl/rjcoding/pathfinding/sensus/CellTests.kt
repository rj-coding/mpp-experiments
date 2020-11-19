package nl.rjcoding.pathfinding.sensus

import nl.rjcoding.common.Direction
import nl.rjcoding.common.Fractional
import nl.rjcoding.common.frac
import nl.rjcoding.common.geometry
import kotlin.test.*

class CellTests {

    val vector2D = Fractional.geometry.vector2D

    @Test
    fun attachTest() {
        val hub = Cell<String>(vector2D.zero)

        val port = Cell.Port(Direction.North, 0)
        assertTrue { hub.isFree(port) }
        hub.attach(port)
        assertFalse { hub.isFree(port) }

        assertFailsWith<IllegalArgumentException> {
            hub.attach(port)
        }
    }

    @Test
    fun linkTest() {
        val hub = Cell<String>(vector2D.zero)
        val portA = Cell.Port(Direction.North, 0)
        val portB = Cell.Port(Direction.South, 0)

        assertFailsWith<IllegalArgumentException> {
            hub.link(portA, portB)
        }
        hub.attach(portA)

        assertFailsWith<IllegalArgumentException> {
            hub.link(portA, portB)
        }

        hub.attach(portB)
        hub.link(portA, portB)
        assertEquals(1, hub.links.size)
    }

}