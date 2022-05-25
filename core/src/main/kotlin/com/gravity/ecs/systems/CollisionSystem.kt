package com.gravity.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.gravity.ecs.components.*
import ktx.ashley.*
import ktx.math.div
import ktx.math.plus

class CollisionSystem : IteratingSystem(allOf(Transform::class, Mass::class).get()) {
    private val allFamily = allOf(Transform::class, Mass::class, Velocity::class, Acceleration::class).get()
    private val allEntities get() = engine.getEntitiesFor(allFamily)
    private val transMapper = mapperFor<Transform>()
    private val massMapper = mapperFor<Mass>()
    private val accMapper = mapperFor<Acceleration>()
    private val velMapper = mapperFor<Velocity>()
    private val removeMapper = mapperFor<Remove>()

    override fun processEntity(e1: Entity, deltaTime: Float) {
        if (!removeMapper.has(e1))
            for (e2 in allEntities) {
                if (e1 != e2 && !removeMapper.has(e2)) {
                    val distance = transMapper.get(e1).position.dst(transMapper.get(e2).position)
                    val m1 = massMapper.get(e1)
                    val m2 = massMapper.get(e2)
                    if (distance < m1.radius + m2.radius) {
                        val a1 = accMapper.get(e1)
                        val a2 = accMapper.get(e2)
                        val v1 = velMapper.get(e1)
                        val v2 = velMapper.get(e2)
                        //COLLISSION!
                        val newEntity = engine.entity {
                            with<Mass> {
                                mass = m1.mass + m2.mass
                            }
                            with<Transform> {
                                position.set(transMapper.get(e1).position)
                            }
                            with<Acceleration> {
                                a.set((a1.a + a2.a) / 2f)
                            }
                            with<Velocity> {
                                v.set((v1.v + v2.v) / 2f)
                            }
                            with<Trail>()
                        }
                        e1.remove<Acceleration>()
                        e2.remove<Acceleration>()
                        e1.remove<Velocity>()
                        e2.remove<Velocity>()
                        e1.addComponent<Remove>(engine)
                        e2.addComponent<Remove>(engine)
                    }
                }
            }
    }
}