package nl.rjcoding.orgchart

import nl.rjcoding.ecs.ECS
import nl.rjcoding.ecs.Query

class OrgChartPositionSystem<Id>(val ecs: ECS<Id, TypeTag>) {

    val query = Query.Or(
        Query.Has(TypeTag.Department),
        Query.Has(TypeTag.Function)
    )

    fun position() {
        query.execute(ecs)
    }

    /*private fun positionEntities(entities: List<Id>): List<AreaTree<Id>> {
        return entities.map { entity -> positionEntity(entity) }
    }

    private fun positionEntity(entity: Id): AreaTree<Id> {
        return AreaTree.Item(entity)
    }*/
}