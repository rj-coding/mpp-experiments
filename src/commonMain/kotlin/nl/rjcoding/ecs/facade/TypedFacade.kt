package nl.rjcoding.ecs.facade

import nl.rjcoding.ecs.Component
import nl.rjcoding.ecs.ECS
import kotlin.reflect.KClass

data class TypedComponent<T : Any>(val component: T, override val type: KClass<out Any>) : Component<KClass<out Any>>

inline fun <reified T : Any> T.asTypedComponent(): Component<KClass<out Any>> {
    val component = this
    val type = T::class
    return TypedComponent(component, type)
}

fun <Id> ECS<Id, KClass<out Any>>.getAllTyped(id: Id): Set<Any> {
    return this.getAll(id).map { (it as TypedComponent<*>).component }.toSet()
}

class TypedFacade<Id>(
    backend: ECS<Id, KClass<out Any>>
) : AbstractECSFacade<Id, KClass<out Any>>(backend) {

    inline fun <reified T : Any> set(id: Id, component: T) {
        val typed = component.asTypedComponent()
        backend.set(id, typed)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Any> get(id: Id): T? {
        return (backend.get(id, T::class) as? TypedComponent<T>)?.component
    }

    inline fun <reified T : Any> has(id: Id): Boolean {
        return backend.has(id, T::class)
    }

    inline fun <reified T : Any> unset(id: Id) {
        backend.unset(id, T::class)
    }


}