package com.gravity

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class MainGame : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()

        //30000000f..50000000f
        addScreen(FirstScreen(addBigOnes = true, addSun = true, 25000000f..35000000f, 10000f..1000000f,500))
        setScreen<FirstScreen>()
    }
}

