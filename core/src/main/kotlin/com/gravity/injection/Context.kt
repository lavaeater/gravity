package com.gravity.injection

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.gravity.ecs.systems.GravitySystem
import com.gravity.ecs.systems.RenderSystem
import com.gravity.ecs.systems.SpeedSystem
import com.gravity.injection.GameConstants.GAME_HEIGHT
import com.gravity.injection.GameConstants.GAME_WIDTH
import ktx.inject.Context
import ktx.inject.register


object GameConstants {
    const val GAME_HEIGHT = 100f
    const val GAME_WIDTH = 100f
}

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
            addSystem(SpeedSystem())
            addSystem(RenderSystem())
        }
    }
}