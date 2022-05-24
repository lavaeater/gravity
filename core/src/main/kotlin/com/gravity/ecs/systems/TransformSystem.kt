package com.gravity.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.gravity.ecs.components.Transform
import com.gravity.ecs.components.Velocity
import ktx.ashley.allOf
import ktx.ashley.mapperFor

class TransformSystem : IteratingSystem(allOf(Transform::class, Velocity::class).get()) {
    private val speedMapper = mapperFor<Velocity>()
    private val transMapper = mapperFor<Transform>()
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val speed = speedMapper.get(entity)
        val transform = transMapper.get(entity)
        transform.position.add(speed.v)
    }
}