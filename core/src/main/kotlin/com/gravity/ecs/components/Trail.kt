package com.gravity.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.math.vec2

class Trail: Component, Pool.Poolable {
    var framesCounter = 0
    val frameTarget = 5
    var currentPointIndex = 0
    val points = Array(1000) {
        vec2()
    }
    override fun reset() {
        framesCounter = 0
        currentPointIndex = 0
        points.forEach { it.setZero() }
    }
}