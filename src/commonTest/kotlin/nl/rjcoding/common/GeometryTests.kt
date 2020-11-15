package nl.rjcoding.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GeometryTests {

    @Test
    fun orientationIntegralTest() {
        val v1 = Integral.geometry.vector2D(0, 0)
        val v2 = Integral.geometry.vector2D(1, 0)
        val v3 = Integral.geometry.vector2D(2, 0)

        val v4 = Integral.geometry.vector2D(1, 1)
        val v5 = Integral.geometry.vector2D(1, -1)

        assertEquals(RotationalOrientation.Collinear, Integral.geometry.vector2D.orientation(v1, v2, v3))
        assertEquals(RotationalOrientation.CounterClockwise, Integral.geometry.vector2D.orientation(v1, v2, v4))
        assertEquals(RotationalOrientation.Clockwise, Integral.geometry.vector2D.orientation(v1, v2, v5))
    }

    @Test
    fun intersectionIntegralTest() {
        val l1 = Integral.geometry.line(0, 0, 2, 0)
        val l2 = Integral.geometry.line(1, 1, 1, -1)
        val l3 = Integral.geometry.line(1, 1, 2, 2)

        assertTrue { l1.intersects(l2) }
        assertFalse { l1.intersects(l3) }
    }

    @Test
    fun orientationRationalTest() {
        val v1 = Rational.geometry.vector2D(0.0, 0.0)
        val v2 = Rational.geometry.vector2D(1.0, 0.0)
        val v3 = Rational.geometry.vector2D(2.0, 0.0)

        val v4 = Rational.geometry.vector2D(1.0, 1.0)
        val v5 = Rational.geometry.vector2D(1.0, -1.0)

        assertEquals(RotationalOrientation.Collinear, Rational.geometry.vector2D.orientation(v1, v2, v3))
        assertEquals(RotationalOrientation.CounterClockwise, Rational.geometry.vector2D.orientation(v1, v2, v4))
        assertEquals(RotationalOrientation.Clockwise, Rational.geometry.vector2D.orientation(v1, v2, v5))
    }

    @Test
    fun intersectionRationalTest() {
        val l1 = Rational.geometry.line(0.0, 0.0, 2.0, 0.0)
        val l2 = Rational.geometry.line(1.0, 1.0, 1.0, -1.0)
        val l3 = Rational.geometry.line(1.0, 1.0, 2.0, 2.0)

        assertTrue { l1.intersects(l2) }
        assertFalse { l1.intersects(l3) }
    }

}