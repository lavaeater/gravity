package com.gravity.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.gravity.ecs.components.Acceleration
import com.gravity.ecs.components.Velocity
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.math.plus
import ktx.math.times

class SpeedSystem : IteratingSystem(allOf(Acceleration::class, Velocity::class).get()) {
    private val speedMapper = mapperFor<Velocity>()
    private val accelerationMapper = mapperFor<Acceleration>()
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val speed = speedMapper.get(entity)
        val acceleration = accelerationMapper.get(entity)
        speed.v.set(speed.v + acceleration.a * deltaTime)
    }

}