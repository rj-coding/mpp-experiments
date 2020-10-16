package nl.rjcoding.jsinterop

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class UUIDAdapterTest {

    @Test
    fun v4Test() {
        val uuid1 = UUIDAdapter.v4()
        val uuid2 = UUIDAdapter.v4()

        assertTrue { uuid1 != uuid2 }
    }

    @Test
    fun validateTest() {
        val uuid = UUIDAdapter.v4()
        assertTrue { UUIDAdapter.validate(uuid) }
        assertFalse { UUIDAdapter.validate("Foo") }
    }
}