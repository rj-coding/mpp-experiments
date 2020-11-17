package nl.rjcoding.common

data class Fraction(val num: Int, val den: Int) {
    constructor(a: Int) : this(a, 1)

    fun simplify() = Integral.gcd(num, den).let { Fraction(num / it, den / it) }
    fun inverse() = Fraction(den, num)

    operator fun plus(other: Fraction) = Fraction(num * other.den + other.num * den, den * other.den)
    operator fun times(other: Fraction) = Fraction(num * other.num, den * other.den)

    operator fun minus(other: Fraction) = plus(other.times(Fraction(-1)))
    operator fun div(other: Fraction) = times(other.inverse())

    operator fun compareTo(other: Fraction): Int {
        val a = this.simplify()
        val b = other.simplify()
        return a.num.compareTo(b.num)
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Int -> equals(Fraction(other, 1))
            is Fraction -> {
                val a = this.simplify()
                val b = other.simplify()
                a.num == b.num && a.den == b.den
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        val s = simplify()
        return 31 * s.num.hashCode() + s.den.hashCode()
    }

    fun toDouble(): Double = num.toDouble() / den.toDouble()

}

fun frac(num: Int, den: Int): Fraction = Fraction(num, den)
fun frac(a: Int): Fraction = Fraction(a)

fun min(a: Fraction, b: Fraction): Fraction {
    if (a.num * b.den <= b.num * a.den) return a
    return b
}

fun max(a: Fraction, b: Fraction): Fraction {
    if (a.num * b.den >= b.num * a.den) return a
    return b
}