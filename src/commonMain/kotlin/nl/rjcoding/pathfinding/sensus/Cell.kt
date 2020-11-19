package nl.rjcoding.pathfinding.sensus

import nl.rjcoding.common.Direction
import nl.rjcoding.common.Fraction
import nl.rjcoding.common.Vector2D

class Cell<Item>(open val position: Vector2D<Fraction>) {

    data class Port(val direction: Direction, val index: Int)
    data class Link(val from: Port, val to: Port)

    private val closedLocations = mutableSetOf<Direction>()
    private val portIndicesOccupied = mutableMapOf<Direction, MutableSet<Int>>()
    private val _links = mutableSetOf<Link>()

    var item: Item? = null

    fun hasItem(): Boolean = item != null

    val links : Set<Link> get() = _links

    fun attach(port: Port) {
        require(isFree(port))
        portIndicesOccupied.getOrPut(port.direction) { mutableSetOf() }.add(port.index)
    }

    fun isFree(port: Port): Boolean {
        return if (closedLocations.contains(port.direction)) {
            false
        } else {
            !(portIndicesOccupied[port.direction]?.contains(port.index) ?: false)
        }
    }

    fun isOccupied(port: Port): Boolean = !isFree(port)

    fun occupiedIndices(location: Direction): Set<Int> {
        return portIndicesOccupied[location] ?: setOf()
    }

    fun closeDirection(direction: Direction) {
        closedLocations.add(direction)
    }

    fun openDirection(direction: Direction) {
        closedLocations.remove(direction)
    }

    fun link(from: Port, to: Port) {
        require(isOccupied(from))
        require(isOccupied(to))
        require(from != to)

        val connection = Link(from, to)
        _links.add(connection)
    }

    fun directionTo(other: Cell<Item>): Direction? = when {
        position.x == other.position.x && position.y == other.position.y -> null
        position.x != other.position.x && position.y != other.position.y -> null
        position.x == other.position.x -> when {
            position.y < other.position.y -> Direction.South
            else -> Direction.North
        }
        else -> when {
            position.x < other.position.x -> Direction.East
            else -> Direction.West
        }
    }
}