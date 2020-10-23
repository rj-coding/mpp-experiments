package nl.rjcoding.orgchart

import nl.rjcoding.ecs.ECS
import nl.rjcoding.orgchart.OrgChartUtils.getHierarchyMap

object OrgChartPositionSystem {

    fun <Id> position(ecs: ECS<Id, TypeTag>) {
        val hierarchy = getHierarchyMap(ecs)
        val roots = hierarchy.filter { it.value.parent == 0 }

        
    }
}