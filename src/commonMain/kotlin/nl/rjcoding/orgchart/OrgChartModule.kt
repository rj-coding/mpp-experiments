package nl.rjcoding.orgchart

import nl.rjcoding.ecs.ECS
import nl.rjcoding.ecs.into

class OrgChartModule<Id>(val ecs: ECS<Id, TypeTag>) {

    fun createDepartment(name: String, parent: Id? = null, isAssistant: Boolean = false): Id = ecs.create().also { id ->
        ecs.set(id, OrgChartComponent.Department)
        setName(id, name)
        if (parent != null && ecs.exists(parent)) {
            setParent(id, parent)
            setAssistant(id, isAssistant)
        }
    }

    fun createFunction(name: String, parent: Id): Id? {
        if (ecs.exists(parent)) {
            return ecs.create().also { id ->
                ecs.set(id, OrgChartComponent.Function)
                setName(id, name)
                setParent(id, parent)
            }
        }
        return null
    }

    fun setName(id: Id, name: String) {
        if (!ecs.exists(id)) return
        ecs.set(id, OrgChartComponent.Name(name))
    }

    fun setParent(id: Id, parent: Id?) {
        if (!ecs.exists(id)) return

        if (parent == null && ecs.has(id, TypeTag.Department)) {
            ecs.unset(id, TypeTag.Parent)
        } else if (ecs.exists(parent!!)) {
            ecs.get(id, TypeTag.Parent).into<OrgChartComponent.Parent<Id>>()?.also { oldParent ->
                ecs.get(oldParent.id, TypeTag.Children).into<OrgChartComponent.Children<Id>>()?.also { oldChildren ->
                    ecs.set(oldParent.id, OrgChartComponent.Children(oldChildren.children - id))
                }
            }

            ecs.set(id, OrgChartComponent.Parent(parent))

            if (!ecs.has(parent, TypeTag.Children)) {
                ecs.set(parent, OrgChartComponent.Children(listOf<Id>(id)))
            } else {
                ecs.set(parent, OrgChartComponent.Children(ecs.get(parent, TypeTag.Children).into<OrgChartComponent.Children<Id>>()!!.children + id))
            }
        }
    }

    fun setAssistant(id: Id, isAssistant: Boolean) {
        if (!ecs.exists(id)) return

        if (isAssistant) {
            val components = ecs.getAll(id)
            if (components.containsKey(TypeTag.Department) && components.containsKey(TypeTag.Parent)) {
                ecs.set(id, OrgChartComponent.Assistant)
            }
        } else {
            ecs.unset(id, TypeTag.Assistant)
        }
    }

    fun destroy(id: Id) {
        if (!ecs.exists(id)) return

        childrenOf(id).forEach { childId ->
            destroy(childId)
        }
        ecs.destroy(id)
    }

    fun childrenOf(id: Id): Set<Id> {
        if (!ecs.exists(id)) return setOf()

        return ecs.entities().filter { childId ->
            ecs.get(childId, TypeTag.Parent).into<OrgChartComponent.Parent<Id>>()?.id == id
        }.toSet()
    }
}