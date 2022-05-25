package com.gravity.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.gravity.KeepTrackOfFatties
import com.gravity.ecs.components.FollowThisEntity
import com.gravity.ecs.components.Remove
import com.gravity.injection.Context
import ktx.ashley.allOf
import ktx.ashley.mapperFor

class RemoveSystem : IteratingSystem(allOf(Remove::class).get()) {
    private val followMapper = mapperFor<FollowThisEntity>()
    private val fattyTracker by lazy { Context.inject<KeepTrackOfFatties>() }
    private val removeMapper = mapperFor<Remove>()
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val remove = removeMapper.get(entity)
        remove.coolDown -= deltaTime
        if (remove.coolDown < 0f) {
            if (followMapper.has(entity))
                fattyTracker.trackTheFattest()
            engine.removeEntity(entity)
        }
    }

}