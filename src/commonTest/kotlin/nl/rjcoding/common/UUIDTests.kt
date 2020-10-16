package nl.rjcoding.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UUIDTests {

    @Test
    fun uniquenessTest() {
        val uuid1 = UUID.random()
        val uuid2 = UUID.random()
        assertTrue { uuid1 != uuid2 }
        assertTrue { uuid1 !== uuid2 }
    }

    @Test
    fun fromStringTest() {
        val input = "6ec0bd7f-11c0-43da-975e-2a8ad9ebae0b"
        assertEquals(input, UUID.fromString(input).toString())
    }
}