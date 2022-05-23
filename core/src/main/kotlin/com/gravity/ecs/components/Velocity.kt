package com.gravity.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.math.vec2

class Velocity : Component, Pool.Poolable {
    val v = vec2()
    override fun reset() {
        v.setZero()
    }
}

