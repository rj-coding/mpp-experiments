package nl.rjcoding.pathfinding.sensus

import nl.rjcoding.pathfinding.HasPrevious

sealed class Step<Item>(override val previous: Step<Item>?) : HasPrevious<Step<Item>>

abstract class Terminator<Item>(val terminator: Cell<Item>, previous: Step<Item>?) : Step<Item>(previous) {
    override fun hashCode(): Int {
        return terminator.position.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return (other as? Terminator<*>)?.let { otherStep ->
            terminator.position == otherStep.terminator.position
        } ?: false
    }
}

class Start<Item>(terminator: Cell<Item>) : Terminator<Item>(terminator, null)

class End<Item>(terminator: Cell<Item>, previous: Step<Item>) : Terminator<Item>(terminator, previous)

class Target<Item>(terminator: Cell<Item>) : Terminator<Item>(terminator, null)

class Connection<Item>(val from: Cell<Item>, val to: Cell<Item>, val index: Int, previous: Step<Item>) : Step<Item>(previous) {

    override fun hashCode(): Int {
        return (from.position to to.position).hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return (other as? Connection<*>)?.let { otherConnection ->
            from.position == otherConnection.from.position
                    && to.position == otherConnection.to.position
                    && index == otherConnection.index
        } ?: false
    }
}
