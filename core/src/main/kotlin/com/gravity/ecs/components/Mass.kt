package com.gravity.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Pool

class Mass : Component, Pool.Poolable {
    var color = 1f
    var mass = 1f
        set(value) {
            color = MathUtils.norm(0f, 1f, value)
            field = value
        }

    override fun reset() {
        mass = 1f
    }
}