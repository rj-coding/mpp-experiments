package nl.rjcoding.ecs.decorator

import nl.rjcoding.common.Generator
import nl.rjcoding.common.ReadBus
import nl.rjcoding.common.WriteBus
import nl.rjcoding.ecs.Component
import nl.rjcoding.ecs.ECS

class CRDTDecorator<Id, TypeTag, Timestamp : Comparable<Timestamp>>(
    private val idGenerator: Generator<Id>,
    private val timeStampGenerator: Generator<Timestamp>,
    backend: ECS<Id, TypeTag>,
    readBus: ReadBus<Event<Id, TypeTag, Timestamp>>? = null,
    private val writeBus: WriteBus<Event<Id, TypeTag, Timestamp>>? = null
): AbstractECSDecorator<Id, TypeTag>(backend) {

    init {
        readBus?.subscribe { event ->
            invokeEvent(event)
        }
    }

    private val entityTombstones = mutableSetOf<Id>()
    private val componentStash = mutableMapOf<Id, MutableList<Event<Id, TypeTag, Timestamp>>>()
    private val componentTimestamps = mutableMapOf<Pair<Id, TypeTag>, Timestamp>()

    sealed class Event<Id, out TypeTag, TimeStamp : Comparable<TimeStamp>> {
        abstract val timeStamp: TimeStamp

        data class Create<Id, TypeTag, TimeStamp : Comparable<TimeStamp>>(override val timeStamp: TimeStamp, val id: Id) : Event<Id, TypeTag, TimeStamp>()
        data class Destroy<Id, TypeTag, TimeStamp : Comparable<TimeStamp>>(override val timeStamp: TimeStamp, val id: Id) : Event<Id, TypeTag, TimeStamp>()
        data class Set<Id, TypeTag, TimeStamp : Comparable<TimeStamp>>(override val timeStamp: TimeStamp, val id: Id, val component: Component<TypeTag>) : Event<Id, TypeTag, TimeStamp>()
        data class UnSet<Id, TypeTag, TimeStamp : Comparable<TimeStamp>>(override val timeStamp: TimeStamp, val id: Id, val type: TypeTag) : Event<Id, TypeTag, TimeStamp>()
    }

    override fun create(id: Id?): Id {
        return (id ?: idGenerator.generate()).also { actualId ->
            val timestamp = timeStampGenerator.generate()
            val event : Event<Id, TypeTag, Timestamp> = Event.Create(timestamp, actualId)
            applyEvent(event)
        }
    }

    override fun destroy(id: Id): Boolean {
        val timestamp = timeStampGenerator.generate()
        val event : Event<Id, TypeTag, Timestamp> = Event.Destroy(timestamp, id)
        val returnValue = backend.contains(id)
        applyEvent(event)
        return returnValue
    }

    override fun set(id: Id, component: Component<TypeTag>) {
        val timestamp = timeStampGenerator.generate()
        val event : Event<Id, TypeTag, Timestamp> = Event.Set(timestamp, id, component)
        applyEvent(event)
    }

    override fun unset(id: Id, type: TypeTag): Boolean {
        val timestamp = timeStampGenerator.generate()
        val event : Event<Id, TypeTag, Timestamp> = Event.UnSet(timestamp, id, type)
        val returnValue = backend.has(id, type)
        applyEvent(event)
        return returnValue
    }

    private fun applyEvent(event: Event<Id, TypeTag, Timestamp>) {
        invokeEvent(event)
        writeBus?.write(event)
    }

    private fun invokeEvent(event: Event<Id, TypeTag, Timestamp>) {
        when (event) {
            is Event.Create -> {
                if (entityTombstones.contains(event.id)) return

                backend.create(event.id)
                componentStash[event.id]?.forEach { stashedEvent -> applyEvent(stashedEvent) }
                componentStash.remove(event.id)
            }

            is Event.Destroy -> {
                entityTombstones.add(event.id)
                componentStash.remove(event.id)
                backend.destroy(event.id)
            }

            is Event.Set -> {
                if (entityTombstones.contains(event.id)) return
                else if (backend.contains(event.id)) {
                    val existingTimestamp = componentTimestamps[event.id to event.component.type]
                    if (existingTimestamp == null || event.timeStamp > existingTimestamp) {
                        componentTimestamps[event.id to event.component.type] = event.timeStamp
                        backend.set(event.id, event.component)
                    }
                } else {
                    componentStash.getOrPut(event.id, { mutableListOf() }).add(event)
                }
            }

            is Event.UnSet -> {
                if (entityTombstones.contains(event.id)) return
                else if (backend.contains(event.id)) {
                    val existingTimestamp = componentTimestamps[event.id to event.type]
                    if (existingTimestamp == null || event.timeStamp > existingTimestamp) {
                        componentTimestamps[event.id to event.type] = event.timeStamp
                        backend.unset(event.id, event.type)
                    }
                } else {
                    componentStash.getOrPut(event.id, { mutableListOf() }).add(event)
                }
            }
        }
    }

}