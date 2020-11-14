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

interface AssertsTarget<Node> {
    fun reachedTarget(target: Node): Boolean
}