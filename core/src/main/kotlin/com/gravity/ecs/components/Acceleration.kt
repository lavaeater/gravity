package com.gravity.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.math.vec2

class Acceleration : Component, Pool.Poolable {
    val a = vec2()
    override fun reset() {
        a.setZero()
    }
}