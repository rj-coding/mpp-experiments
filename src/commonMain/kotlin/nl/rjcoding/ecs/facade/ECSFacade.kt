package nl.rjcoding.ecs.facade

import nl.rjcoding.ecs.ECS

interface ECSFacade<Id, TypeTag> : ECS<Id, TypeTag> {
    val backend: ECS<Id, TypeTag>
}