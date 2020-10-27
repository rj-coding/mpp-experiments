package nl.rjcoding.ecs.facade

import nl.rjcoding.ecs.ECS

abstract class AbstractECSFacade<Id, TypeTag>(override val backend: ECS<Id, TypeTag>) : ECSFacade<Id, TypeTag>, ECS<Id, TypeTag> by backend