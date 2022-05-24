package com.gravity

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.gravity.ecs.components.Acceleration
import com.gravity.ecs.components.Mass
import com.gravity.ecs.components.Transform
import com.gravity.ecs.components.Velocity
import com.gravity.injection.Context.inject
import com.gravity.injection.GameConstants.MAX_MASS
import com.gravity.injection.GameConstants.MIN_MASS
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.ashley.entity
import ktx.ashley.with
import ktx.math.random

class FirstScreen : KtxScreen, KtxInputAdapter {
    private val engine by lazy { inject<Engine>() }
    private val camera by lazy { inject<OrthographicCamera>() }
    private val viewPort by lazy { inject<ExtendViewport>() }
    private var cameraZoom = 0f
    private val zoomFactor = 0.05f


    override fun keyDown(keycode: Int): Boolean {
        return when (keycode) {
            Input.Keys.Z -> {
                cameraZoom = 1f
                true
            }
            Input.Keys.X -> {
                cameraZoom = -1f
                true
            }
            else -> {
                false
            }
        }
    }

    override fun keyUp(keycode: Int): Boolean {
        return when (keycode) {
            Input.Keys.Z -> {
                cameraZoom = 0f
                true
            }
            Input.Keys.X -> {
                cameraZoom = 0f
                true
            }
            else -> {
                false
            }
        }
    }

    override fun show() {
        Gdx.input.inputProcessor = this
        viewPort.minWorldWidth = 60000f
        viewPort.minWorldHeight = 40000f
        /*
        add some entities, my man!
         */
        val xRange = -150000f..150000f
        val yRange = -100000f..100000f
        val velRange = -100f..100f
        val massRange = MIN_MASS..MAX_MASS
        for (i in 0..2000) {
            engine.entity {
                with<Mass> {
                    mass = massRange.random()
                }
                with<Transform> {
                    position.set(xRange.random(), yRange.random())
                }
                with<Acceleration>()
                with<Velocity> {
                    v.set(velRange.random(), velRange.random())
                }
            }
        }
        engine.entity {
            with<Mass> {
                mass = MAX_MASS / 3f
            }
            with<Transform> {
                position.set(-10000f, -10000f)
            }
            with<Acceleration>()
            with<Velocity>()
        }
        engine.entity {
            with<Mass> {
                mass = MAX_MASS / 2f
            }
            with<Transform> {
                position.set(10000f, 10000f)
            }
            with<Acceleration>()
            with<Velocity>()
        }
        engine.entity {
            with<Mass> {
                mass = MAX_MASS * 10000f
            }
            with<Transform> {
                position.set(0f, 0f)
            }
            with<Acceleration>()
            with<Velocity>()
        }
        camera.position.set(0f, 0f, 0f)
    }

    override fun render(delta: Float) {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        engine.update(delta)
        camera.zoom += zoomFactor * cameraZoom
    }

    override fun dispose() {
    }
}