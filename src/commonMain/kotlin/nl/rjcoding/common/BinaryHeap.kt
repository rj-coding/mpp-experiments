package nl.rjcoding.common

class BinaryHeap<Item>(private val comparator: Comparator<Item>) : Collection<Item>  {

    private val containedItems = mutableSetOf<Item>()
    private val array = ArrayList<Item>()

    override var size: Int = 0
        private set

    override operator fun contains(element: Item): Boolean {
        return containedItems.contains(element)
    }

    override fun containsAll(elements: Collection<Item>): Boolean {
        return containedItems.containsAll(elements)
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun iterator(): Iterator<Item> {
        val copy = BinaryHeap(comparator)
        (0 until size).forEach { i ->
            copy.insert(array[i])
        }
        return BinaryHeapIterator(copy)
    }

    fun insert(item: Item) {
        if (array.size > size) {
            array[size] = item
        } else {
            array.add(item)
        }

        containedItems.add(item)
        size += 1

        var i = size - 1
        while (i != 0 && comparator.compare(array[i], array[parent(i)]) > 0) {
            swap(i, parent(i))
            i = parent(i)
        }
    }

    fun peek(): Item? {
        return if (size > 0) array[0] else null
    }

    fun pop(): Item? {
        if (size <= 0) return null

        if (size == 1) {
            size -= 1
            return array[0]
        }

        val root = array[0]
        array[0] = array[size - 1]
        size -= 1
        minHeapify(0)
        return root
    }

    private fun parent(key: Int): Int = (key - 1) / 2
    private fun left(key: Int): Int = 2 * key + 1
    private fun right(key: Int): Int = 2 * key + 2

    private fun swap(i: Int, j: Int) {
        val k = array[i]
        array[i] = array[j]
        array[j] = k
    }

    private fun minHeapify(key: Int) {
        val l = left(key)
        val r = right(key)
        var k = key

        if (l < size && comparator.compare(array[l], array[k]) > 0) {
            k = l
        }

        if (r < size && comparator.compare(array[r], array[k]) > 0) {
            k = r
        }

        if (k != key) {
            swap(key, k)
            minHeapify(k)
        }
    }
}

class BinaryHeapIterator<Item>(private val heap: BinaryHeap<Item>) : Iterator<Item> {

    override fun hasNext(): Boolean {
        return !heap.isEmpty()
    }

    override fun next(): Item {
        return heap.pop()!!
    }
}