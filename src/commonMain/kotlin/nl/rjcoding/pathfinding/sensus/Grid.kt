package nl.rjcoding.pathfinding.sensus

import nl.rjcoding.common.*

class Grid<Item>(val width: Fraction, val height: Fraction) {

    val vec2d = Fractional.geometry.vector2D

    private val occupiedCells: MutableMap<Vector2D<Fraction>, Cell<Item>> = mutableMapOf()

    fun isEmpty(position: Vector2D<Fraction>): Boolean {
        return occupiedCells[position] == null
    }

    fun getCell(position: Vector2D<Fraction>): Cell<Item>? {
        if (!withinBounds(position)) return null
        return occupiedCells[position]
    }

    fun addCell(position: Vector2D<Fraction>): Cell<Item>? {
        if (withinBounds(position)) {
            return Cell<Item>(position).also {
                occupiedCells[position] = it
            }
        }
        return null
    }

    fun clearCell(position: Vector2D<Fraction>) {
        if (withinBounds(position)) {
            occupiedCells.remove(position)
        }
    }

    fun freeOuterRowIndices(row: Fraction): Pair<Int, Int>{
        return freeOuterIndices(rowCells(row), Orientation.Horizontal)
    }

    fun freeOuterColumnIndices(row: Fraction): Pair<Int, Int> {
        return freeOuterIndices(columnCells(row), Orientation.Vertical)
    }

    private fun freeOuterIndices(cellSequence: Sequence<Cell<Item>>, orientation: Orientation): Pair<Int, Int> {
        val directions = orientation.directions()
        val occupiedIndices = cellSequence
            .filterIsInstance<Cell<Item>>()
            .fold(setOf<Int>()) { occupied, hub ->
                occupied.union(hub.occupiedIndices(directions.first).union(hub.occupiedIndices(directions.second)))
            }

        val min = occupiedIndices.minOrNull() ?: 0
        val max = occupiedIndices.maxOrNull() ?: 0

        return min to max
    }

    private fun withinBounds(position: Vector2D<Fraction>): Boolean {
        return position.x >= Fractional.geometry.vector2D.zero.x
                && position.x < width
                && position.y >= Fractional.geometry.vector2D.zero.y
                && position.y < height
    }

    private fun rowCells(row: Fraction): Sequence<Cell<Item>> {
        return cellSequence(
            vec2d(frac(0), row),
            vec2d(frac(1, 2), frac(0))
        )
    }

    private fun columnCells(column: Fraction): Sequence<Cell<Item>> {
        return cellSequence(
            vec2d(column, frac(0)),
            vec2d(frac(0), frac(1, 2))
        )
    }

    private fun cellSequence(start: Vector2D<Fraction>, offset: Vector2D<Fraction>): Sequence<Cell<Item>> = sequence {
        var cursor =  start
        while (cursor.x < width && cursor.y < height) {
            yield(cursor)
            cursor += offset
        }
    }.mapNotNull { getCell(it) }

}