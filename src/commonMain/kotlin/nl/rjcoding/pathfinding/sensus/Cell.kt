package nl.rjcoding.pathfinding.sensus

import nl.rjcoding.common.Direction
import nl.rjcoding.common.Fractional.Vector2D
import kotlin.math.max
import kotlin.math.min

sealed class Cell(open val position: Vector2D) {

    data class Empty(override val position: Vector2D): Cell(position)

    class Terminator<Item>(override val position: Vector2D, val item: Item): Hub(position)

    class Junction(override val position: Vector2D): Hub(position) {
        data class Connection(val from: Port, val to: Port)

        private val _connections = mutableSetOf<Connection>()

        val connections : Set<Connection> get() = _connections

        fun connect(from: Port, to: Port): Connection {
            require(isOccoupied(from))
            require(isOccoupied(to))

            return Connection(from, to).also {
                _connections.add(it)
            }
        }
    }

    abstract class Hub(override val position: Vector2D): Cell(position) {
        data class Port(val location: Direction, val index: Int)

        private val portIndicesOccupied = mutableMapOf<Direction, MutableSet<Int>>()

        fun attach(port: Port) {
            require(isFree(port))
            portIndicesOccupied.getOrPut(port.location) { mutableSetOf() }.add(port.index)
        }

        fun isFree(port: Port): Boolean {
            return portIndicesOccupied[port.location]?.contains(port.index) == false
        }

        fun isOccoupied(port: Port): Boolean = !isFree(port)

        fun freePortsAtLocation(location: Direction): List<Port> {
            val occupied = (portIndicesOccupied[location] ?: mutableSetOf())
            if (occupied.isEmpty()) return listOf(Port(location, 0))

            val (min, max) = occupied.fold(Pair(Int.MAX_VALUE, Int.MIN_VALUE)) { (currentMin, currentMax), index ->
                min(currentMin, index) to max(currentMax, index)
            }
            return (min - 1 to max + 1).toList()
                .filter { !occupied.contains(it) }
                .map { Port(location, it) }
        }

        fun freeStraightPathPorts(location: Direction): List<Pair<Port, Port>> {
            val locationIndices = freeIndicesAtLocation(location)
            val oppositeIndices = freeIndicesAtLocation(location.opposite())
            return locationIndices.intersect(oppositeIndices).toList().map { index ->
                Port(location, index) to Port(location.opposite(), index)
            }
        }

        private fun freeIndicesAtLocation(location: Direction): List<Int> {
            return freePortsAtLocation(location).map { it.index }
        }
    }

}