package nl.rjcoding.ecs2

import nl.rjcoding.common.Generator
import nl.rjcoding.common.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

data class Name(val name: String): HasTypeTag<Name> {
    override val tag = Name
    companion object : TypeTag<Name>
}

data class Age(val age: Int): HasTypeTag<Age> {
    override val tag = Age
    companion object :TypeTag<Age>
}


class ECSTest {

    @Test
    fun simpleTest() {
        val generator = Generator { UUID.random() }
        val ecs = HashMapECS<UUID>(generator)

        val uuid1 = ecs.create()!!
        val uuid2 = ecs.create()!!

        ecs.set(uuid1, Name("Foo"))
        ecs.set(uuid2, Name("Bar"))

        ecs.set(uuid1, Age(35))
        ecs.set(uuid2, Age(32))

        val age2 = ecs.get(uuid2, Age)
        val name1 = ecs.get(uuid1, Name)

        assertEquals(32, age2?.age)
        assertEquals("Foo", name1?.name)
    }
}