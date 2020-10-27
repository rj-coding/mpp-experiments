package nl.rjcoding.ecs.decorator

import nl.rjcoding.ecs.ECS

interface ECSDecorator<Id, TypeTag> : ECS<Id, TypeTag> {
    val backend: ECS<Id, TypeTag>
}