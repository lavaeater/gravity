package com.gravity.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.gravity.ecs.components.Acceleration
import com.gravity.ecs.components.Mass
import com.gravity.ecs.components.Transform
import com.gravity.ecs.components.Velocity
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.math.plus
import ktx.math.times



class SpeedSystem: IteratingSystem(allOf(Acceleration::class, Velocity::class).get()) {
    val speedMapper = mapperFor<Velocity>()
    val accelerationMapper = mapperFor<Acceleration>()
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val speed = speedMapper.get(entity)
        val acceleration = accelerationMapper.get(entity)
        speed.v.set(speed.v + acceleration.a * deltaTime)
    }

}

class GravitySystem : IteratingSystem(allOf(Mass::class, Transform::class, Acceleration::class).get()) {
    private val entitiesWithMassFamily = allOf(Mass::class, Transform::class, Acceleration::class).get()
    private val transMapper = mapperFor<Transform>()
    private val massMapper = mapperFor<Mass>()
    private val accMapper = mapperFor<Acceleration>()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val mas1 = massMapper.get(entity)
        val acc1 = accMapper.get(entity)
        val tra1 = transMapper.get(entity)

        val allButMe = engine.getEntitiesFor(entitiesWithMassFamily) - entity
        for(other in allButMe) {
            val mas2 = massMapper.get(other)
            val tra2 = transMapper.get(other)
            val distance = tra1.position.dst(tra2.position)
            val unitVector = tra2.position.cpy().sub(tra1.position).nor()
            val newAcc = unitVector.scl (((mas1.mass * mas2.mass) / distance * distance))
            acc1.a.add(newAcc)
        }

    }
}