package com.gravity.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool

class Mass : Component, Pool.Poolable {
    var mass = 1f
    override fun reset() {
        mass = 1f
    }
}