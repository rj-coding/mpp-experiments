package nl.rjcoding.orgchart

import nl.rjcoding.ecs.ECS
import nl.rjcoding.ecs.*

class OrgChartModule<Id>(val ecs: ECS<Id, TypeTag>) {

    fun createDepartment(name: String, parent: Id? = null, isAssistant: Boolean = false): Id = ecs.create { id ->
        ecs[id] += OrgChartComponent.Department

        setName(id, name)
        if (parent != null && ecs.contains(parent)) {
            setParent(id, parent)
            setAssistant(id, isAssistant)
        }
    }

    fun createFunction(name: String, parent: Id): Id? {
        if (ecs.contains(parent)) {
            return ecs.create { id ->
                ecs[id] += OrgChartComponent.Function

                setName(id, name)
                setParent(id, parent)
            }
        }
        return null
    }

    fun setName(id: Id, name: String) {
        if (!ecs.contains(id)) return
        ecs[id] += OrgChartComponent.Name(name)
    }

    fun setParent(id: Id, parent: Id?) {
        if (!ecs.contains(id)) return

        if (parent == null && ecs.hasEquals(id, TypeTag.Kind, OrgChartComponent.Department)) {
            ecs.unset(id, TypeTag.Parent)
        } else if (ecs.contains(parent!!)) {
            ecs.get(id, TypeTag.Parent).into<OrgChartComponent.Parent<Id>>() { oldParent ->
                ecs.get(oldParent.id, TypeTag.Children).into<OrgChartComponent.Children<Id>>()?.also { oldChildren ->
                    ecs[oldParent.id] += OrgChartComponent.Children(oldChildren.children - id)
                }
            }

            ecs[id] += OrgChartComponent.Parent(parent)

            if (!ecs.has(parent, TypeTag.Children)) {
                ecs[parent] += OrgChartComponent.Children(listOf(id))
            } else {
                ecs[parent] += OrgChartComponent.Children(ecs.get(parent, TypeTag.Children).into<OrgChartComponent.Children<Id>>()!!.children + id)
            }
        }
    }

    fun setAssistant(id: Id, isAssistant: Boolean) {
        if (!ecs.contains(id)) return

        if (isAssistant) {
            val components = ecs.getAll(id)
            if (components.hasEquals(TypeTag.Kind, OrgChartComponent.Department) && components.has(TypeTag.Parent)) {
                ecs[id] += OrgChartComponent.Assistant
            }
        } else {
            ecs[id] -= TypeTag.Assistant
        }
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