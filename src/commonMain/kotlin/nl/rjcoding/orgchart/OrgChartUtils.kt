package nl.rjcoding.orgchart

import nl.rjcoding.ecs.ECS
import nl.rjcoding.ecs.into

object OrgChartUtils {

    data class HierarchyLinks<Id>(val parent: Id?, val children: Set<Id>)

    fun <Id> getHierarchyMap(ecs: ECS<Id, TypeTag>): Map<Id, HierarchyLinks<Id>> {
        val map = mutableMapOf<Id, HierarchyLinks<Id>>()

        ecs.entities().forEach { id ->
            map.getOrPut(id, { HierarchyLinks(null, setOf()) })
            ecs.get(id, TypeTag.Parent).into<OrgChartComponent.Parent<Id>>()?.let { parent ->
                map.getOrPut(parent.id, { HierarchyLinks(null, setOf()) })

                map[id] = map[id]!!.copy(parent = parent.id)
                map[parent.id] = map[parent.id]!!.copy(children = map[parent.id]!!.children + id)
            }
        }
        return map
    }
}