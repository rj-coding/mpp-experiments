package nl.rjcoding.ecs

typealias QueryResult<Id, TypeTag> = Sequence<Pair<Id, ComponentContainer<TypeTag>>>

sealed class Query<TypeTag> {
    data class Has<TypeTag>(val typeTag: TypeTag) : Query<TypeTag>()
    data class Not<TypeTag>(val query: Query<TypeTag>) : Query<TypeTag>()
    data class And<TypeTag>(val left: Query<TypeTag>, val right: Query<TypeTag>) : Query<TypeTag>()
    data class Or<TypeTag>(val left: Query<TypeTag>, val right: Query<TypeTag>) : Query<TypeTag>()

    fun <Id> execute(ecs: ECS<Id, TypeTag>): QueryResult<Id, TypeTag> {
        val executor = QueryExecutor(ecs)
        return executor.execute(this)
    }
}

class QueryExecutor<Id, TypeTag>(val ecs: ECS<Id, TypeTag>) {

    fun execute(query: Query<TypeTag>): QueryResult<Id, TypeTag> {
        return ecs.entities()
            .map { id -> id to ecs.getAll(id) }
            .filter { (_, components) -> applies(query, components) }
    }

    private fun applies(query: Query<TypeTag>, components: ComponentContainer<TypeTag>): Boolean {
        return when (query) {
            is Query.Has -> query.typeTag in components
            is Query.Not -> !applies(query.query, components)
            is Query.And -> applies(query.left, components) && applies(query.right, components)
            is Query.Or -> applies(query.left, components) || applies(query.right, components)
        }
    }
}