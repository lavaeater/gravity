package com.gravity

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.gravity.ecs.components.*
import com.gravity.injection.Context.inject
import com.gravity.injection.GameConstants
import com.gravity.injection.GameConstants.MAX_MASS
import com.gravity.injection.GameConstants.MIN_MASS
import com.gravity.injection.GameConstants.drawScale
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.ashley.entity
import ktx.ashley.with
import ktx.math.random

class FirstScreen(
    private val addBigOnes: Boolean,
    private val addSun: Boolean,
    private val velRange: ClosedFloatingPointRange<Float>,
    private val distanceRange: ClosedFloatingPointRange<Float>,
    private val numberOfBodies: Int
) : KtxScreen, KtxInputAdapter {
    private val engine by lazy { inject<Engine>() }
    private val camera by lazy { inject<OrthographicCamera>() }
    private val viewPort by lazy { inject<ExtendViewport>() }
    private var cameraZoom = 0f
    private val zoomFactor = 0.05f

    private val drawScaleFactor = 1f
    private var drawScaleValue = 0f

    private val planetScaleFactor = 10f
    private var planetScale = 0f
    private val fattyTracky by lazy { inject<KeepTrackOfFatties>() }


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
                drawScaleValue = 1f
                true
            }
            Input.Keys.DOWN -> {
                drawScaleValue = -1f
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
            Input.Keys.F -> {
                fattyTracky.trackTheFattest()
                true
            }
            Input.Keys.Z -> {
                cameraZoom = 0f
                true
            }
            Input.Keys.X -> {
                cameraZoom = 0f
                true
            }
            Input.Keys.LEFT -> {
                fattyTracky.prev()
                true
            }
            Input.Keys.RIGHT -> {
                fattyTracky.next()
                true
            }
            Input.Keys.UP -> {
                drawScaleValue = 0f
                true
            }
            Input.Keys.DOWN -> {
                drawScaleValue = 0f
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
            Input.Keys.SPACE -> togglePause()
            else -> {
                false
            }
        }
    }

    override fun show() {
        Gdx.input.inputProcessor = this
        /*
        add some entities, my man!
         */
        val angleRange = 0f..359f
        var largestDistance = 0f

        val massRange = MIN_MASS..MAX_MASS
        for (i in 0..numberOfBodies) {
            val distance = distanceRange.random()
            if(distance > largestDistance)
                largestDistance = distance
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

        if (addBigOnes) {
            var distance = distanceRange.random()
            if(distance > largestDistance)
                largestDistance = distance
            var p = Vector2.X.cpy().rotateDeg(angleRange.random()).scl(distance)
            var uV = Vector2.Zero.cpy().sub(p).nor().rotate90(-1).scl((1f / distance) * velRange.random())

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
            if(distance > largestDistance)
                largestDistance = distance
            p = Vector2.X.cpy().rotateDeg(angleRange.random()).scl(distance)
            uV = Vector2.Zero.cpy().sub(p).nor().rotate90(-1).scl((1f / distance) * velRange.random())
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
        }
        if (addSun) {
            engine.entity {
                with<Mass> {
                    mass = MAX_MASS * 100000f
                }
                with<Transform> {
                    position.set(0f, 0f)
                }
                with<Acceleration>()
                with<Velocity>()
            }
        }
        inject<KeepTrackOfFatties>().trackTheFattest()
        viewPort.minWorldWidth = largestDistance * 2 / drawScale
        viewPort.minWorldHeight = largestDistance * 2 / drawScale
        togglePause()
    }

    private fun togglePause(): Boolean {
        for (system in engine.systems) {
            system.setProcessing(!system.checkProcessing())
        }
        return true
    }

    override fun render(delta: Float) {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        engine.update(delta)
        camera.zoom += zoomFactor * cameraZoom
        GameConstants.drawScale += drawScaleValue * drawScaleFactor
        GameConstants.planetScale += planetScale * planetScaleFactor
    }

    override fun dispose() {
    }
}