package nl.rjcoding.ecs.decorator

import nl.rjcoding.ecs.ECS

abstract class AbstractECSDecorator<Id, TypeTag>(override val backend: ECS<Id, TypeTag>) : ECSDecorator<Id, TypeTag>, ECS<Id, TypeTag> by backend