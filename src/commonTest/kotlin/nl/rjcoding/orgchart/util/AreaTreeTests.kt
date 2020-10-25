package nl.rjcoding.orgchart.util

import nl.rjcoding.common.Integral
import kotlin.test.Test
import kotlin.test.assertEquals

class AreaTreeTests {

    @Test
    fun appendToRowTest() {
        val a = AreaTree.Item("A", Integral.Area(1, 1))
        val b = AreaTree.Item("B", Integral.Area(1, 1))
        val c = AreaTree.Item("C", Integral.Area(1, 1))

        val tree = AreaTree.Container<String>()

        tree.add(a, AreaTree.Container.AdditionMode.AppendToRow)
        tree.add(b, AreaTree.Container.AdditionMode.AppendToRow)
        tree.add(c, AreaTree.Container.AdditionMode.AppendToRow)


        assertEquals(Integral.Area(3, 1), tree.area)
        assertEquals(3, tree.children.toList().size)

        tree.children.forEach { (position, tree) ->
            when ((tree as AreaTree.Item).item) {
                "A" -> assertEquals(Integral.Vector2D(0, 0), position)
                "B" -> assertEquals(Integral.Vector2D(1, 0), position)
                "C" -> assertEquals(Integral.Vector2D(2, 0), position)
            }
        }
    }

    @Test
    fun appendToColumnTest() {
        val a = AreaTree.Item("A", Integral.Area(1, 1))
        val b = AreaTree.Item("B", Integral.Area(1, 1))
        val c = AreaTree.Item("C", Integral.Area(1, 1))

        val tree = AreaTree.Container<String>()

        tree.add(a, AreaTree.Container.AdditionMode.NewRow)
        tree.add(b, AreaTree.Container.AdditionMode.NewRow)
        tree.add(c, AreaTree.Container.AdditionMode.NewRow)


        assertEquals(Integral.Area(1, 3), tree.area)
        assertEquals(3, tree.children.toList().size)

        tree.children.forEach { (position, tree) ->
            when ((tree as AreaTree.Item).item) {
                "A" -> assertEquals(Integral.Vector2D(0, 0), position)
                "B" -> assertEquals(Integral.Vector2D(0, 1), position)
                "C" -> assertEquals(Integral.Vector2D(0, 2), position)
            }
        }
    }

    @Test
    fun unevenAppendToRowTest() {
        val a = AreaTree.Item("A", Integral.Area(3, 5))
        val b = AreaTree.Item("B", Integral.Area(6, 1))
        val c = AreaTree.Item("C", Integral.Area(2, 2))

        val tree = AreaTree.Container<String>()

        tree.add(a, AreaTree.Container.AdditionMode.AppendToRow)
        tree.add(b, AreaTree.Container.AdditionMode.AppendToRow)
        tree.add(c, AreaTree.Container.AdditionMode.AppendToRow)


        assertEquals(Integral.Area(11, 5), tree.area)
        assertEquals(3, tree.children.toList().size)

        tree.children.forEach { (position, tree) ->
            when ((tree as AreaTree.Item).item) {
                "A" -> assertEquals(Integral.Vector2D(0, 0), position)
                "B" -> assertEquals(Integral.Vector2D(3, 0), position)
                "C" -> assertEquals(Integral.Vector2D(9, 0), position)
            }
        }
    }

    @Test
    fun unevenNewRowTest() {
        val a = AreaTree.Item("A", Integral.Area(3, 5))
        val b = AreaTree.Item("B", Integral.Area(6, 1))
        val c = AreaTree.Item("C", Integral.Area(2, 2))

        val tree = AreaTree.Container<String>()

        tree.add(a, AreaTree.Container.AdditionMode.NewRow)
        tree.add(b, AreaTree.Container.AdditionMode.NewRow)
        tree.add(c, AreaTree.Container.AdditionMode.NewRow)


        assertEquals(Integral.Area(6, 8), tree.area)
        assertEquals(3, tree.children.toList().size)

        tree.children.forEach { (position, tree) ->
            when ((tree as AreaTree.Item).item) {
                "A" -> assertEquals(Integral.Vector2D(0, 0), position)
                "B" -> assertEquals(Integral.Vector2D(0, 5), position)
                "C" -> assertEquals(Integral.Vector2D(0, 6), position)
            }
        }
    }
}