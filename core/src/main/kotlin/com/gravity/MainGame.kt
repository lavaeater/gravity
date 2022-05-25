package com.gravity

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class MainGame : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()

        addScreen(FirstScreen(true))
        setScreen<FirstScreen>()
    }
}

