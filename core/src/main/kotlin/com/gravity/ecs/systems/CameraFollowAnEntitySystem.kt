package com.gravity.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.gravity.ecs.components.FollowThisEntity
import com.gravity.ecs.components.Mass
import com.gravity.ecs.components.Trail
import com.gravity.ecs.components.Transform
import com.gravity.injection.Context
import com.gravity.injection.GameConstants
import ktx.ashley.*
import ktx.math.div

class CameraFollowAnEntitySystem : IteratingSystem(allOf(FollowThisEntity::class, Transform::class).get()) {
    private val transMapper = mapperFor<Transform>()
    private val camera by lazy { Context.inject<OrthographicCamera>() }
    private val viewPort by lazy { Context.inject<ExtendViewport>() }
    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (transMapper.has(entity)) {
            val pos = transMapper.get(entity).position / GameConstants.drawScale
            camera.position.set(pos.x, pos.y, 0f)
        }
        viewPort.update(Gdx.graphics.width, Gdx.graphics.height)
        camera.update(true)
    }
}