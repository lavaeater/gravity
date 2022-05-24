package com.gravity.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.gravity.ecs.components.Trail
import com.gravity.ecs.components.Transform
import ktx.ashley.allOf
import ktx.ashley.mapperFor

class PointSystem: IteratingSystem(allOf(Transform::class, Trail::class).get()) {
    private val trailMapper = mapperFor<Trail>()
    private val transMapper = mapperFor<Transform>()
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val t = trailMapper.get(entity)
        t.framesCounter++
        if(t.framesCounter > t.frameTarget) {
            t.framesCounter = 0
            val p = transMapper.get(entity).position
            t.currentPointIndex -= 1
            if (t.currentPointIndex < 0)
                t.currentPointIndex = t.points.size - 1
            t.points[t.currentPointIndex].set(p)
        }
    }
}