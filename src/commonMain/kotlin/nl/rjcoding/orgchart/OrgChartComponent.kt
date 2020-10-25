package nl.rjcoding.orgchart

import nl.rjcoding.ecs.Component

enum class TypeTag {
    Name,
    Parent,
    Children,
    Assistant,
    Department,
    Function,
    Position,
}

sealed class OrgChartComponent(override val type: TypeTag) : Component<TypeTag> {
    data class Name(val name: String) : OrgChartComponent(TypeTag.Name)
    data class Parent<Id>(val id: Id) : OrgChartComponent(TypeTag.Parent)
    data class Children<Id>(val children: List<Id>) : OrgChartComponent(TypeTag.Children)
    object Assistant : OrgChartComponent(TypeTag.Assistant)
    object Department : OrgChartComponent(TypeTag.Department)
    object Function : OrgChartComponent(TypeTag.Function)
    data class Position(val x: Int, val y: Int): OrgChartComponent(TypeTag.Position)
}