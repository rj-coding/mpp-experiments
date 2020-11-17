package nl.rjcoding.pathfinding.sensus

import nl.rjcoding.common.Fraction
import nl.rjcoding.common.Vector2D
import nl.rjcoding.pathfinding.HasPrevious

sealed class Step(val position: Vector2D<Fraction>, override val previous: Step?) : HasPrevious<Step>

data class Start<Item>(val terminator: Cell.Terminator<Item>) : Step(terminator.position, null)

data class OutBound(val hub: Cell.Hub, val port: Cell.Hub.Port, override val previous: Step) : Step(hub.position, previous)

data class InBound(val hub: Cell.Hub, val port: Cell.Hub.Port, override val previous: Step) : Step(hub.position, previous)

data class End<Item>(val terminator: Cell.Terminator<Item>, override val previous: Step) : Step(terminator.position, previous)
