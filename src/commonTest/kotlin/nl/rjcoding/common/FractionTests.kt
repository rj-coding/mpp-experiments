package nl.rjcoding.common

import kotlin.test.Test
import kotlin.test.assertEquals

class FractionTests {

    @Test
    fun simplifyTest() {
        val f = frac(3, 9).simplify()
        assertEquals(1, f.num)
        assertEquals(3, f.den)
    }

    @Test
    fun equalsTest() {
        assertEquals(frac(2, 3), frac(6, 9))
        assertEquals(frac(17,11), frac(17 * 121, 11 * 121))
    }

    @Test
    fun hashCodeTest() {
        assertEquals(frac(2, 3).hashCode(), frac(6, 9).hashCode())
        assertEquals(frac(17,11).hashCode(), frac(17 * 121, 11 * 121).hashCode())
    }

    @Test
    fun arithmeticTest() {
        assertEquals(frac(5, 8), frac(2, 4) + frac(1, 8))
        assertEquals(frac(2, 4), frac(5, 8) - frac(1, 8))
        assertEquals(frac(14, 15), frac(2, 5) * frac(7, 3))
        assertEquals(frac(14, 15), frac(2, 5) / frac(3, 7))
    }
}