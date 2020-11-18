package nl.rjcoding.pathfinding.sensus

import nl.rjcoding.pathfinding.HasPrevious

sealed class Step(override val previous: Step?) : HasPrevious<Step>

abstract class Terminator<Item>(val terminator: Cell.Terminator<Item>, previous: Step?) : Step(previous) {
    override fun hashCode(): Int {
        return terminator.position.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return (other as? Terminator<*>)?.let { otherStep ->
            terminator.position == otherStep.terminator.position
        } ?: false
    }
}

class Start<Item>(terminator: Cell.Terminator<Item>) : Terminator<Item>(terminator, null)
class End<Item>(terminator: Cell.Terminator<Item>, previous: Step) : Terminator<Item>(terminator, previous)

class Connection(val from: Cell.Hub, val to: Cell.Hub, val index: Int, previous: Step) : Step(previous) {

    override fun hashCode(): Int {
        return (from.position to to.position).hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return (other as? Connection)?.let { otherConnection ->
            from.position == otherConnection.from.position
                    && to.position == otherConnection.to.position
                    && index == otherConnection.index
        } ?: false
    }
}
