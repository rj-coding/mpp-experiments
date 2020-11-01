package nl.rjcoding.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BinaryHeapTests {

    private val intMaxComparator = Comparator<Int> { a, b ->
        when {
            a > b -> 1
            a < b -> -1
            else -> 0
        }
    }

    private val intMinComparator = Comparator<Int> { a, b ->
        -intMaxComparator.compare(a, b)
    }

    @Test
    fun insertTest() {
        val heap = BinaryHeap<Int, Int>(intMaxComparator)
        assertEquals(0, heap.size)
        assertTrue(heap.isEmpty())

        (0 until 10).shuffled().forEach { i -> heap.insert(i, i) }
        assertEquals(10, heap.size)
    }

    @Test
    fun maxPeekTest() {
        val heap = BinaryHeap<Int, Int>(intMaxComparator)
        assertEquals(0, heap.size)

        (0 until 10).shuffled().forEach { i -> heap.insert(i, i) }
        assertEquals(9, heap.peek())
        assertEquals(9, heap.peek())
    }

    @Test
    fun minPeekTest() {
        val heap = BinaryHeap<Int, Int>(intMinComparator)
        assertEquals(0, heap.size)

        (0 until 10).shuffled().forEach { i -> heap.insert(i, i) }
        assertEquals(0, heap.peek())
        assertEquals(0, heap.peek())
    }

    @Test
    fun maxPopTest() {
        val heap = BinaryHeap<Int, Int>(intMaxComparator)
        assertEquals(0, heap.size)

        (0 until 10).shuffled().forEach { i -> heap.insert(i, i) }
        (0 until 10).forEach { i ->
            assertEquals(9 - i, heap.pop())
        }
        assertEquals(0, heap.size)
        assertTrue(heap.isEmpty())
    }

    @Test
    fun minPopTest() {
        val heap = BinaryHeap<Int, Int>(intMinComparator)
        assertEquals(0, heap.size)

        (0 until 10).shuffled().forEach { i -> heap.insert(i, i) }
        (0 until 10).forEach { i ->
            assertEquals(i, heap.pop())
        }
        assertEquals(0, heap.size)
        assertTrue(heap.isEmpty())
    }

    @Test
    fun doublesTest() {
        val heap = BinaryHeap<Int, Int>(intMaxComparator)
        (0 until 10).shuffled().forEach { i -> heap.insert(i, i) }
        (0 until 10).shuffled().forEach { i -> heap.insert(i, i) }
        assertEquals(20, heap.size)

        (0 until 10).forEach { i ->
            assertEquals(9 - i, heap.pop())
            assertEquals(9 - i, heap.pop())
        }
    }

    @Test
    fun iteratorTest() {
        val heap = BinaryHeap<Int, Int>(intMaxComparator)
        (0 until 10).shuffled().forEach { i -> heap.insert(i, i) }

        heap.forEachIndexed { index, value ->
            assertEquals(9 - index, value)
        }
    }

    @Test
    fun stressTest() {
        val amount: Int = 100000
        val heap = BinaryHeap<Int, Int>(intMinComparator)
        (0 until amount).shuffled().forEach { i -> heap.insert(i, i) }

        (0 until amount).forEach { i ->
            assertEquals(i, heap.pop())
        }
    }
}