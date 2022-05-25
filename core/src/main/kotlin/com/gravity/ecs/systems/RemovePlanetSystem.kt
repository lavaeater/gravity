package com.gravity.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.gravity.KeepTrackOfFatties
import com.gravity.ecs.components.FollowThisEntity
import com.gravity.ecs.components.Mass
import com.gravity.ecs.components.Transform
import com.gravity.injection.Context.inject
import com.gravity.injection.GameConstants
import ktx.ashley.allOf
import ktx.ashley.getSystem
import ktx.ashley.mapperFor

class RemovePlanetSystem : IteratingSystem(allOf(Mass::class, Transform::class).get()) {
    private val transMapper = mapperFor<Transform>()
    private val massMapper = mapperFor<Mass>()
    private val heavyFamily = allOf(Mass::class).get()
    private val heaviestEntity get() = engine.getEntitiesFor(heavyFamily).maxByOrNull { massMapper.get(it).mass }
    private val followMapper = mapperFor<FollowThisEntity>()
    private val fattyTracker by lazy { inject<KeepTrackOfFatties>() }
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val thisPosition = transMapper.get(entity).position
        if(heaviestEntity != null) {
            val heaviestPosition = transMapper.get(heaviestEntity).position
            if (thisPosition.dst(heaviestPosition) > GameConstants.MAX_DISTANCE) {
                if (followMapper.has(entity))
                    fattyTracker.trackTheFattest()
                engine.removeEntity(entity)
            }
        }
    }
}

