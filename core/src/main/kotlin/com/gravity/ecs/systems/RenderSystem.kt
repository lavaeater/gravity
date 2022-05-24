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
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.graphics.use
import ktx.math.div
import ktx.math.plus
import ktx.math.times

class RenderSystem : IteratingSystem(allOf(Transform::class, Mass::class).get()) {
    private val trailColor = Color(0f, 0f, 1f, 0.2f)
    private val shapeDrawer by lazy { Assets.shapeDrawer }
    private val transMapper = mapperFor<Transform>()
    private val trailMapper = mapperFor<Trail>()
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
        val scaledPosition = position / GameConstants.drawScale
//        if(trailMapper.has(entity)) {
//            val t = trailMapper.get(entity)
//            var index = t.currentPointIndex
//            var prevIndex = 0
//            for (i in 0 until t.points.size) {
//                if(i < t.points.size -1) {
//                    index += i
//                    prevIndex = index + 1
//                    if (index >= t.points.size) {
//                        index = 0
//                    }
//                    if (prevIndex >= t.points.size) {
//                        prevIndex = 0
//                    }
//
//                    shapeDrawer.line(t.points[index] / GameConstants.drawScale, t.points[prevIndex] / GameConstants.drawScale, trailColor, 5 * GameConstants.drawScale)
//                }
//            }
//        }
        shapeDrawer.filledCircle(scaledPosition, MathUtils.norm(MIN_MASS, MAX_MASS, MathUtils.clamp(mass.mass, MIN_MASS, MAX_MASS)) * GameConstants.planetScale)
//        shapeDrawer.line(scaledPosition, scaledPosition + acc.a * 10f, Color.BLUE, 10f)
//        shapeDrawer.line(scaledPosition, scaledPosition + velocity.v * 10f, Color.RED, 10f)
    }
}