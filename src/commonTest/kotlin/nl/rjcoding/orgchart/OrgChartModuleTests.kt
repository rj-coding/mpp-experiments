package nl.rjcoding.orgchart

import nl.rjcoding.common.UUID
import nl.rjcoding.ecs.SimpleECS
import nl.rjcoding.ecs.get
import nl.rjcoding.ecs.hasEquals
import nl.rjcoding.ecs.into
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OrgChartModuleTests {

    @Test
    fun createDepartmentTest() {
        val ecs = SimpleECS<TypeTag>()
        val module = OrgChartModule(ecs)

        val foo = module.createDepartment("Foo")
        val bar = module.createDepartment("Bar", foo, true)
        val baz = module.createDepartment("Baz", foo)

        assertTrue { foo in ecs }
        assertTrue { bar in ecs }
        assertTrue { baz in ecs }

        assertEquals("Foo", ecs[foo].getName())
        assertEquals("Bar", ecs[bar].getName())
        assertEquals("Baz", ecs[baz].getName())

        assertEquals(foo, ecs[bar, TypeTag.Parent].into<OrgChartComponent.Parent<UUID>>()?.id)
        assertEquals(foo, ecs[baz, TypeTag.Parent].into<OrgChartComponent.Parent<UUID>>()?.id)

        assertTrue { ecs.hasEquals(foo, TypeTag.Kind, OrgChartComponent.Department) }
        assertFalse { ecs.hasEquals(foo, TypeTag.Kind, OrgChartComponent.Function) }
        assertTrue { ecs.hasEquals(bar, TypeTag.Kind, OrgChartComponent.Department) }
        assertFalse { ecs.hasEquals(bar, TypeTag.Kind, OrgChartComponent.Function) }
        assertTrue { ecs.hasEquals(baz, TypeTag.Kind, OrgChartComponent.Department) }
        assertFalse { ecs.hasEquals(baz, TypeTag.Kind, OrgChartComponent.Function) }

        assertFalse { ecs.has(foo, TypeTag.Assistant) }
        assertTrue { ecs.has(bar, TypeTag.Assistant) }
        assertFalse { ecs.has(baz, TypeTag.Assistant) }
    }

    @Test
    fun createFunctionTest() {
        val ecs = SimpleECS<TypeTag>()
        val module = OrgChartModule(ecs)

        val foo = module.createDepartment("Foo")
        val bar = module.createFunction("Bar", foo)
        assertTrue { bar != null }

        val baz = module.createFunction("Baz", bar!!)
        assertTrue { baz != null }

        assertTrue { bar in ecs }
        assertTrue { baz!! in ecs }

        assertEquals("Bar", ecs.get(bar, TypeTag.Name).into<OrgChartComponent.Name>()?.name)
        assertEquals("Baz", ecs.get(baz!!, TypeTag.Name).into<OrgChartComponent.Name>()?.name)

        assertEquals(foo, ecs[bar, TypeTag.Parent].into<OrgChartComponent.Parent<UUID>>()?.id)
        assertEquals(bar, ecs[baz, TypeTag.Parent].into<OrgChartComponent.Parent<UUID>>()?.id)

        assertFalse { ecs.hasEquals(bar, TypeTag.Kind, OrgChartComponent.Department) }
        assertTrue { ecs.hasEquals(bar, TypeTag.Kind, OrgChartComponent.Function) }
        assertFalse { ecs.hasEquals(baz, TypeTag.Kind, OrgChartComponent.Department) }
        assertTrue { ecs.hasEquals(baz, TypeTag.Kind, OrgChartComponent.Function) }

        assertFalse { ecs.has(foo, TypeTag.Assistant) }
        assertFalse { ecs.has(bar, TypeTag.Assistant) }
        assertFalse { ecs.has(baz, TypeTag.Assistant) }
    }

    @Test
    fun destroyTest() {
        val ecs = SimpleECS<TypeTag>()
        val module = OrgChartModule(ecs)

        val foo = module.createDepartment("Foo")
        val bar = module.createFunction("Bar", foo)
        val baz = module.createFunction("Baz", bar!!)

        assertTrue { foo in ecs }
        assertTrue { bar in ecs }
        assertTrue { baz!! in ecs }

        module.destroy(foo)
        assertFalse { foo in ecs }
        assertFalse { bar in ecs }
        assertFalse { baz!! in ecs }
    }

}