package nl.avwie.declarative

import kotlin.test.Test
import kotlin.test.assertEquals

class NodeTests {

    @Test
    fun rectangle() {
        val tree = Node(Group) {
            Rectangle(200, 300) {
                color = "#c00c0c"
            }

            Rectangle(800, 600) {
                color = "#c00c0c"
            }
        }

        assertEquals(200, tree.nodes[0][Width])
        assertEquals(300, tree.nodes[0][Height])
        assertEquals("#c00c0c", tree.nodes[0][Color])

        assertEquals(800, tree.nodes[1][Width])
        assertEquals(600, tree.nodes[1][Height])
        assertEquals("#c00c0c", tree.nodes[1][Color])
    }
}