package nl.avwie.declarative

import kotlin.reflect.KProperty

interface Attribute<V> {

    class Delegate<V>(val attribute: Attribute<V>, val map: MutableMap<Attribute<*>, Any?>, initialValue: V) {

        init {
            map[attribute] = initialValue
        }

        @Suppress("UNCHECKED_CAST")
        operator fun getValue(thisRef: Any?, property: KProperty<*>): V {
            return map[attribute] as V
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: V) {
            map[attribute] = value
        }
    }

}

interface Node {
    val descriptor: Descriptor<*>
    val attributes: Map<Attribute<*>, *>
    val nodes: List<Node>

    @Suppress("UNCHECKED_CAST")
    fun <K, V> getOrNull(key: K): V? where K : Attribute<V>, V : Any? = attributes[key] as? V
    operator fun <K, V> get(key: K): V where K : Attribute<V>, V : Any? = getOrNull(key)!!

    data class Default(override val descriptor: Descriptor<*>, override val attributes: Map<Attribute<*>, *>, override val nodes: List<Node>): Node

    open class Builder(val type: Descriptor<*>) {
        private val attributes = mutableMapOf<Attribute<*>, Any?>()
        private val nodes = mutableListOf<Node>()

        fun <K, V> attribute(key: K, initialValue: V): Attribute.Delegate<V> where K : Attribute<V>, V : Any? {
            return Attribute.Delegate(key, attributes, initialValue)
        }

        operator fun <B> Descriptor<B>.invoke(block: B.() -> Unit) where B : Builder {
            invoke(createBuilder(), block)
        }

        operator fun <B> Descriptor<B>.invoke(builder: B, block: B.() -> Unit) where B : Builder {
            block(builder)
            nodes.add(builder.build())
        }

        fun build(): Node = Default(type, attributes, nodes)
    }

    open class Descriptor<B : Builder>(val createBuilder: () -> B)

    companion object {
        operator fun <B : Builder> invoke(root: Descriptor<B>, block: B.() -> Unit): Node {
            val builder = root.createBuilder()
            block(builder)
            return builder.build()
        }
    }
}