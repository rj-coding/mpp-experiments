package nl.rjcoding.ecs

import kotlin.reflect.KClass

interface TypedComponent<T : Any> : Component<KClass<out Any>> {
    val component: T
}

inline fun <reified T : Any> T.asTypedComponent(): Component<KClass<out Any>> {
    val component = this
    val typeTag = T::class

    return object : TypedComponent<T> {
        override val component: T = component
        override val type: KClass<T> = typeTag
    }
}

class TypedECS<Id>(
    private val backend: ECS<Id, KClass<out Any>>
) : ECS<Id, KClass<out Any>> by backend {

    inline fun <reified T : Any> set(id: Id, component: T) {
        val typed = component.asTypedComponent()
        this.set(id, typed)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Any> get(id: Id): T? {
        val typeTag = T::class
        return (this.get(id, typeTag) as? TypedComponent<T>)?.component
    }
}