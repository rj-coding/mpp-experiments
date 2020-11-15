package nl.rjcoding.orgchart.util

import nl.rjcoding.common.Integral
import nl.rjcoding.common.geometry
import kotlin.test.Test
import kotlin.test.assertEquals

class AreaTreeTests {

    @Test
    fun appendToRowTest() {
        val a = AreaTree.Item("A", Integral.geometry.area(1, 1))
        val b = AreaTree.Item("B", Integral.geometry.area(1, 1))
        val c = AreaTree.Item("C", Integral.geometry.area(1, 1))

        val tree = AreaTree.Container<String>()

        tree.add(a, AreaTree.Container.AdditionMode.AppendToRow)
        tree.add(b, AreaTree.Container.AdditionMode.AppendToRow)
        tree.add(c, AreaTree.Container.AdditionMode.AppendToRow)


        assertEquals(Integral.geometry.area(3, 1), tree.area)
        assertEquals(3, tree.children.toList().size)

        tree.children.forEach { (position, tree) ->
            when ((tree as AreaTree.Item).item) {
                "A" -> assertEquals(Integral.geometry.vector2D(0, 0), position)
                "B" -> assertEquals(Integral.geometry.vector2D(1, 0), position)
                "C" -> assertEquals(Integral.geometry.vector2D(2, 0), position)
            }
        }
    }

    @Test
    fun appendToColumnTest() {
        val a = AreaTree.Item("A", Integral.geometry.area(1, 1))
        val b = AreaTree.Item("B", Integral.geometry.area(1, 1))
        val c = AreaTree.Item("C", Integral.geometry.area(1, 1))

        val tree = AreaTree.Container<String>()

        tree.add(a, AreaTree.Container.AdditionMode.NewRow)
        tree.add(b, AreaTree.Container.AdditionMode.NewRow)
        tree.add(c, AreaTree.Container.AdditionMode.NewRow)


        assertEquals(Integral.geometry.area(1, 3), tree.area)
        assertEquals(3, tree.children.toList().size)

        tree.children.forEach { (position, tree) ->
            when ((tree as AreaTree.Item).item) {
                "A" -> assertEquals(Integral.geometry.vector2D(0, 0), position)
                "B" -> assertEquals(Integral.geometry.vector2D(0, 1), position)
                "C" -> assertEquals(Integral.geometry.vector2D(0, 2), position)
            }
        }
    }

    @Test
    fun unevenAppendToRowTest() {
        val a = AreaTree.Item("A", Integral.geometry.area(3, 5))
        val b = AreaTree.Item("B", Integral.geometry.area(6, 1))
        val c = AreaTree.Item("C", Integral.geometry.area(2, 2))

        val tree = AreaTree.Container<String>()

        tree.add(a, AreaTree.Container.AdditionMode.AppendToRow)
        tree.add(b, AreaTree.Container.AdditionMode.AppendToRow)
        tree.add(c, AreaTree.Container.AdditionMode.AppendToRow)


        assertEquals(Integral.geometry.area(11, 5), tree.area)
        assertEquals(3, tree.children.toList().size)

        tree.children.forEach { (position, tree) ->
            when ((tree as AreaTree.Item).item) {
                "A" -> assertEquals(Integral.geometry.vector2D(0, 0), position)
                "B" -> assertEquals(Integral.geometry.vector2D(3, 0), position)
                "C" -> assertEquals(Integral.geometry.vector2D(9, 0), position)
            }
        }
    }

    @Test
    fun unevenNewRowTest() {
        val a = AreaTree.Item("A", Integral.geometry.area(3, 5))
        val b = AreaTree.Item("B", Integral.geometry.area(6, 1))
        val c = AreaTree.Item("C", Integral.geometry.area(2, 2))

        val tree = AreaTree.Container<String>()

        tree.add(a, AreaTree.Container.AdditionMode.NewRow)
        tree.add(b, AreaTree.Container.AdditionMode.NewRow)
        tree.add(c, AreaTree.Container.AdditionMode.NewRow)


        assertEquals(Integral.geometry.area(6, 8), tree.area)
        assertEquals(3, tree.children.toList().size)

        tree.children.forEach { (position, tree) ->
            when ((tree as AreaTree.Item).item) {
                "A" -> assertEquals(Integral.geometry.vector2D(0, 0), position)
                "B" -> assertEquals(Integral.geometry.vector2D(0, 5), position)
                "C" -> assertEquals(Integral.geometry.vector2D(0, 6), position)
            }
        }
    }
}