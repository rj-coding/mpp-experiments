package nl.rjcoding.orgchart.util

import nl.rjcoding.common.Integral
import kotlin.math.max

sealed class AreaTree<T> : Integral.HasArea {

    data class Item<T>(val item: T, override val area: Integral.Area): AreaTree<T>()

    class Container<T>: AreaTree<T>() {

        private var cursor = Integral.Vector2D(0, 0)
        private var currentArea : Integral.Area = Integral.Area(0, 0)
        private var subTrees : MutableMap<Integral.Vector2D, AreaTree<T>> = mutableMapOf()

        override val area: Integral.Area get() = currentArea
        val children: Sequence<Pair<Integral.Vector2D, AreaTree<T>>> = subTrees.asSequence().map { it.toPair() }

        enum class AdditionMode {
            AppendToRow,
            NewRow
        }

        fun add(tree: AreaTree<T>, additionMode: AdditionMode, offset: Integral.Vector2D = Integral.Vector2D(0, 0)) {
            when (additionMode) {
                AdditionMode.AppendToRow -> {
                    cursor = cursor.copy(x = currentArea.width + offset.x)
                    currentArea = currentArea.copy(
                        width = currentArea.width + tree.area.width,
                        height = max(currentArea.height, tree.area.height + cursor.y)
                    )
                }

                AdditionMode.NewRow -> {
                    cursor = cursor.copy(x = 0, y = currentArea.height + offset.y)
                    currentArea = currentArea.copy(
                        width = max(currentArea.width, tree.area.width + cursor.x),
                        height = currentArea.height + tree.area.height
                    )
                }
            }
            subTrees[cursor] = tree
        }
    }
}