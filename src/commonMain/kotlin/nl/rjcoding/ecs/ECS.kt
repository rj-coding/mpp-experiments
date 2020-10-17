package nl.rjcoding.ecs

interface Component<ComponentType> {
    val type: ComponentType
}

interface ECS<Id, ComponentType> {
    fun create(id: Id? = null): Id
    fun exists(id: Id): Boolean
    fun destroy(id: Id): Boolean

    fun set(id: Id, component: Component<ComponentType>)
    fun has(id: Id, type: ComponentType): Boolean
    fun get(id: Id, type: ComponentType): Component<ComponentType>?
    fun getAll(id: Id): Set<Component<ComponentType>>
    fun unset(id: Id, type: ComponentType): Boolean

    fun entities(): Sequence<Id>
}