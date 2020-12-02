package nl.rjcoding.ecs2

import nl.rjcoding.common.Generator
import nl.rjcoding.common.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

sealed class Type {
    object Name : Type(), TypeData<Type, Component.Name> {
        override val tag: Type = this
    }

    object Age : Type(), TypeData<Type, Component.Age> {
        override val tag: Type = this
    }
}

sealed class Component<Item>(override val typeData: TypeData<Type, Item>) : HasTypeData<Type, Item> {
    data class Name(val name: String): Component<Name>(Type.Name)
    data class Age(val age: Int): Component<Age>(Type.Age)
}

class ECSTest {

    @Test
    fun simpleTest() {
        val generator = Generator { UUID.random() }
        val ecs = HashMapECS<UUID, Type>(generator)

        val uuid1 = ecs.create()!!
        val uuid2 = ecs.create()!!
        ecs.set(uuid1, Component.Age(35))

        ecs.set(uuid2, Component.Name("Mieky"))
        ecs.set(uuid2, Component.Age(32))

        val age1 = ecs.get(uuid1, Type.Age)
        val age2 = ecs.get(uuid2, Type.Age)
        val name2 = ecs.get(uuid2, Type.Name)

        assertEquals(35, age1?.age)
        assertEquals(32, age2?.age)
        assertEquals("Mieky", name2?.name)
    }
}