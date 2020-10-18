package nl.rjcoding.ecs

import nl.rjcoding.common.Generator
import nl.rjcoding.common.InMemoryBus
import nl.rjcoding.common.UUID
import kotlin.test.Test

class CRECSTests : ECSTests<UUID>() {

    val idGenerator = Generator {
        UUID.random()
    }

    var lastTimestamp = 0L

    val timestampGenerator = Generator {
        lastTimestamp += 1
        lastTimestamp
    }

    override fun createECS(): ECS<UUID, TypeTag> {
        return CRECS(
            idGenerator,
            timestampGenerator,
            SimpleECS()
        )
    }

    override fun generateId(): UUID {
        return UUID.random()
    }

    @Test
    fun replayTest() {
        val backend = SimpleECS<TypeTag>()
        val bus = InMemoryBus<CRECS.Event<UUID, TypeTag, Long>>()
        val ecs = CRECS(idGenerator, timestampGenerator, backend, writeBus = bus)

        val events = mutableListOf<CRECS.Event<UUID, TypeTag, Long>>()
        bus.subscribe {
            events.add(it)
        }
        fillRandom(ecs, 10, 100)

        val replayBackend = SimpleECS<TypeTag>()
        val replayBus = InMemoryBus<CRECS.Event<UUID, TypeTag, Long>>()
        val replayEcs = CRECS(idGenerator, timestampGenerator, replayBackend, readBus = replayBus)
        events.forEach { event ->
            replayBus.write(event)
        }

        compare(ecs, replayEcs)
    }

    @Test
    fun replayUnorderedTest() {
        val backend = SimpleECS<TypeTag>()
        val bus = InMemoryBus<CRECS.Event<UUID, TypeTag, Long>>()
        val ecs = CRECS(idGenerator, timestampGenerator, backend, writeBus = bus)

        val events = mutableListOf<CRECS.Event<UUID, TypeTag, Long>>()
        bus.subscribe {
            events.add(it)
        }
        fillRandom(ecs, 10, 100)

        val replayBackend = SimpleECS<TypeTag>()
        val replayBus = InMemoryBus<CRECS.Event<UUID, TypeTag, Long>>()
        val replayEcs = CRECS(idGenerator, timestampGenerator, replayBackend, readBus = replayBus)
        events.shuffled().forEach { event ->
            replayBus.write(event)
        }

        compare(ecs, replayEcs)
    }
}