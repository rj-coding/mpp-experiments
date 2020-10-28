package nl.rjcoding.orgchart

import nl.rjcoding.ecs.Component

enum class TypeTag {
    Kind,
    Name,
    Parent,
    Children,
    Assistant,
    Position,
}

sealed class OrgChartComponent(override val type: TypeTag) : Component<TypeTag> {
    object Department : OrgChartComponent(TypeTag.Kind)
    object Function : OrgChartComponent(TypeTag.Kind)

    data class Name(val name: String) : OrgChartComponent(TypeTag.Name)
    data class Parent<Id>(val id: Id) : OrgChartComponent(TypeTag.Parent)
    data class Children<Id>(val children: List<Id>) : OrgChartComponent(TypeTag.Children)
    object Assistant : OrgChartComponent(TypeTag.Assistant)
    data class Position(val x: Int, val y: Int): OrgChartComponent(TypeTag.Position)
}