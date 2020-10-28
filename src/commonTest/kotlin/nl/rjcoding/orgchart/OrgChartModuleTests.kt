package nl.rjcoding.orgchart

import nl.rjcoding.common.UUID
import nl.rjcoding.ecs.SimpleECS
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

        assertTrue { ecs.exists(foo) }
        assertTrue { ecs.exists(bar) }
        assertTrue { ecs.exists(baz) }

        assertEquals("Foo", ecs.get(foo, TypeTag.Name).into<OrgChartComponent.Name>()?.name)
        assertEquals("Bar", ecs.get(bar, TypeTag.Name).into<OrgChartComponent.Name>()?.name)
        assertEquals("Baz", ecs.get(baz, TypeTag.Name).into<OrgChartComponent.Name>()?.name)

        assertEquals(foo, ecs.get(bar, TypeTag.Parent).into<OrgChartComponent.Parent<UUID>>()?.id)
        assertEquals(foo, ecs.get(baz, TypeTag.Parent).into<OrgChartComponent.Parent<UUID>>()?.id)

        assertTrue { ecs.has(foo, TypeTag.Department) }
        assertFalse { ecs.has(foo, TypeTag.Function) }
        assertTrue { ecs.has(bar, TypeTag.Department) }
        assertFalse { ecs.has(bar, TypeTag.Function) }
        assertTrue { ecs.has(baz, TypeTag.Department) }
        assertFalse { ecs.has(baz, TypeTag.Function) }

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

        assertTrue { ecs.exists(bar) }
        assertTrue { ecs.exists(baz!!) }

        assertEquals("Bar", ecs.get(bar, TypeTag.Name).into<OrgChartComponent.Name>()?.name)
        assertEquals("Baz", ecs.get(baz!!, TypeTag.Name).into<OrgChartComponent.Name>()?.name)

        assertEquals(foo, ecs.get(bar, TypeTag.Parent).into<OrgChartComponent.Parent<UUID>>()?.id)
        assertEquals(bar, ecs.get(baz, TypeTag.Parent).into<OrgChartComponent.Parent<UUID>>()?.id)

        assertFalse { ecs.has(bar, TypeTag.Department) }
        assertTrue { ecs.has(bar, TypeTag.Function) }
        assertFalse { ecs.has(baz, TypeTag.Department) }
        assertTrue { ecs.has(baz, TypeTag.Function) }

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

        assertTrue { ecs.exists(foo) }
        assertTrue { ecs.exists(bar) }
        assertTrue { ecs.exists(baz!!) }

        module.destroy(foo)
        assertFalse { ecs.exists(foo) }
        assertFalse { ecs.exists(bar) }
        assertFalse { ecs.exists(baz!!) }
    }

}