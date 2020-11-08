package nl.rjcoding.pathfinding
import nl.rjcoding.common.BinaryHeap

class AStarPathFinder<Node, Cost>(
    private val nodeImpl: Implementation<Node, Cost>
) : PathFinder<Node>
{
    interface Implementation<Node, Cost> : Comparator<Cost> {
        fun add(l: Cost, r: Cost): Cost
        fun cost(from: Node, to: Node): Cost
        fun heuristic(from: Node, to: Node): Cost
        fun neighbours(node: Node): List<Node>
    }

    override fun find(from: Node, to: Node): List<Node> {
        val frontier = BinaryHeap<Node, Cost>(nodeImpl)
        val closedSet = mutableSetOf<Node>()
        val cameFrom = mutableMapOf<Node, Node>()
        val costs = mutableMapOf<Node, Cost>()

        nodeImpl.cost(from, from).also { initialCost ->
            frontier.insert(from, initialCost)
            costs[from] = initialCost
        }

        var endPoint : Node? = null
        while (frontier.isNotEmpty() && endPoint == null) {
            val currentNode = frontier.pop()!!
            closedSet.add(currentNode)

            if (currentNode == to) {
                endPoint = currentNode
                continue
            }

            nodeImpl.neighbours(currentNode)
                .filter { node -> !closedSet.contains(node) }
                .forEach { node ->
                    val newCost = nodeImpl.add(costs[currentNode]!!, nodeImpl.cost(currentNode, node))
                    if (!costs.contains(node) || nodeImpl.compare(newCost, costs[node]!!) < 0) {
                        costs[node] = newCost
                        val priority = nodeImpl.add(newCost, nodeImpl.heuristic(node, to))
                        frontier.insert(node, priority)
                        cameFrom[node] = currentNode
                    }
                }
        }

        if (endPoint != null) {
            return backTrack(endPoint, cameFrom)
        } else {
            return listOf()
        }
    }

    private fun backTrack(end: Node, cameFrom: Map<Node, Node>): List<Node> {
        val path = mutableListOf<Node>()
        var next : Node? = end

        while (next != null) {
            path.add(next)
            next = cameFrom[next]
        }
        return path.reversed()
    }
}