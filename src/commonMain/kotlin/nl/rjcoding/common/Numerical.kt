package nl.rjcoding.common

interface NumericalOps<T> {
    fun plus(left: T, right: T): T
    fun minus(left: T, right: T): T
    fun times(left: T, right: T): T
    fun div(left: T, right: T): T

    fun min(left: T, right: T): T
    fun max(left: T, right: T): T

    val zero: T
    val unit: T
}

object Integral : NumericalOps<Int> {
    override fun plus(left: Int, right: Int): Int {
        return left + right
    }

    override fun minus(left: Int, right: Int): Int {
        return left - right
    }

    override fun times(left: Int, right: Int): Int {
        return left * right
    }

    override fun div(left: Int, right: Int): Int {
        return left / right
    }

    override fun min(left: Int, right: Int): Int {
        return kotlin.math.min(left, right)
    }

    override fun max(left: Int, right: Int): Int {
        return kotlin.math.max(left, right)
    }

    override val zero: Int = 0
    override val unit: Int = 1
}

object Fractional : NumericalOps<Fraction> {
    override fun plus(left: Fraction, right: Fraction): Fraction {
        return left + right
    }

    override fun minus(left: Fraction, right: Fraction): Fraction {
        return left - right
    }

    override fun times(left: Fraction, right: Fraction): Fraction {
        return left * right
    }

    override fun div(left: Fraction, right: Fraction): Fraction {
        return left / right
    }

    override fun min(left: Fraction, right: Fraction): Fraction {
        return min(left, right)
    }

    override fun max(left: Fraction, right: Fraction): Fraction {
        return max(left, right)
    }

    override val zero: Fraction = Fraction(0, 0)
    override val unit: Fraction = Fraction(1, 1)
}