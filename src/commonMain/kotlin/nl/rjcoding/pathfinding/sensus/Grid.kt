package nl.rjcoding.pathfinding.sensus

import nl.rjcoding.common.Fraction
import nl.rjcoding.common.Fractional
import nl.rjcoding.common.Vector2D
import nl.rjcoding.common.geometry

class Grid<Item>(val width: Fraction, val height: Fraction) {

    private val occupiedCells: MutableMap<Vector2D<Fraction>, Cell> = mutableMapOf()

    fun getCell(position: Vector2D<Fraction>): Cell? {
        if (!withinBounds(position)) return null

        return occupiedCells[position] ?: Cell.Empty(position)
    }

    fun addTerminator(position: Vector2D<Fraction>, item: Item) {
        if (withinBounds(position)) {
            occupiedCells[position] = Cell.Terminator(position, item)
        }
    }

    fun clearCell(position: Vector2D<Fraction>) {
        if (withinBounds(position)) {
            occupiedCells.remove(position)
        }
    }

    private fun withinBounds(position: Vector2D<Fraction>): Boolean {
        return position.x >= Fractional.geometry.vector2D.zero.x
                && position.x < width
                && position.y >= Fractional.geometry.vector2D.zero.y
                && position.y < height
    }

}