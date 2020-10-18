package nl.rjcoding.common

fun interface Generator<T> {
    fun generate(): T
}