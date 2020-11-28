package nl.rjcoding.orgchart

import nl.rjcoding.ecs.EntityScope
import nl.rjcoding.ecs.get
import nl.rjcoding.ecs.hasEquals
import nl.rjcoding.ecs.into

fun <Id> EntityScope<Id, TypeTag>.setName(name: String) = set(OrgChartComponent.Name(name))
fun <Id> EntityScope<Id, TypeTag>.getName(): String? = get(TypeTag.Name).into<OrgChartComponent.Name>()?.name

fun <Id> EntityScope<Id, TypeTag>.setParent(parent: Id) {
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


fun <Id> EntityScope<Id, TypeTag>.getParent(): Id? = get(TypeTag.Parent).into<OrgChartComponent.Parent<Id>>()?.id

fun <Id> EntityScope<Id, TypeTag>.getParent(block: EntityScope<Id, TypeTag>.() -> Unit) = getParent()?.also {
    block(EntityScope(it, ecs))
}

fun <Id> EntityScope<Id, TypeTag>.setIsAssistant(isAssistant: Boolean) {
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

fun <Id> EntityScope<Id, TypeTag>.getIsAssistant(): Boolean = get(TypeTag.Assistant).into<OrgChartComponent.Assistant>() != null