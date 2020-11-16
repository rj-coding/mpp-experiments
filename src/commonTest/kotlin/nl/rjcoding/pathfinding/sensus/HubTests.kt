package nl.rjcoding.pathfinding.sensus

import nl.rjcoding.common.Direction
import nl.rjcoding.common.Fractional
import nl.rjcoding.common.frac
import nl.rjcoding.common.geometry
import kotlin.test.*

class HubTests {

    val vector2D = Fractional.geometry.vector2D

    @Test
    fun attachTest() {
        val hub = Cell.Junction(vector2D.zero)

        val port = Cell.Hub.Port(Direction.North, 0)
        assertTrue { hub.isFree(port) }
        hub.attach(port)
        assertFalse { hub.isFree(port) }

        assertFailsWith<IllegalArgumentException> {
            hub.attach(port)
        }
    }

    @Test
    fun connectTest() {
        val hub = Cell.Junction(vector2D.zero)
        val portA = Cell.Hub.Port(Direction.North, 0)
        val portB = Cell.Hub.Port(Direction.South, 0)

        assertFailsWith<IllegalArgumentException> {
            hub.connect(portA, portB)
        }
        hub.attach(portA)

        assertFailsWith<IllegalArgumentException> {
            hub.connect(portA, portB)
        }

        hub.attach(portB)
        hub.connect(portA, portB)
        assertEquals(1, hub.connections.size)
    }

}