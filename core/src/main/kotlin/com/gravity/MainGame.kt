package com.gravity

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class MainGame : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()

        //30000000f..50000000f
        addScreen(FirstScreen(addBigOnes = false, addSun = false, 0f..0f, 10000f..1000000f))
        setScreen<FirstScreen>()
    }
}

