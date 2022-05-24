package com.gravity.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.gravity.Assets
import com.gravity.ecs.components.Acceleration
import com.gravity.ecs.components.Mass
import com.gravity.ecs.components.Transform
import com.gravity.ecs.components.Velocity
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.graphics.use
import ktx.math.plus
import ktx.math.times

class RenderSystem : IteratingSystem(allOf(Transform::class, Mass::class).get()) {
    private val shapeDrawer by lazy { Assets.shapeDrawer }
    private val transMapper = mapperFor<Transform>()
    private val massMapper = mapperFor<Mass>()
    override fun update(deltaTime: Float) {
        shapeDrawer.batch.use {
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        shapeDrawer.filledCircle(transMapper.get(entity).position, massMapper.get(entity).mass)
    }
}

class SpeedSystem : IteratingSystem(allOf(Acceleration::class, Velocity::class).get()) {
    private val speedMapper = mapperFor<Velocity>()
    private val accelerationMapper = mapperFor<Acceleration>()
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val speed = speedMapper.get(entity)
        val acceleration = accelerationMapper.get(entity)
        speed.v.set(speed.v + acceleration.a * deltaTime)
    }

}