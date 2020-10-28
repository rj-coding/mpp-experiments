package nl.rjcoding.ecs

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

enum class TypeTag {
    Age,
    Name,
    Color
}

abstract class TestComponent(override val type: TypeTag): Component<TypeTag>

data class Age(val value: Int): TestComponent(TypeTag.Age)
data class Name(val value: String): TestComponent(TypeTag.Name)
data class Color(val value: Int): TestComponent(TypeTag.Color)

abstract class ECSTests<Id> {
    abstract fun createECS(): ECS<Id, TypeTag>
    abstract fun generateId(): Id

    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

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
    fun containtsTest() {
        val ecs = createECS()
        val id = generateId()

        assertFalse { id in ecs }
        ecs.create(id)
        assertTrue { id in ecs }
    }

    @Test
    fun destroyTest() {
        val ecs = createECS()
        val id = ecs.create()
        assertFalse { ecs.destroy(generateId()) }
        assertTrue { ecs.destroy(id) }
        assertFalse { ecs.contains(id) }
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
        assertTrue { components.components().contains(Age(42)) }
        assertTrue { components.components().contains(Name("Foo")) }
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

    fun compare(first: ECS<Id, TypeTag>, second: ECS<Id, TypeTag>) {
        val entitiesFirst = first.entities().toSet()
        val entitiesSecond = second.entities().toSet()
        assertEquals(entitiesFirst, entitiesSecond)

        entitiesFirst.forEach { id ->
            val componentsFirst = first.getAll(id)
            val componentsSecond = second.getAll(id)
            assertEquals(componentsFirst, componentsSecond)
        }
    }

    fun fillRandom(ecs: ECS<Id, TypeTag>, entities: Int, components: Int) {
        (0 until entities).forEach { _ ->
            val id = ecs.create()
            (0 until components).forEach { _ ->
                randomComponent()?.also { component ->
                    ecs.set(id, component)
                }

                if (Random.nextFloat() < 0.2) {
                    randomComponent()?.also { component ->
                        ecs.unset(id, component.type)
                    }
                }
            }

            if (Random.nextFloat() < 0.2) {
                ecs.destroy(id)
            }
        }
    }

    private fun randomComponent(): TestComponent? {
        return when (Random.nextInt(3)) {
            0 -> {
                val name = (1..10)
                    .map { i -> Random.nextInt(0, charPool.size) }
                    .map(charPool::get)
                    .joinToString("")
                Name(name)
            }

            1 -> Age(Random.nextInt(100))
            2 -> Color(Random.nextInt())
            else -> null
        }
    }
}