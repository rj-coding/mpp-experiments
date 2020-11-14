package nl.rjcoding.pathfinding

import nl.rjcoding.common.BinaryHeap

class AStarPathFinder<Node, Cost> (
    private val nodeImpl: Implementation<Node, Cost>
) : StepWisePathFinder<Node> where Node : HasPrevious<Node>, Node : AssertsTarget<Node>
{
    interface Implementation<Node, Cost> : Comparator<Cost> {
        fun add(l: Cost, r: Cost): Cost
        fun cost(from: Node, to: Node): Cost
        fun heuristic(from: Node, to: Node): Cost
        fun neighbours(node: Node): List<Node>
    }

    override fun find(from: Node, to: Node): List<Node> {
        val steps = findStepWise(from, to)
        return steps.last()?.backtrack() ?: listOf()
    }

    override fun findStepWise(from: Node, to: Node): Sequence<Node?> = sequence {
        val frontier = BinaryHeap<Node, Cost>(nodeImpl)
        val closedSet = mutableSetOf<Node>()
        val costs = mutableMapOf<Node, Cost>()

        nodeImpl.cost(from, from).also { initialCost ->
            frontier.insert(from, initialCost)
            costs[from] = initialCost
        }

        var endPoint : Node? = null

        while (frontier.isNotEmpty() && endPoint == null) {
            val currentNode = frontier.pop()!!
            closedSet.add(currentNode)

            yield(currentNode)

            if (currentNode.reachedTarget(to)) {
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
                    }
                }
        }

        if (endPoint == null) {
            yield(null)
        }
    }
}