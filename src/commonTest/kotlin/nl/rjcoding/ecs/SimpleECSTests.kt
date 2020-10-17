package nl.rjcoding.ecs

import nl.rjcoding.common.UUID
import kotlin.random.Random

class SimpleECSTests : ECSTests<UUID>() {
    override fun createECS(): ECS<UUID, TestComponentType> {
        return SimpleECS()
    }

    override fun generateId(): UUID {
        return UUID.random()
    }
}