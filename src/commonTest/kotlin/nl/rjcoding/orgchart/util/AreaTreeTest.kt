package nl.rjcoding.orgchart.util

import nl.rjcoding.common.Integral
import kotlin.test.Test
import kotlin.test.assertEquals

class AreaTreeTest {

    @Test
    fun appendToRowTest() {
        val root = AreaTree("Root")
        val childA = AreaTree("A")
        val childB = AreaTree("B")

        root.addSubTree(childA, AreaTree.AdditionMode.AppendToRow)
        root.addSubTree(childB, AreaTree.AdditionMode.AppendToRow)

        assertEquals(Integral.Area(3, 1), root.area)
        assertEquals(2, root.children.toList().size)
        
        root.children.forEach { (position, tree) ->
            when (tree.item) {
                "A" -> assertEquals(Integral.Vector2D(1, 0), position)
                "B" -> assertEquals(Integral.Vector2D(2, 0), position)
            }
        }
    }
}