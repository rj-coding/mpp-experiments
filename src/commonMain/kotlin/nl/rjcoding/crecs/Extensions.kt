package nl.rjcoding.crecs

@Suppress("UNCHECKED_CAST")
fun <ComponentType, T : Component<ComponentType>> Component<ComponentType>.into(): T? = this as? T