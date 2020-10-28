package nl.rjcoding.ecs

@Suppress("UNCHECKED_CAST")
fun <T : Component<*>> Component<*>?.into(): T? = this as? T

fun <Id, TypeTag> ECS<Id, TypeTag>.hasWhere(id: Id, typeTag: TypeTag, where: (Component<TypeTag>) -> Boolean): Boolean {
    return this[id, typeTag]?.let { where(it) } ?: false
}

fun <Id, TypeTag> ECS<Id, TypeTag>.hasEquals(id: Id, typeTag: TypeTag, equals: Component<TypeTag>): Boolean {
    return this.hasWhere(id, typeTag) { it == equals }
}

fun <TypeTag> ComponentContainer<TypeTag>.hasWhere(typeTag: TypeTag, where: (Component<TypeTag>) -> Boolean): Boolean {
    return this[typeTag]?.let { where(it) } ?: false
}

fun <TypeTag> ComponentContainer<TypeTag>.hasEquals(typeTag: TypeTag, equals: Component<TypeTag>): Boolean {
    return this.hasWhere(typeTag) { it == equals }
}