package nl.rjcoding.common

import kotlin.math.max
import kotlin.math.min

object Integral {

    data class Vector2D(val x: Int, val y: Int) {
        operator fun plus(other: Vector2D) = Vector2D(x + other.x, y + other.y)
        operator fun minus(other: Vector2D) = Vector2D(x - other.x, y - other.y)

        companion object {
            val Origin = Vector2D(0, 0)
        }
    }

    data class Area(val width: Int, val height: Int) {
        companion object {
            val Zero = Area(0, 0)
            val Unit = Area(1, 1)
        }
    }

    data class Rectangle(val origin: Vector2D, val area: Area) {
        constructor(x: Int, y: Int, width: Int, height: Int) : this(Vector2D(x, y), Area(width, height))

        val x0 = origin.x
        val y0 = origin.y
        val x1 = origin.x + area.width
        val y1 = origin.y + area.height
        val width = area.width
        val height = area.height

        fun move(d: Vector2D) : Rectangle = copy(origin = origin + d)

        operator fun plus(other: Rectangle): Rectangle {
            val u0 = min(x0, other.x0)
            val v0 = min(y0, other.y0)
            val u1 = max(x1, other.x1)
            val v1 = max(y1, other.y1)

            val width = u1 - u0
            val height = v1 - v0
            return Rectangle(u0, v0, width, height)
        }
    }

}