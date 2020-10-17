package nl.rjcoding.ecs

@Suppress("UNCHECKED_CAST")
fun <T : Component<*>> Component<*>?.into(): T? = this as? T