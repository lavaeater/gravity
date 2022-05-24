package com.gravity.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Pool
import com.gravity.injection.GameConstants.MAX_MASS
import com.gravity.injection.GameConstants.MIN_MASS

class Mass : Component, Pool.Poolable {
    var color = com.badlogic.gdx.graphics.Color.WHITE
    var normValue = 0f
    var mass = 1f
        set(value) {
            normValue = MathUtils.norm(MIN_MASS, MAX_MASS, value)
            color = com.badlogic.gdx.graphics.Color(normValue, normValue / 2f, normValue / 3f, 1f)
            field = value
        }

    override fun reset() {
        mass = 1f
        normValue = 0f
        color = com.badlogic.gdx.graphics.Color.WHITE
    }
}