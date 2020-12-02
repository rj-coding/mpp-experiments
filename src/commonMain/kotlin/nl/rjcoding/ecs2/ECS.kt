package nl.rjcoding.ecs2

import nl.rjcoding.common.Generator
import nl.rjcoding.common.UUID

interface TypeData<Tag, Item> {
    val tag: Tag

    @Suppress("UNCHECKED_CAST")
    fun transform(item: Any): Item? = item as? Item
}

interface HasTypeData<Tag, Item> {
    val typeData: TypeData<Tag, Item>
}

interface ECS<Id, Tag> {
    fun create(): Id?
    fun <Item : HasTypeData<Tag, Item>> set(id: Id, item: Item)
    fun <Item> get(id: Id, typeData: TypeData<Tag, Item>): Item?
}

class HashMapECS<Id, Tag>(private val idGenerator: Generator<Id>) : ECS<Id, Tag> {
    private val data = mutableMapOf<Id, MutableMap<Tag, Any>>()

    override fun create(): Id? = idGenerator.generate()

    override fun <Item : HasTypeData<Tag, Item>> set(id: Id, item: Item) {
        data.getOrPut(id) { mutableMapOf() }[item.typeData.tag] = item
    }

    override fun <Item> get(id: Id, typeData: TypeData<Tag, Item>): Item? {
        val raw = data[id]?.get(typeData.tag)
        if (raw == null) return raw
        return typeData.transform(raw)
    }
}