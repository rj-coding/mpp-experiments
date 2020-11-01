package nl.rjcoding.common

class BinaryHeap<Item, Weight>(private val comparator: Comparator<Weight>) : Collection<Item>  {

    constructor(compare: (Weight, Weight) -> Int) : this(Comparator<Weight> { a, b -> compare(a, b)})

    private val containedItems = mutableSetOf<Item>()
    private val arrayItems = ArrayList<Item>()
    private val arrayWeights = ArrayList<Weight>()

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
        val copy = BinaryHeap<Item, Weight>(comparator)
        (0 until size).forEach { i ->
            copy.insert(arrayItems[i], arrayWeights[i])
        }
        return BinaryHeapIterator(copy)
    }

    fun insert(item: Item, weight: Weight) {
        if (arrayItems.size > size) {
            arrayItems[size] = item
            arrayWeights[size] = weight
        } else {
            arrayItems.add(item)
            arrayWeights.add(weight)
        }

        containedItems.add(item)
        size += 1

        var i = size - 1
        while (i != 0 && comparator.compare(arrayWeights[i], arrayWeights[parent(i)]) > 0) {
            swap(i, parent(i), arrayItems)
            swap(i, parent(i), arrayWeights)
            i = parent(i)
        }
    }

    fun peek(): Item? {
        return if (size > 0) arrayItems[0] else null
    }

    fun pop(): Item? {
        if (size <= 0) return null

        if (size == 1) {
            size -= 1
            return arrayItems[0]
        }

        val rootItem = arrayItems[0]
        arrayItems[0] = arrayItems[size - 1]
        arrayWeights[0] = arrayWeights[size - 1]
        size -= 1
        minHeapify(0)
        return rootItem
    }

    private fun parent(key: Int): Int = (key - 1) / 2
    private fun left(key: Int): Int = 2 * key + 1
    private fun right(key: Int): Int = 2 * key + 2

    private fun <T> swap(i: Int, j: Int, array: ArrayList<T>) {
        val k = array[i]
        array[i] = array[j]
        array[j] = k
    }

    private tailrec fun minHeapify(key: Int) {
        val l = left(key)
        val r = right(key)
        var k = key

        if (l < size && comparator.compare(arrayWeights[l], arrayWeights[k]) > 0) {
            k = l
        }

        if (r < size && comparator.compare(arrayWeights[r], arrayWeights[k]) > 0) {
            k = r
        }

        if (k != key) {
            swap(key, k, arrayItems)
            swap(key, k, arrayWeights)
            minHeapify(k)
        }
    }
}

class BinaryHeapIterator<Item, Weight>(private val heap: BinaryHeap<Item, Weight>) : Iterator<Item> {

    override fun hasNext(): Boolean {
        return !heap.isEmpty()
    }

    override fun next(): Item {
        return heap.pop()!!
    }
}