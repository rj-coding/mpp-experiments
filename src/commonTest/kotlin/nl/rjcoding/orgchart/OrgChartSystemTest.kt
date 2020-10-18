package nl.rjcoding.orgchart

import nl.rjcoding.ecs.SimpleECS
import nl.rjcoding.ecs.into
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OrgChartSystemTest {

    @Test
    fun createDepartmentTest() {
        val ecs = SimpleECS<OrgChartSystem.TypeTag>()
        val system = OrgChartSystem(ecs)

        val foo = system.createDepartment("Foo")
        val bar = system.createDepartment("Bar", foo, true)
        val baz = system.createDepartment("Baz", foo)

        assertTrue { ecs.exists(foo) }
        assertTrue { ecs.exists(bar) }
        assertTrue { ecs.exists(baz) }

        assertEquals("Foo", ecs.get(foo, OrgChartSystem.TypeTag.Name).into<OrgChartSystem.OrgChartComponent.Name>()?.name)
        assertEquals("Bar", ecs.get(bar, OrgChartSystem.TypeTag.Name).into<OrgChartSystem.OrgChartComponent.Name>()?.name)
        assertEquals("Baz", ecs.get(baz, OrgChartSystem.TypeTag.Name).into<OrgChartSystem.OrgChartComponent.Name>()?.name)

        assertEquals(foo, ecs.get(bar, OrgChartSystem.TypeTag.Parent).into<OrgChartSystem.OrgChartComponent.Parent>()?.id)
        assertEquals(foo, ecs.get(baz, OrgChartSystem.TypeTag.Parent).into<OrgChartSystem.OrgChartComponent.Parent>()?.id)

        assertTrue { ecs.has(foo, OrgChartSystem.TypeTag.Department) }
        assertFalse { ecs.has(foo, OrgChartSystem.TypeTag.Function) }
        assertTrue { ecs.has(bar, OrgChartSystem.TypeTag.Department) }
        assertFalse { ecs.has(bar, OrgChartSystem.TypeTag.Function) }
        assertTrue { ecs.has(baz, OrgChartSystem.TypeTag.Department) }
        assertFalse { ecs.has(baz, OrgChartSystem.TypeTag.Function) }

        assertFalse { ecs.has(foo, OrgChartSystem.TypeTag.Assistant) }
        assertTrue { ecs.has(bar, OrgChartSystem.TypeTag.Assistant) }
        assertFalse { ecs.has(baz, OrgChartSystem.TypeTag.Assistant) }
    }

    @Test
    fun createFunctionTest() {
        val ecs = SimpleECS<OrgChartSystem.TypeTag>()
        val system = OrgChartSystem(ecs)

        val foo = system.createDepartment("Foo")
        val bar = system.createFunction("Bar", foo)
        assertTrue { bar != null }

        val baz = system.createFunction("Baz", bar!!)
        assertTrue { baz != null }

        assertTrue { ecs.exists(bar) }
        assertTrue { ecs.exists(baz!!) }

        assertEquals("Bar", ecs.get(bar, OrgChartSystem.TypeTag.Name).into<OrgChartSystem.OrgChartComponent.Name>()?.name)
        assertEquals("Baz", ecs.get(baz!!, OrgChartSystem.TypeTag.Name).into<OrgChartSystem.OrgChartComponent.Name>()?.name)

        assertEquals(foo, ecs.get(bar, OrgChartSystem.TypeTag.Parent).into<OrgChartSystem.OrgChartComponent.Parent>()?.id)
        assertEquals(bar, ecs.get(baz, OrgChartSystem.TypeTag.Parent).into<OrgChartSystem.OrgChartComponent.Parent>()?.id)

        assertFalse { ecs.has(bar, OrgChartSystem.TypeTag.Department) }
        assertTrue { ecs.has(bar, OrgChartSystem.TypeTag.Function) }
        assertFalse { ecs.has(baz, OrgChartSystem.TypeTag.Department) }
        assertTrue { ecs.has(baz, OrgChartSystem.TypeTag.Function) }

        assertFalse { ecs.has(foo, OrgChartSystem.TypeTag.Assistant) }
        assertFalse { ecs.has(bar, OrgChartSystem.TypeTag.Assistant) }
        assertFalse { ecs.has(baz, OrgChartSystem.TypeTag.Assistant) }
    }

    @Test
    fun destroyTest() {
        val ecs = SimpleECS<OrgChartSystem.TypeTag>()
        val system = OrgChartSystem(ecs)

        val foo = system.createDepartment("Foo")
        val bar = system.createFunction("Bar", foo)
        val baz = system.createFunction("Baz", bar!!)

        assertTrue { ecs.exists(foo) }
        assertTrue { ecs.exists(bar) }
        assertTrue { ecs.exists(baz!!) }

        system.destroy(foo)
        assertFalse { ecs.exists(foo) }
        assertFalse { ecs.exists(bar) }
        assertFalse { ecs.exists(baz!!) }
    }

}