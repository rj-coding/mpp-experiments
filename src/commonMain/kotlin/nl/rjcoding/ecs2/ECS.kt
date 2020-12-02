package nl.rjcoding.ecs2

import nl.rjcoding.common.Generator

fun interface TypeFetcher<Item> {
    fun fetch(item: Any): Item?
}

interface TypeTag<Item> {
    @Suppress("UNCHECKED_CAST")
    fun typeFetcher(): TypeFetcher<Item> = TypeFetcher { item -> item as? Item }
}

interface HasTypeTag<Item> {
    val tag: TypeTag<Item>
}

interface ECS<Id> {
    fun create(): Id?
    fun <Item : HasTypeTag<Item>> set(id: Id, item: Item)
    fun <Item> get(id: Id, tag: TypeTag<Item>): Item?
}

class HashMapECS<Id>(private val idGenerator: Generator<Id>) : ECS<Id> {

    private val data = mutableMapOf<Id, MutableMap<TypeTag<*>, Any>>()

    override fun create(): Id? = idGenerator.generate()

    override fun <Item : HasTypeTag<Item>> set(id: Id, item: Item) {
        data.getOrPut(id) { mutableMapOf() }[item.tag] = item
    }

    override fun <Item> get(id: Id, tag: TypeTag<Item>): Item? {
        val raw = data[id]?.get(tag)
        if (raw == null) return raw
        return tag.typeFetcher().fetch(raw)
    }
}
