package nl.rjcoding.ecs

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

enum class TypeTag {
    Age,
    Name
}

abstract class TestComponent(override val type: TypeTag): Component<TypeTag>

data class Age(val value: Int): TestComponent(TypeTag.Age)
data class Name(val value: String): TestComponent(TypeTag.Name)

abstract class ECSTests<Id> {
    abstract fun createECS(): ECS<Id, TypeTag>
    abstract fun generateId(): Id

    @Test
    fun createTest() {
        val ecs = createECS()
        val id1 = ecs.create()
        val id2 = ecs.create()

        assertTrue { id1 != id2 }
        assertTrue { id1 !== id2 }

        val existingId = generateId()
        val id3 = ecs.create(existingId)
        assertEquals(existingId, id3)
    }

    @Test
    fun existsTest() {
        val ecs = createECS()
        val id = generateId()

        assertFalse { ecs.exists(id) }
        ecs.create(id)
        assertTrue { ecs.exists(id) }
    }

    @Test
    fun destroyTest() {
        val ecs = createECS()
        val id = ecs.create()
        assertFalse { ecs.destroy(generateId()) }
        assertTrue { ecs.destroy(id) }
        assertFalse { ecs.exists(id) }
    }

    @Test
    fun setHasTest() {
        val ecs = createECS()
        val id = ecs.create()

        ecs.set(id, Age(42))
        assertTrue { ecs.has(id, TypeTag.Age) }
        assertFalse { ecs.has(id, TypeTag.Name) }
    }

    @Test
    fun setGetTest() {
        val ecs = createECS()
        val id = ecs.create()

        ecs.set(id, Age(42))
        var retrieved : Age? = ecs.get(id, TypeTag.Age).into()
        assertEquals(Age(42), retrieved)

        ecs.set(id, Age(43))
        retrieved = ecs.get(id, TypeTag.Age).into()
        assertEquals(Age(43), retrieved)
    }

    @Test
    fun setUnsetTest() {
        val ecs = createECS()
        val id = ecs.create()

        ecs.set(id, Age(42))
        ecs.unset(id, TypeTag.Age)
        assertFalse { ecs.has(id, TypeTag.Age) }
    }

    @Test
    fun getAllTest() {
        val ecs = createECS()
        val id = ecs.create()
        ecs.set(id, Age(42))
        ecs.set(id, Name("Foo"))

        val components = ecs.getAll(id)
        assertEquals(2, components.size)
        assertTrue { components.contains(Age(42)) }
        assertTrue { components.contains(Name("Foo")) }
    }

    @Test
    fun entitiesTest() {
        val ecs = createECS()
        (0 until 100).forEach { _ ->
            ecs.create()
        }

        val entities = ecs.entities().distinct().toList()
        assertEquals(100, entities.size)
    }
}