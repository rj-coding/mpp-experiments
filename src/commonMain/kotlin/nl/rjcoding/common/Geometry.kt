package nl.rjcoding.common

import kotlin.math.max
import kotlin.math.min

enum class Direction(val angle: Int) {
    North(0),
    East(90),
    South(180),
    West(270);

    fun opposite(): Direction = when (this) {
        North -> South
        East -> West
        South -> North
        West -> East
    }

    companion object {
        fun fromAngle(angle: Int): Direction? = when (normalizeAngle(angle)) {
            0 -> North
            90 -> East
            180 -> South
            270 -> West
            else -> null
        }

        private tailrec fun normalizeAngle(angle: Int): Int = when {
            angle < 0 -> normalizeAngle(angle + 360)
            angle >= 360 -> normalizeAngle(angle % 360)
            else -> angle
        }
    }
}

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

    interface HasArea {
        val area: Area
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

object Fractional {
    data class Vector2D(val x: Fraction, val y: Fraction) {
        operator fun plus(other: Vector2D) = Vector2D(x + other.x, y + other.y)
        operator fun minus(other: Vector2D) = Vector2D(x - other.x, y - other.y)

        companion object {
            val Origin = Vector2D(frac(0), frac(0))
        }
    }

    data class Area(val width: Fraction, val height: Fraction) {
        companion object {
            val Zero = Area(frac(0), frac(0))
            val Unit = Area(frac(1), frac(1))
        }
    }

    interface HasArea {
        val area: Area
    }

    data class Rectangle(val origin: Vector2D, val area: Area) {
        constructor(x: Fraction, y: Fraction, width: Fraction, height: Fraction) : this(Vector2D(x, y), Area(width, height))

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