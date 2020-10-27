package nl.rjcoding.ecs

interface ComponentContainer<TypeTag> {

    val size: Int

    fun typeTags(): Sequence<TypeTag>
    fun components(): Sequence<Component<TypeTag>>

    operator fun contains(typeTag: TypeTag): Boolean
    operator fun get(typeTag: TypeTag): Component<TypeTag>?
}