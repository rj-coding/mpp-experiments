package nl.rjcoding.common

import kotlin.math.max
import kotlin.math.min

interface HasArea<T> {
    val area: Area<T>
}

val <T> NumericalOps<T>.geometry: GeometryProvider<T>
    get() = GeometryProvider(this)

class GeometryProvider<T>(private val ops: NumericalOps<T>) {
    val vector2D = Vector2DProvider(ops)
    val area = AreaProvider(ops)
}

class Vector2DProvider<T>(private val ops: NumericalOps<T>) {
    val zero = Vector2D(ops.zero, ops.zero, ops)
    val unit = Vector2D(ops.unit, ops.unit, ops)

    operator fun invoke(x: T, y: T): Vector2D<T> {
        return Vector2D(x, y, ops)
    }
}

class AreaProvider<T>(private val ops: NumericalOps<T>) {
    val empty = Area(ops.zero, ops.zero)
    val unit = Area(ops.unit, ops.unit)

    operator fun invoke(width: T, height: T) = Area(width, height)
}

class RectangleProvider<T>(private val ops: NumericalOps<T>) {
    operator fun invoke(origin: Vector2D<T>, area: Area<T>) = Rectangle(origin, area, ops)
    operator fun invoke(x: T, y: T, width: T, height: T) = Rectangle(x, y, width, height, ops)
}

data class Vector2D<T>(val x: T, val y: T, private val ops: NumericalOps<T>) {
    operator fun plus(other: Vector2D<T>) = Vector2D(ops.plus(x, other.x), ops.plus(y, other.y), ops)
    operator fun minus(other: Vector2D<T>) = Vector2D(ops.minus(x, other.x), ops.minus(y, other.y), ops)
}

data class Area<T>(val width: T, val height: T)

data class Rectangle<T>(val origin: Vector2D<T>, val area: Area<T>, private val ops: NumericalOps<T>) {

    constructor(x: T, y: T, width: T, height: T, ops: NumericalOps<T>): this(Vector2D(x, y, ops), Area(width, height), ops)

    val x0 = origin.x
    val y0 = origin.y
    val x1 = ops.plus(origin.x, area.width)
    val y1 = ops.plus(origin.y, area.height)
    val width = area.width
    val height = area.height

    fun move(d: Vector2D<T>) : Rectangle<T> = copy(origin = origin + d)

    operator fun plus(other: Rectangle<T>): Rectangle<T> {
        val u0 = ops.min(x0, other.x0)
        val v0 = ops.min(y0, other.y0)
        val u1 = ops.max(x1, other.x1)
        val v1 = ops.max(y1, other.y1)

        val width = ops.minus(u1, u0)
        val height = ops.min(v1, v0)
        return Rectangle(u0, v0, width, height, ops)
    }
}