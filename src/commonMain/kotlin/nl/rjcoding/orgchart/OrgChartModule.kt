package nl.rjcoding.orgchart

import nl.rjcoding.ecs.Component
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
            ecs.set(id, OrgChartComponent.Parent(parent))
        }
    }

    fun setAssistant(id: Id, isAssistant: Boolean) {
        if (!ecs.exists(id)) return

        if (isAssistant) {
            val components = ecs.getAll(id)
            if (components.contains(OrgChartComponent.Department) && components.any { it.type == TypeTag.Parent }) {
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