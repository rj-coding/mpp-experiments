package nl.rjcoding.ecs

import nl.rjcoding.common.UUID

class SimpleECS<ComponentType> : ECS<UUID, ComponentType> {

    private val components = mutableMapOf<UUID, MutableMap<ComponentType, Component<ComponentType>>>()

    override fun create(id: UUID?): UUID {
        return (id ?: UUID.random()).also {uuid ->
            components[uuid] = mutableMapOf()
        }
    }

    override fun exists(id: UUID): Boolean {
        return components.containsKey(id)
    }

    private fun requireExists(id: UUID) {
        require(exists(id)) { "Entity $id does not exist" }
    }

    override fun destroy(id: UUID): Boolean {
        return components.remove(id) != null
    }

    override fun set(id: UUID, component: Component<ComponentType>) {
        requireExists(id)
        components[id]!![component.type] = component
    }

    override fun has(id: UUID, type: ComponentType): Boolean {
        requireExists(id)
        return components[id]!!.containsKey(type)
    }

    override fun get(id: UUID, type: ComponentType): Component<ComponentType>? {
        requireExists(id)
        return components[id]!![type]
    }

    override fun getAll(id: UUID): Set<Component<ComponentType>> {
        requireExists(id)
        return components[id]!!.entries.map { it.value }.toSet()
    }

    override fun unset(id: UUID, type: ComponentType): Boolean {
        requireExists(id)
        return components[id]!!.remove(type) != null
    }

    override fun entities(): Sequence<UUID> {
        return sequenceOf(*components.keys.toTypedArray())
    }
}