package nl.rjcoding.ecs

import kotlin.reflect.KClass

/*interface TypedComponent<T : Any> : Component<KClass<out Any>> {
    val component: T
}*/

data class TypedComponent<T : Any>(val component: T, override val type: KClass<out Any>) : Component<KClass<out Any>>

inline fun <reified T : Any> T.asTypedComponent(): Component<KClass<out Any>> {
    val component = this
    val type = T::class
    return TypedComponent(component, type)
}

class TypedECS<Id>(
    val backend: ECS<Id, KClass<out Any>>
) : ECS<Id, KClass<out Any>> by backend {

    inline fun <reified T : Any> set(id: Id, component: T) {
        val typed = component.asTypedComponent()
        backend.set(id, typed)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Any> get(id: Id): T? {
        return (backend.get(id, T::class) as? TypedComponent<T>)?.component
    }

    fun getAllTyped(id: Id): Set<Any> {
        return backend.getAll(id).map { (it as TypedComponent<*>).component }.toSet()
    }

    inline fun <reified T : Any> has(id: Id): Boolean {
        return backend.has(id, T::class)
    }

    inline fun <reified T : Any> unset(id: Id) {
        backend.unset(id, T::class)
    }
}