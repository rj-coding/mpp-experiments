package nl.rjcoding.ecs

import nl.rjcoding.common.Generator
import nl.rjcoding.common.ReadBus
import nl.rjcoding.common.WriteBus

class CRECS<Id, TypeTag, Timestamp : Comparable<Timestamp>>(
    private val idGenerator: Generator<Id>,
    private val timeStampGenerator: Generator<Timestamp>,
    private val backend: ECS<Id, TypeTag>,
    private val readBus: ReadBus<Event<Id, TypeTag, Timestamp>>? = null,
    private val writeBus: WriteBus<Event<Id, TypeTag, Timestamp>>? = null
): ECS<Id, TypeTag> {

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
        return (id ?: idGenerator.generate()).also { id ->
            val timestamp = timeStampGenerator.generate()
            val event : Event<Id, TypeTag, Timestamp> = Event.Create(timestamp, id)
            applyEvent(event)
        }
    }

    override fun destroy(id: Id): Boolean {
        val timestamp = timeStampGenerator.generate()
        val event : Event<Id, TypeTag, Timestamp> = Event.Destroy(timestamp, id)
        val returnValue = backend.exists(id)
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

    override fun exists(id: Id): Boolean = backend.exists(id)
    override fun entities(): Sequence<Id> = backend.entities()
    override fun has(id: Id, type: TypeTag): Boolean = backend.has(id, type)
    override fun get(id: Id, type: TypeTag): Component<TypeTag>? = backend.get(id, type)
    override fun getAll(id: Id): Set<Component<TypeTag>> = backend.getAll(id)

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
                else if (backend.exists(event.id)) {
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
                else if (backend.exists(event.id)) {
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