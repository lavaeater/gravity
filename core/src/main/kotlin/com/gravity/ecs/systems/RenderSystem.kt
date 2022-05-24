package com.gravity.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils
import com.gravity.Assets
import com.gravity.ecs.components.Acceleration
import com.gravity.ecs.components.Mass
import com.gravity.ecs.components.Transform
import com.gravity.ecs.components.Velocity
import com.gravity.injection.Context.inject
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.graphics.use

class RenderSystem : IteratingSystem(allOf(Transform::class, Mass::class).get()) {
    private val shapeDrawer by lazy { Assets.shapeDrawer }
    private val transMapper = mapperFor<Transform>()
    private val massMapper = mapperFor<Mass>()
    private val speedMapper = mapperFor<Velocity>()
    private val accMapper = mapperFor<Acceleration>()
    private val camera by lazy { inject<OrthographicCamera>() }
    override fun update(deltaTime: Float) {
        shapeDrawer.batch.projectionMatrix = camera.combined
        shapeDrawer.batch.use {
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val mass = massMapper.get(entity)
        val acc = accMapper.get(entity)
        val velocity = speedMapper.get(entity)
        val position = transMapper.get(entity).position
        shapeDrawer.setColor(mass.color)
        shapeDrawer.filledCircle(transMapper.get(entity).position, MathUtils.clamp(mass.mass / 1000f, 100f, 1000f))
        shapeDrawer.line(position, position.cpy().add(acc.a.cpy().scl(10f)), Color.BLUE, 10f)
        shapeDrawer.line(position, position.cpy().add(velocity.v.cpy().scl(10f)), Color.RED, 10f)
    }
}