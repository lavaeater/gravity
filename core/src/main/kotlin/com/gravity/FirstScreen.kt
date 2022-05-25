package com.gravity

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.gravity.ecs.components.*
import com.gravity.ecs.systems.CameraFollowAnEntitySystem
import com.gravity.injection.Context.inject
import com.gravity.injection.GameConstants
import com.gravity.injection.GameConstants.MAX_MASS
import com.gravity.injection.GameConstants.MIN_MASS
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.ashley.entity
import ktx.ashley.getSystem
import ktx.ashley.with
import ktx.math.random

class FirstScreen : KtxScreen, KtxInputAdapter {
    private val addBigOnes = true
    private val engine by lazy { inject<Engine>() }
    private val camera by lazy { inject<OrthographicCamera>() }
    private val viewPort by lazy { inject<ExtendViewport>() }
    private var cameraZoom = 0f
    private val zoomFactor = 0.05f

    private val drawScaleFactor = 1f
    private var drawScale = 0f

    private val planetScaleFactor = 10f
    private var planetScale = 0f
    private val followSystem by lazy { engine.getSystem<CameraFollowAnEntitySystem>() }


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
            Input.Keys.UP -> {
                drawScale = 1f
                true
            }
            Input.Keys.DOWN -> {
                drawScale = -1f
                true
            }
            Input.Keys.W -> {
                planetScale = 1f
                true
            }
            Input.Keys.S -> {
                planetScale = -1f
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
            Input.Keys.LEFT -> {
                followSystem.selectedEntityIndex -= 1
                true
            }
            Input.Keys.RIGHT -> {
                followSystem.selectedEntityIndex += 1
                true
            }
            Input.Keys.UP -> {
                drawScale = 0f
                true
            }
            Input.Keys.DOWN -> {
                drawScale = 0f
                true
            }
            Input.Keys.W -> {
                planetScale = 0f
                true
            }
            Input.Keys.S -> {
                planetScale = 0f
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
        val distanceRange = 10000f..100000f
        val angleRange = 0f..359f

        val xRange = -150000f..150000f
        val yRange = -100000f..100000f
        val velRange = 25000000f..25000000f
        val massRange = MIN_MASS..MAX_MASS
        for (i in 0..500) {
            val distance = distanceRange.random()
            val p = Vector2.X.cpy().rotateDeg(angleRange.random()).scl(distance)
            val m = massRange.random()
            val uV = Vector2.Zero.cpy().sub(p).nor().rotate90(-1).scl((1f / distance) * velRange.random())

            engine.entity {
                with<Mass> {
                    mass = m
                }
                with<Transform> {
                    position.set(p)
                }
                with<Acceleration>()
                with<Velocity> {
                    v.set(uV)
                }
                with<Trail> {
                    points.forEach { it.set(p) }
                }
            }
        }

        if(addBigOnes) {
            var distance = distanceRange.random()
            var p = Vector2.X.cpy().rotateDeg(angleRange.random()).scl(distance)
            var uV = Vector2.Zero.cpy().sub(p).nor().rotate90(-1).scl((1f / distance) * 25000000f)

            engine.entity {
                with<Mass> {
                    mass = MAX_MASS / 3f
                }
                with<Transform> {
                    position.set(p)
                }
                with<Acceleration>()
                with<Velocity> {
                    v.set(uV)
                }
                with<Trail> {
                    points.forEach { it.set(p) }
                }
            }
            distance = distanceRange.random()
            p = Vector2.X.cpy().rotateDeg(angleRange.random()).scl(distance)
            uV = Vector2.Zero.cpy().sub(p).nor().rotate90(-1).scl((1f / distance) * 25000000f)
            engine.entity {
                with<Mass> {
                    mass = MAX_MASS / 2f
                }
                with<Transform> {
                    position.set(p)
                }
                with<Acceleration>()
                with<Velocity> {
                    v.set(uV)
                }
                with<Trail> {
                    points.forEach { it.set(p) }
                }
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
        }
        camera.position.set(0f, 0f, 0f)
    }

    override fun render(delta: Float) {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        engine.update(delta)
        camera.zoom += zoomFactor * cameraZoom
        GameConstants.drawScale += drawScale * drawScaleFactor
        GameConstants.planetScale += planetScale * planetScaleFactor
    }

    override fun dispose() {
    }
}