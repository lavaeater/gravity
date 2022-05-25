package com.gravity.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.gravity.injection.GameConstants

class Remove: Component, Pool.Poolable {
    var coolDown = GameConstants.COLLISION_COOLDOWN
    override fun reset() {
        coolDown = GameConstants.COLLISION_COOLDOWN
    }

}