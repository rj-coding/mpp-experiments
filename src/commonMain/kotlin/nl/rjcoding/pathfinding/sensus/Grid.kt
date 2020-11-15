package nl.rjcoding.pathfinding.sensus

import nl.rjcoding.common.Fraction
import nl.rjcoding.common.Fractional.Vector2D

class Grid<Item>(val width: Fraction, val height: Fraction) {

    private val occupiedCells: MutableMap<Vector2D, Cell> = mutableMapOf()

    fun getCell(position: Vector2D): Cell? {
        if (!withinBounds(position)) return null

        return occupiedCells[position] ?: Cell.Empty(position)
    }

    fun addTerminator(position: Vector2D, item: Item) {
        if (withinBounds(position)) {
            occupiedCells[position] = Cell.Terminator(position, item)
        }
    }

    fun clearCell(position: Vector2D) {
        if (withinBounds(position)) {
            occupiedCells.remove(position)
        }
    }

    private fun withinBounds(position: Vector2D): Boolean {
        return position.x >= Vector2D.Origin.x
                && position.x < width
                && position.y >= Vector2D.Origin.y
                && position.y < height
    }

}