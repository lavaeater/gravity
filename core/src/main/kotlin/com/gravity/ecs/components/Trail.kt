package com.gravity.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Queue

class Trail: Component, Pool.Poolable {
    val maxSize = 150
    var framesCounter = 0
    val frameTarget = 5
    val points = Queue<Vector2>(100)
    override fun reset() {
        framesCounter = 0
        points.clear()
    }
}