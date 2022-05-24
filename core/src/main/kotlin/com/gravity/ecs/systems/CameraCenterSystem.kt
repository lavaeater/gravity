package com.gravity.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.gravity.ecs.components.Mass
import com.gravity.ecs.components.Transform
import com.gravity.injection.Context
import com.gravity.injection.Context.inject
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.math.vec2

class CameraFollowAnEntitySystem: EntitySystem() {
    val allFamily = allOf(Transform::class, Mass::class).get()
    val massMapper = mapperFor<Mass>()
    val allEntities get() = engine.getEntitiesFor(allFamily).sortedByDescending { massMapper.get(it).mass }
    val transMapper = mapperFor<Transform>()
    lateinit var selectedEntity : Entity
    private val camera by lazy { inject<OrthographicCamera>() }
    private val viewPort by lazy { inject<ExtendViewport>() }

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
            val pos = transMapper.get(selectedEntity).position
            camera.position.set(pos.x / 10f, pos.y / 10f, 0f)

            viewPort.update(Gdx.graphics.width, Gdx.graphics.height)
            camera.update(true)
        }
    }
}

class CameraFattieSystem: IteratingSystem(allOf(Mass::class, Transform::class).get()) {
    private val camera by lazy { inject<OrthographicCamera>() }
    private val viewPort by lazy { inject<ExtendViewport>() }
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

class CameraCenterSystem : EntitySystem() {
    private val camera by lazy { inject<OrthographicCamera>() }
    private val viewPort by lazy { inject<ExtendViewport>() }
    private val transMapper = mapperFor<Transform>()
    private val massMapper = mapperFor<Mass>()
    private val transFamily = allOf(Transform::class, Mass::class).get()
    private var minX = 0f
    private var minY = 0f
    private var maxX = 0f
    private var maxY = 0f
    private var x = 0f
    private var y = 0f
    private var totalMass = 0f

    override fun update(deltaTime: Float) {
        x = 0f
        y = 0f
        totalMass = 0f
        minX = 0f
        maxX = 0f
        minY = 0f
        maxY = 0f

        for((position, mass) in engine.getEntitiesFor(transFamily).map { transMapper.get(it).position to massMapper.get(it).mass }) {
            x += position.x * mass
            y += position.y * mass

            if(position.x < minX)
                minX = position.x
            if(position.x > maxX)
                maxX = position.x
            if(position.y < minY)
                minY = position.y
            if(position.y > maxY)
                maxY = position.y
            totalMass += mass
        }
        x /= totalMass
        y /= totalMass
        camera.position.set(x, y, 0f)

        //viewPort.minWorldWidth =(maxX - minX) * 0.5f
//        viewPort.minWorldHeight = (maxY - minY) * 0.5f

        viewPort.update(Gdx.graphics.width, Gdx.graphics.height)
        camera.update(true)
    }

}