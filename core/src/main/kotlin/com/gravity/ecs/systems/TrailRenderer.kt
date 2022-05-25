package com.gravity.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.gravity.Assets
import com.gravity.ecs.components.Trail
import com.gravity.injection.GameConstants
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.graphics.use
import ktx.math.div

class TrailRenderer : IteratingSystem(allOf(Trail::class).get()) {
    private val trailColor = Color(0f, 0f, 1f, 0.2f)
    val trailMapper = mapperFor<Trail>()
    val shapeDrawer by lazy { Assets.shapeDrawer }

    override fun update(deltaTime: Float) {
        shapeDrawer.batch.use {
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val t = trailMapper.get(entity)
        if (t.points.any()) {
            var p1 = t.points.first()
            for ((index, p2) in t.points.withIndex()) {
                if (index > 0) {
                    shapeDrawer.line(
                        p1 / GameConstants.drawScale,
                        p2 / GameConstants.drawScale,
                        trailColor,
                        5 * GameConstants.drawScale
                    )
                    p1 = p2
                }
            }
        }
    }

}