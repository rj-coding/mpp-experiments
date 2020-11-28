package nl.rjcoding.orgchart

import nl.rjcoding.ecs.ECS
import nl.rjcoding.ecs.*

class OrgChartModule<Id>(val ecs: ECS<Id, TypeTag>) {

    fun createDepartment(name: String, parent: Id? = null, isAssistant: Boolean = false): Id = ecs.create {
        set(OrgChartComponent.Department)

        setName(name)
        if (parent != null && ecs.contains(parent)) {
            setParent(parent)
            setIsAssistant(isAssistant)
        }
    }

    fun createFunction(name: String, parent: Id): Id? {
        if (ecs.contains(parent)) {
            return ecs.create {
                set(OrgChartComponent.Function)
                setName(name)
                setParent(parent)
            }
        }
        return null
    }

    fun destroy(id: Id) {
        if (!ecs.contains(id)) return

        childrenOf(id).forEach { childId ->
            destroy(childId)
        }
        ecs.destroy(id)
    }

    fun childrenOf(id: Id): Set<Id> {
        if (!ecs.contains(id)) return setOf()

        return ecs.entities().filter { childId ->
            ecs.get(childId, TypeTag.Parent).into<OrgChartComponent.Parent<Id>>()?.id == id
        }.toSet()
    }
}