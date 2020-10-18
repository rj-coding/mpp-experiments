package nl.rjcoding.orgchart

import nl.rjcoding.common.UUID
import nl.rjcoding.ecs.Component
import nl.rjcoding.ecs.ECS
import nl.rjcoding.ecs.into

class OrgChartSystem(val ecs: ECS<UUID, TypeTag>) {

    enum class TypeTag {
        Name,
        Parent,
        Assistant,
        Department,
        Function
    }

    sealed class OrgChartComponent(override val type: TypeTag) : Component<TypeTag> {
        data class Name(val name: String) : OrgChartComponent(TypeTag.Name)
        data class Parent(val id: UUID) : OrgChartComponent(TypeTag.Parent)
        object Assistant : OrgChartComponent(TypeTag.Assistant)
        object Department : OrgChartComponent(TypeTag.Department)
        object Function : OrgChartComponent(TypeTag.Function)
    }

    fun createDepartment(name: String, parent: UUID? = null, isAssistant: Boolean = false): UUID = ecs.create().also { id ->
        ecs.set(id, OrgChartComponent.Department)
        setName(id, name)
        if (parent != null && ecs.exists(parent)) {
            setParent(id, parent)
            setAssistant(id, isAssistant)
        }
    }

    fun createFunction(name: String, parent: UUID): UUID? {
        if (ecs.exists(parent)) {
            return ecs.create().also { id ->
                ecs.set(id, OrgChartComponent.Function)
                setName(id, name)
                setParent(id, parent)
            }
        }
        return null
    }

    fun setName(id: UUID, name: String) {
        if (!ecs.exists(id)) return
        ecs.set(id, OrgChartComponent.Name(name))
    }

    fun setParent(id: UUID, parent: UUID?) {
        if (!ecs.exists(id)) return

        if (parent == null && ecs.has(id, TypeTag.Department)) {
            ecs.unset(id, TypeTag.Parent)
        } else if (ecs.exists(parent!!)) {
            ecs.set(id, OrgChartComponent.Parent(parent))
        }
    }

    fun setAssistant(id: UUID, isAssistant: Boolean) {
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

    fun destroy(id: UUID) {
        if (!ecs.exists(id)) return

        childrenOf(id).forEach { childId ->
            destroy(childId)
        }
        ecs.destroy(id)
    }

    fun childrenOf(id: UUID): Set<UUID> {
        if (!ecs.exists(id)) return setOf()

        return ecs.entities().filter { childId ->
            ecs.get(childId, TypeTag.Parent).into<OrgChartComponent.Parent>()?.id == id
        }.toSet()
    }
}