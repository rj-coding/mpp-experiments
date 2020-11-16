package nl.rjcoding.ecs

@Suppress("UNCHECKED_CAST")
fun <T : Component<*>> Component<*>?.into(): T? = this as? T

@Suppress("UNCHECKED_CAST")
fun <T : Component<*>> Component<*>?.into(also: (T) -> Unit): T? =  (this as? T)?.also(also)

fun <Id, TypeTag> ECS<Id, TypeTag>.create(block: (Id) -> Unit) = this.create().also(block)

fun <Id, TypeTag> ECS<Id, TypeTag>.create(id: Id, block: (Id) -> Unit) = this.create(id).also(block)

operator fun <Id, TypeTag> ECS<Id, TypeTag>.get(id: Id) = EntityScope(id, this)

class EntityScope<Id, TypeTag>(val id: Id, val ecs: ECS<Id, TypeTag>) {
    operator fun plusAssign(component: Component<TypeTag>) {
        ecs.set(id, component)
    }

    operator fun minusAssign(typeTag: TypeTag) {
        ecs.unset(id, typeTag)
    }
}

fun <Id, TypeTag> ECS<Id, TypeTag>.hasWhere(id: Id, typeTag: TypeTag, where: (Component<TypeTag>) -> Boolean): Boolean {
    return this.get(id, typeTag)?.let { where(it) } ?: false
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