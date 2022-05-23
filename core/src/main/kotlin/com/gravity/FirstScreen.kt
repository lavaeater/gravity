package com.gravity

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.gravity.ecs.components.Acceleration
import com.gravity.ecs.components.Mass
import com.gravity.ecs.components.Transform
import com.gravity.ecs.components.Velocity
import com.gravity.injection.Context.inject
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.ashley.entity
import ktx.ashley.with
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.graphics.use
import ktx.math.random

class FirstScreen : KtxScreen {
    private val engine by lazy { inject<Engine>() }

    override fun show() {
        /*
        add some entities, my man!
         */
        val positionRange = -100f..100f
        val massRange = 10f..100f
        for(i in 0..10) {
            engine.entity {
                with<Mass> {
                    mass = massRange.random()
                }
                with<Transform> {
                    position.set(positionRange.random(), positionRange.random())
                }
                with<Acceleration>()
                with<Velocity>()
            }
        }
    }

    override fun render(delta: Float) {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        engine.update(delta)
    }

    override fun dispose() {
    }
}