package nl.rjcoding.orgchart

import nl.rjcoding.orgchart.OrgChartComponent.Position
import nl.rjcoding.orgchart.OrgChartComponent.Children
import nl.rjcoding.ecs.ECS
import nl.rjcoding.ecs.into
import nl.rjcoding.orgchart.util.AreaTree

class OrgChartPositionSystem<Id>(val ecs: ECS<Id, TypeTag>) {

    fun position() {
        val roots = ecs.entities().filter { id ->
            ecs.has(id, TypeTag.Department) && !ecs.has(id, TypeTag.Parent)
        }.toList()

        positionEntities(roots)
    }

    private fun positionEntities(entities: List<Id>): List<AreaTree<Id>> {
        return entities.map { entity -> positionEntity(entity) }
    }

    private fun positionEntity(entity: Id): AreaTree<Id> {
        val tree = AreaTree(entity)
        return tree
    }
}