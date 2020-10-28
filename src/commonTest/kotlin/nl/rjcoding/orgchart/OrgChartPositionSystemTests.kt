package nl.rjcoding.orgchart

import nl.rjcoding.ecs.SimpleECS
import kotlin.test.Test
import kotlin.test.assertEquals

class OrgChartPositionSystemTests {

    @Test
    fun singleDepartmentTest() {
        val ecs = SimpleECS<TypeTag>()
        val module = OrgChartModule(ecs)
        val department = module.createDepartment("Foo")

        val positionSystem = OrgChartPositionSystem(ecs)
        positionSystem.position()

        assertEquals(OrgChartComponent.Position(0, 0), ecs.get(department, TypeTag.Position))
    }

    @Test
    fun doubleDepartmentTest() {
        val ecs = SimpleECS<TypeTag>()
        val module = OrgChartModule(ecs)
        val departmentA = module.createDepartment("A")
        val departmentB = module.createDepartment("A")

        val positionSystem = OrgChartPositionSystem(ecs)
        positionSystem.position()

        assertEquals(OrgChartComponent.Position(0, 0), ecs.get(departmentA, TypeTag.Position))
        assertEquals(OrgChartComponent.Position(1, 0), ecs.get(departmentB, TypeTag.Position))
    }

    @Test
    fun singleDepartmentWithChildrenTest() {
        val ecs = SimpleECS<TypeTag>()
        val module = OrgChartModule(ecs)
        val department = module.createDepartment("Foo")
        val functionA = module.createFunction("A", department)!!
        val functionB = module.createFunction("B", department)!!

        val positionSystem = OrgChartPositionSystem(ecs)
        positionSystem.position()

        assertEquals(OrgChartComponent.Position(0, 0), ecs.get(department, TypeTag.Position))
        assertEquals(OrgChartComponent.Position(0, 1), ecs.get(functionA, TypeTag.Position))
        assertEquals(OrgChartComponent.Position(1, 1), ecs.get(functionB, TypeTag.Position))
    }

    @Test
    fun doubleDepartmentWithChildrenTest() {
        val ecs = SimpleECS<TypeTag>()
        val module = OrgChartModule(ecs)
        val departmentA = module.createDepartment("Foo")
        val functionAA = module.createFunction("AA", departmentA)!!
        val functionAB = module.createFunction("AB", departmentA)!!

        val departmentB = module.createDepartment("Foo")
        val functionBA = module.createFunction("BA", departmentB)!!
        val functionBB = module.createFunction("BB", departmentB)!!

        val positionSystem = OrgChartPositionSystem(ecs)
        positionSystem.position()

        assertEquals(OrgChartComponent.Position(0, 0), ecs.get(departmentA, TypeTag.Position))
        assertEquals(OrgChartComponent.Position(0, 1), ecs.get(functionAA, TypeTag.Position))
        assertEquals(OrgChartComponent.Position(1, 1), ecs.get(functionAB, TypeTag.Position))

        assertEquals(OrgChartComponent.Position(2, 0), ecs.get(departmentB, TypeTag.Position))
        assertEquals(OrgChartComponent.Position(2, 1), ecs.get(functionBA, TypeTag.Position))
        assertEquals(OrgChartComponent.Position(3, 1), ecs.get(functionBB, TypeTag.Position))
    }

}