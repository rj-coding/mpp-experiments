package nl.rjcoding.ecs

interface Component<TypeTag> {
    val type: TypeTag
}

interface ECS<Id, TypeTag> {
    fun create(id: Id? = null): Id
    fun exists(id: Id): Boolean
    fun destroy(id: Id): Boolean

    fun set(id: Id, component: Component<TypeTag>)
    fun has(id: Id, type: TypeTag): Boolean
    fun get(id: Id, type: TypeTag): Component<TypeTag>?
    fun getAll(id: Id): Set<Component<TypeTag>>
    fun unset(id: Id, type: TypeTag): Boolean

    fun entities(): Sequence<Id>
}