package nl.rjcoding.pathfinding.sensus

import nl.rjcoding.common.Direction
import nl.rjcoding.common.Fraction
import nl.rjcoding.common.Orientation
import nl.rjcoding.common.Vector2D

sealed class Cell(open val position: Vector2D<Fraction>) {

    data class Empty(override val position: Vector2D<Fraction>): Cell(position)

    class Terminator<Item>(override val position: Vector2D<Fraction>, val item: Item): Hub(position)

    class Junction(override val position: Vector2D<Fraction>): Hub(position) {
        data class Connection(val from: Port, val to: Port)

        private val _connections = mutableSetOf<Connection>()

        val connections : Set<Connection> get() = _connections

        fun connect(from: Port, to: Port) {
            require(isOccupied(from))
            require(isOccupied(to))
            require(from != to)

            val connection = Connection(from, to)
            _connections.add(connection)
        }
    }

    abstract class Hub(override val position: Vector2D<Fraction>): Cell(position) {
        data class Port(val location: Direction, val index: Int)

        private val closedLocations = mutableSetOf<Direction>()
        private val portIndicesOccupied = mutableMapOf<Direction, MutableSet<Int>>()

        fun attach(port: Port) {
            require(isFree(port))
            portIndicesOccupied.getOrPut(port.location) { mutableSetOf() }.add(port.index)
        }

        fun isFree(port: Port): Boolean {
            return if (closedLocations.contains(port.location)) {
                false
            } else {
                !(portIndicesOccupied[port.location]?.contains(port.index) ?: false)
            }
        }

        fun isOccupied(port: Port): Boolean = !isFree(port)

        fun occupiedIndices(location: Direction): Set<Int> {
            return portIndicesOccupied[location] ?: setOf()
        }

        fun closeLocation(location: Direction) {
            closedLocations.add(location)
        }

        fun openLocation(location: Direction) {
            closedLocations.remove(location)
        }
    }

}