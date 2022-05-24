package com.gravity.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.gravity.ecs.components.Acceleration
import com.gravity.ecs.components.Mass
import com.gravity.ecs.components.Transform
import com.gravity.injection.GameConstants.G
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.math.div
import ktx.math.times
import ktx.math.vec2

class GravitySystem : IteratingSystem(allOf(Mass::class, Transform::class, Acceleration::class).get()) {
    private val entitiesWithMassFamily = allOf(Mass::class, Transform::class, Acceleration::class).get()
    private val transMapper = mapperFor<Transform>()
    private val massMapper = mapperFor<Mass>()
    private val accMapper = mapperFor<Acceleration>()
    private val forceVector = vec2()

    override fun processEntity(entity: Entity, deltaTime: Float) {
        forceVector.setZero()
        val myMass = massMapper.get(entity)
        val myAcceleration = accMapper.get(entity)
        val myTransform = transMapper.get(entity)

        val allButMe = engine.getEntitiesFor(entitiesWithMassFamily) - entity
        for (other in allButMe) {
            val otherMass = massMapper.get(other)
            val otherTransform = transMapper.get(other)
            val distance = myTransform.position.dst(otherTransform.position)
            val unitVector = myTransform.position.cpy().sub(otherTransform.position).nor()
            val thisForce = unitVector * (-G * ((myMass.mass * otherMass.mass) / (distance * distance)))
            forceVector.add(thisForce)
        }
        myAcceleration.a.set(forceVector / myMass.mass)
    }
}