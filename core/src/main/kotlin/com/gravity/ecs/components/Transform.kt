package com.gravity.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.math.vec2

class Transform : Component, Pool.Poolable {
    val position = vec2()
    override fun reset() {
        position.setZero()
    }
}