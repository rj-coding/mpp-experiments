package nl.rjcoding.ecs.decorator

import nl.rjcoding.common.Generator
import nl.rjcoding.common.InMemoryBus
import nl.rjcoding.common.UUID
import nl.rjcoding.ecs.ECS
import nl.rjcoding.ecs.ECSTests
import nl.rjcoding.ecs.SimpleECS
import nl.rjcoding.ecs.TypeTag
import kotlin.test.Test

class CRDTDecoratorTests : ECSTests<UUID>() {

    val idGenerator = Generator {
        UUID.random()
    }

    var lastTimestamp = 0L

    val timestampGenerator = Generator {
        lastTimestamp += 1
        lastTimestamp
    }

    override fun createECS(): ECS<UUID, TypeTag> {
        return CRDTDecorator(
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
        val bus = InMemoryBus<CRDTDecorator.Event<UUID, TypeTag, Long>>()
        val ecs = CRDTDecorator(idGenerator, timestampGenerator, backend, writeBus = bus)

        val events = mutableListOf<CRDTDecorator.Event<UUID, TypeTag, Long>>()
        bus.subscribe {
            events.add(it)
        }
        fillRandom(ecs, 10, 100)

        val replayBackend = SimpleECS<TypeTag>()
        val replayBus = InMemoryBus<CRDTDecorator.Event<UUID, TypeTag, Long>>()
        val replayEcs = CRDTDecorator(idGenerator, timestampGenerator, replayBackend, readBus = replayBus)
        events.forEach { event ->
            replayBus.write(event)
        }

        compare(ecs, replayEcs)
    }

    @Test
    fun replayUnorderedTest() {
        val backend = SimpleECS<TypeTag>()
        val bus = InMemoryBus<CRDTDecorator.Event<UUID, TypeTag, Long>>()
        val ecs = CRDTDecorator(idGenerator, timestampGenerator, backend, writeBus = bus)

        val events = mutableListOf<CRDTDecorator.Event<UUID, TypeTag, Long>>()
        bus.subscribe {
            events.add(it)
        }
        fillRandom(ecs, 10, 100)

        val replayBackend = SimpleECS<TypeTag>()
        val replayBus = InMemoryBus<CRDTDecorator.Event<UUID, TypeTag, Long>>()
        val replayEcs = CRDTDecorator(idGenerator, timestampGenerator, replayBackend, readBus = replayBus)
        events.shuffled().forEach { event ->
            replayBus.write(event)
        }

        compare(ecs, replayEcs)
    }
}