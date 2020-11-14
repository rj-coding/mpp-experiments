package nl.rjcoding.pathfinding.sensus

import nl.rjcoding.common.Direction
import nl.rjcoding.common.Fractional.Vector2D
import kotlin.math.max
import kotlin.math.min

sealed class Cell(open val position: Vector2D) {

    data class Empty(override val position: Vector2D): Cell(position)

    data class Occupied<Item>(override val position: Vector2D, val item: Item): Cell(position)

    class Hub<Path>(override val position: Vector2D): Cell(position) {
        data class Port(val location: Direction, val index: Int)
        data class PlacedPath<Path>(val path: Path, val entry: Port, val exit: Port)

        private val portIndicesOccupied = mutableMapOf<Direction, MutableSet<Int>>()
        private val placedPaths = mutableListOf<PlacedPath<Path>>()

        fun placeStraightPath(path: Path, location: Direction, index: Int) {
            val entry = Port(location, index)
            val exit = Port(location.opposite(), index)
            placeAngledPath(path, entry, exit)
        }

        fun placeAngledPath(path: Path, entry: Port, exit: Port) {
            require(isFreePort(entry))
            require(isFreePort(exit))

            placedPaths.add(PlacedPath(path, entry,exit))
            portIndicesOccupied.getOrPut(entry.location) { mutableSetOf() }.add(entry.index)
            portIndicesOccupied.getOrPut(exit.location) { mutableSetOf() }.add(exit.index)
        }

        fun isFreePort(port: Port): Boolean {
            return portIndicesOccupied[port.location]?.contains(port.index) == false
        }

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

        fun freeIndicesAtLocation(location: Direction): List<Int> {
            return freePortsAtLocation(location).map { it.index }
        }

        fun freeStraightPathPorts(location: Direction): List<Pair<Port, Port>> {
            val locationIndices = freeIndicesAtLocation(location)
            val oppositeIndices = freeIndicesAtLocation(location.opposite())
            return locationIndices.intersect(oppositeIndices).toList().map { index ->
                Port(location, index) to Port(location.opposite(), index)
            }
        }
    }

}