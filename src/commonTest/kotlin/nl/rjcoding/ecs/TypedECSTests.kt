package nl.rjcoding.ecs

import nl.rjcoding.common.UUID
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals

class TypedECSTests {

    data class Name(val name: String)
    data class Age(val age: Int)

    @Test
    fun typedSetGetTest() {
        val backend = SimpleECS<KClass<out Any>>()
        val ecs = TypedECS(backend)

        val name = Name("Foo")
        val age = Age(42)

        val id = ecs.create()
        ecs.set(id, name)
        ecs.set(id, age)

        val x = ecs.get<Name>(id)
        val y = ecs.get<Age>(id)
        assertEquals("Foo", x?.name)
        assertEquals(42, y?.age)
    }
}