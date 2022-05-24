package com.gravity.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.gravity.ecs.components.Mass
import com.gravity.ecs.components.Transform
import com.gravity.injection.Context
import com.gravity.injection.GameConstants
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.math.div

class CameraFollowAnEntitySystem: EntitySystem() {
    val allFamily = allOf(Transform::class, Mass::class).get()
    val massMapper = mapperFor<Mass>()
    val allEntities get() = engine.getEntitiesFor(allFamily).sortedByDescending { massMapper.get(it).mass }
    val transMapper = mapperFor<Transform>()
    lateinit var selectedEntity : Entity
    private val camera by lazy { Context.inject<OrthographicCamera>() }
    private val viewPort by lazy { Context.inject<ExtendViewport>() }

    var selectedEntityIndex = 0
        set(value) {
            var actualValue = value
            if(actualValue > allEntities.size - 1)
                actualValue = 0
            if(actualValue < 0)
                actualValue = allEntities.size - 1

            selectedEntity = allEntities[actualValue]
            field = actualValue
        }

    override fun update(deltaTime: Float) {
        if(!::selectedEntity.isInitialized && allEntities.any()) {
            selectedEntity = allEntities.first()
        }

        if(::selectedEntity.isInitialized) {
            val pos = transMapper.get(selectedEntity).position / GameConstants.drawScale
            camera.position.set(pos.x, pos.y, 0f)

            viewPort.update(Gdx.graphics.width, Gdx.graphics.height)
            camera.update(true)
        }
    }
}