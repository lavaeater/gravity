package com.gravity.injection

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.gravity.ecs.systems.*
import com.gravity.injection.GameConstants.GAME_HEIGHT
import com.gravity.injection.GameConstants.GAME_WIDTH
import ktx.inject.Context
import ktx.inject.register


object Context {
    val context = Context()

    fun dispose() {
    }

    init {
        buildContext()
    }

    inline fun <reified T> inject(): T {
        return context.inject()
    }

    private fun buildContext() {
        context.register {
            bindSingleton(PolygonSpriteBatch())
            bindSingleton(OrthographicCamera())
            bindSingleton(
                ExtendViewport(
                    GAME_WIDTH,
                    GAME_HEIGHT,
                    inject<OrthographicCamera>() as Camera
                )
            )
            bindSingleton(getEngine())
        }
    }

    private fun getEngine(): Engine {
        return PooledEngine().apply {
            addSystem(GravitySystem())
            addSystem(TrailSystem())
            addSystem(SpeedSystem())
            addSystem(TransformSystem())
            addSystem(RenderSystem())
            addSystem(CameraFollowAnEntitySystem())
            //addSystem(PlanetKiller())
        }
    }
}