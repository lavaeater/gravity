package com.gravity.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.gravity.ecs.components.Mass
import com.gravity.ecs.components.Transform
import com.gravity.injection.GameConstants
import ktx.ashley.allOf
import ktx.ashley.getSystem
import ktx.ashley.mapperFor

class RemovePlanetSystem : IteratingSystem(allOf(Mass::class, Transform::class).get()) {
    private val transMapper = mapperFor<Transform>()
    private val massMapper = mapperFor<Mass>()
    private val heavyFamily = allOf(Mass::class).get()
    private val heaviestEntity by lazy { engine.getEntitiesFor(heavyFamily).maxByOrNull { massMapper.get(it).mass }!! }
    private val cs by lazy { engine.getSystem<CameraFollowAnEntitySystem>() }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val thisPosition = transMapper.get(entity).position
        val heaviestPosition = transMapper.get(heaviestEntity).position
        if(thisPosition.dst(heaviestPosition) > GameConstants.MAX_DISTANCE) {
            if(cs.selectedEntity == entity)
                cs.selectedEntityIndex += 1
            engine.removeEntity(entity)
        }
    }
}

