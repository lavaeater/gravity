package com.gravity.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils
import com.gravity.Assets
import com.gravity.ecs.components.*
import com.gravity.injection.Context.inject
import com.gravity.injection.GameConstants
import com.gravity.injection.GameConstants.MAX_MASS
import com.gravity.injection.GameConstants.MIN_MASS
import com.gravity.injection.GameConstants.drawScale
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.graphics.use
import ktx.math.div
import ktx.math.plus
import ktx.math.times

class RenderSystem(val drawVectors: Boolean) : IteratingSystem(allOf(Transform::class, Mass::class).get()) {

    private val shapeDrawer by lazy { Assets.shapeDrawer }
    private val transMapper = mapperFor<Transform>()
    private val massMapper = mapperFor<Mass>()
    private val removeMapper = mapperFor<Remove>()
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
        val hasRemove = removeMapper.has(entity)
        shapeDrawer.setColor(if(hasRemove) Color.WHITE else mass.color)
        val scaledPosition = position / drawScale

        shapeDrawer.filledCircle(
            scaledPosition,
            MathUtils.norm(MIN_MASS, MAX_MASS, MathUtils.clamp(mass.mass, MIN_MASS, MAX_MASS)) * GameConstants.planetScale
        )

        if(drawVectors) {
            shapeDrawer.line(scaledPosition, scaledPosition + acc.a * drawScale / 10f, Color.BLUE, 10f * drawScale)
            shapeDrawer.line(scaledPosition, scaledPosition + velocity.v * drawScale / 10f, Color.RED, 10f * drawScale)
        }
    }
}