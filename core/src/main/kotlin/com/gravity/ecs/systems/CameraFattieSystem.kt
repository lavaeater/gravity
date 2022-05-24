package com.gravity.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.gravity.ecs.components.Mass
import com.gravity.ecs.components.Transform
import com.gravity.injection.Context
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.math.vec2

class CameraFattieSystem: IteratingSystem(allOf(Mass::class, Transform::class).get()) {
    private val camera by lazy { Context.inject<OrthographicCamera>() }
    private val viewPort by lazy { Context.inject<ExtendViewport>() }
    private val cameraPos = vec2()
    var largestMass = 0f
    private val massMapper = mapperFor<Mass>()
    private val transMapper = mapperFor<Transform>()
    override fun update(deltaTime: Float) {
        largestMass = 0f
        super.update(deltaTime)
        camera.position.set(cameraPos.x, cameraPos.y, 0f)

        viewPort.update(Gdx.graphics.width, Gdx.graphics.height)
        camera.update(true)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val mass = massMapper.get(entity).mass
        if(largestMass < mass) {
            largestMass = mass
            cameraPos.set(transMapper.get(entity).position)
        }
    }
}