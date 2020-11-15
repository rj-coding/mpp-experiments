package nl.rjcoding.orgchart

import nl.rjcoding.common.Integral
import nl.rjcoding.common.Vector2D
import nl.rjcoding.common.geometry
import nl.rjcoding.ecs.ECS
import nl.rjcoding.ecs.Query
import nl.rjcoding.ecs.into
import nl.rjcoding.orgchart.util.AreaTree

class OrgChartPositionSystem<Id>(val ecs: ECS<Id, TypeTag>) {

    val query = Query.Or(
        Query.HasEquals(TypeTag.Kind, OrgChartComponent.Department),
        Query.HasEquals(TypeTag.Kind, OrgChartComponent.Function)
    )

    fun position() {
        val ctx = Context<Id>()

        query.execute(ecs).forEach { (id, components) ->
            components[TypeTag.Parent].into<OrgChartComponent.Parent<Id>>()?.also { parent ->
                ctx.parentRefs[id] = parent.id
            } ?: apply {
                ctx.roots.add(id)
            }

            components[TypeTag.Children].into<OrgChartComponent.Children<Id>>()?.also { children ->
                ctx.childRefs[id] = children.children
            }
        }

        val container = joinTrees(positionEntities(ctx.roots, ctx), AreaTree.Container.AdditionMode.AppendToRow)
        val positions = calculatePositions(container)

        positions.forEach { (id, position) ->
            ecs[id] = position
        }
    }

    private fun positionEntities(entities: List<Id>, ctx: Context<Id>): List<AreaTree<Id>> {
        return entities.map { entity ->
            positionEntity(entity, ctx)
        }
    }

    private fun positionEntity(entity: Id, ctx: Context<Id>): AreaTree<Id> {
        val item = AreaTree.Item(entity, Integral.geometry.area.unit)
        if (!ctx.hasChildren(entity))
            return item

        val children = ctx.childRefs[entity]!!
        val childNodes = joinTrees(positionEntities(children, ctx), AreaTree.Container.AdditionMode.AppendToRow)

        val tree = AreaTree.Container<Id>()
        tree.add(
            item,
            additionMode = AreaTree.Container.AdditionMode.AppendToRow,
            offset = Integral.geometry.vector2D((childNodes.area.width - 1) / 2, 0)
        )
        tree.add(
            childNodes,
            additionMode = AreaTree.Container.AdditionMode.NewRow,
        )
        return tree
    }

    private fun calculatePositions(areaTree: AreaTree<Id>): Map<Id, OrgChartComponent.Position> {

        fun inner(origin: Vector2D<Int>, areaTree: AreaTree<Id>, acc: MutableMap<Id, OrgChartComponent.Position>) {
            return when (areaTree) {
                is AreaTree.Item -> {
                    acc[areaTree.item] = OrgChartComponent.Position(origin.x, origin.y)
                }

                is AreaTree.Container -> {
                    areaTree.children.forEach { (offset, childTree) ->
                        inner(origin + offset, childTree, acc)
                    }
                }
            }
        }

        val acc = mutableMapOf<Id, OrgChartComponent.Position>()
        inner(Integral.geometry.vector2D.zero, areaTree, acc)
        return acc
    }

    private fun joinTrees(trees: List<AreaTree<Id>>, mode: AreaTree.Container.AdditionMode): AreaTree<Id> {
        return trees.fold(AreaTree.Container()) { container, child ->
            container.add(child, mode)
            container
        }
    }

    class Context<Id>(
        val roots: MutableList<Id> = mutableListOf(),
        val parentRefs: MutableMap<Id, Id> = mutableMapOf(),
        val childRefs: MutableMap<Id, List<Id>> = mutableMapOf()
    ) {
        fun hasChildren(id: Id): Boolean = childRefs[id]?.isNotEmpty() ?: false
    }
}