package nl.rjcoding.ecs

import nl.rjcoding.common.UUID

class SimpleECS<TypeTag> : ECS<UUID, TypeTag> {

    private val components = mutableMapOf<UUID, MutableMap<TypeTag, Component<TypeTag>>>()

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

    override fun set(id: UUID, component: Component<TypeTag>) {
        requireExists(id)
        components[id]!![component.type] = component
    }

    override fun has(id: UUID, type: TypeTag): Boolean {
        requireExists(id)
        return components[id]!!.containsKey(type)
    }

    override fun get(id: UUID, type: TypeTag): Component<TypeTag>? {
        requireExists(id)
        return components[id]!![type]
    }

    override fun getAll(id: UUID): Components<TypeTag> {
        requireExists(id)
        return components[id]!!
    }

    override fun unset(id: UUID, type: TypeTag): Boolean {
        requireExists(id)
        return components[id]!!.remove(type) != null
    }

    override fun entities(): Sequence<UUID> {
        return sequenceOf(*components.keys.toTypedArray())
    }
}