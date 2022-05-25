package com.gravity.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.gravity.ecs.components.Remove
import ktx.ashley.allOf
import ktx.ashley.mapperFor

class RemoveSystem: IteratingSystem(allOf(Remove::class).get()) {
    private val removeMapper = mapperFor<Remove>()
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val remove = removeMapper.get(entity)
        remove.coolDown -= deltaTime
        if(remove.coolDown < 0f)
            engine.removeEntity(entity)
    }

}