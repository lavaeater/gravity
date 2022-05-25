package com.gravity

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.gravity.ecs.components.FollowThisEntity
import com.gravity.ecs.components.Mass
import com.gravity.ecs.components.Trail
import com.gravity.ecs.components.Transform
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.ashley.remove

class KeepTrackOfFatties(private val engine: Engine) {
    private val allFamily = allOf(Transform::class, Mass::class).get()
    private val trackedFamily = allOf(FollowThisEntity::class).get()
    private val massMapper = mapperFor<Mass>()
    val allEntities get() = engine.getEntitiesFor(allFamily).sortedByDescending { massMapper.get(it).mass }
    private var currentIndex = 0

    fun addAndCheckIndex(i: Int) {
        currentIndex += i
        if(currentIndex > allEntities.size - 1)
            currentIndex = 0
        if(currentIndex < 0)
            currentIndex = allEntities.lastIndex
    }
    fun next() {
        addAndCheckIndex(1)
        removeCurrents()
        val next = allEntities[currentIndex]
        next.addComponent<FollowThisEntity>(engine)
    }
    fun prev() {
        addAndCheckIndex(-1)
        removeCurrents()
        val prev = allEntities[currentIndex]
        prev.addComponent<FollowThisEntity>(engine)
    }

    fun removeCurrents() {
        for(c in engine.getEntitiesFor(trackedFamily)) {
            c.remove<FollowThisEntity>()
        }
    }

    fun trackTheFattest() {
        removeCurrents()
        allEntities.firstOrNull()?.addComponent<FollowThisEntity>(engine)
        currentIndex = 0
    }
}