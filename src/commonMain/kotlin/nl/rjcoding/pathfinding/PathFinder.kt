package nl.rjcoding.pathfinding

interface PathFinder<Node> {
    fun find(from: Node, to: Node): List<Node>
}

interface StepWisePathFinder<Node> : PathFinder<Node> {
    fun findStepWise(from: Node, to: Node): Sequence<Node?>
}

interface HasPrevious<Node> where Node : HasPrevious<Node> {
    val previous: Node?
}

fun <Node> Node.backtrack(): List<Node> where Node : HasPrevious<Node> {
    val path = mutableListOf<Node>()
    var next : Node? = this

    while (next != null) {
        path.add(next)
        next = next.previous
    }
    return path.reversed()
}