package nl.rjcoding.orgchart.util

import nl.rjcoding.common.Integral
import kotlin.math.max

class AreaTree<Item>(
    val item: Item,
    position: Integral.Vector2D = Integral.Vector2D(0, 0),
    area: Integral.Area = Integral.Area(1, 1)
) {
    private var cursor = Integral.Vector2D(0, 0)
    private var itemPosition : Integral.Vector2D = position;
    private var currentArea : Integral.Area = area;
    private var subTrees = mutableMapOf<Integral.Vector2D, AreaTree<Item>>()

    val position : Integral.Vector2D get() = itemPosition
    val area : Integral.Area get() = currentArea
    val children: Sequence<Pair<Integral.Vector2D, AreaTree<Item>>> get() = subTrees.asSequence().map { it.key to it.value  }

    enum class AdditionMode {
        AppendToRow,
        NewRow
    }

    fun addSubTree(tree: AreaTree<Item>, additionMode: AdditionMode) {
        when (additionMode) {
            AdditionMode.AppendToRow -> {
                cursor = cursor.copy(x = currentArea.width)
                currentArea = currentArea.copy(
                    width = currentArea.width + tree.area.width,
                    height = max(currentArea.height, tree.area.height + cursor.y)
                )
                subTrees[cursor] = tree
            }

            AdditionMode.NewRow -> {
                cursor = cursor.copy(x = 0, y = currentArea.height)
                currentArea = currentArea.copy(
                    width = max(currentArea.width, tree.area.width + cursor.x),
                    height = currentArea.height + tree.area.height
                )
            }
        }

        subTrees[cursor] = tree
    }
}