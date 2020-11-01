package nl.rjcoding.pathfinding

interface PathFinder<Node> {
    fun find(from: Node, to: Node): List<Node>
}