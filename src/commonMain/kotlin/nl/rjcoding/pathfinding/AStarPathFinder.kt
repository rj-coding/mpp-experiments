package nl.rjcoding.pathfinding
import nl.rjcoding.common.BinaryHeap

class AStarPathFinder<Node, Out>(
    private val nodeImpl: Implementation<Node, Out>
) : PathFinder<Node>
{
    interface Implementation<Node, Out> : Comparator<Out> {
        fun add(l: Out, r: Out): Out
        fun cost(from: Node, to: Node): Out
        fun heuristic(from: Node, to: Node): Out
        fun neighbours(node: Node): List<Node>
    }

    override fun find(from: Node, to: Node): List<Node> {
        val frontier = BinaryHeap<Node, Out>(nodeImpl)
        val closedSet = mutableSetOf<Node>()
        val cameFrom = mutableMapOf<Node, Node>()
        val costs = mutableMapOf<Node, Out>()

        nodeImpl.cost(from, from).also { initialCost ->
            frontier.insert(from, initialCost)
            costs[from] = initialCost
        }


        while (frontier.isNotEmpty()) {
            val currentNode = frontier.pop()!!
            closedSet.add(currentNode)

            if (currentNode == to)
                return backTrack(currentNode, cameFrom)

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
        return listOf()
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