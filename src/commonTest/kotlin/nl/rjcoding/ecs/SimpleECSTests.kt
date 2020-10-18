package nl.rjcoding.ecs

import nl.rjcoding.common.UUID

class SimpleECSTests : ECSTests<UUID>() {
    override fun createECS(): ECS<UUID, TypeTag> {
        return SimpleECS()
    }

    override fun generateId(): UUID {
        return UUID.random()
    }
}