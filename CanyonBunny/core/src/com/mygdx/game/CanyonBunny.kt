package com.mygdx.game

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.screens.GameScreen
import com.mygdx.game.screens.MenuScreen
import ktx.app.KtxApplicationAdapter
import ktx.app.KtxGame
import ktx.app.KtxScreen
import kotlin.properties.Delegates


class CanyonBunny() : KtxGame<KtxScreen>() {

    private val TAG = "CanyonBunnyMain"
//    lateinit var worldController : WorldController
//    private lateinit var worldRenderer : WorldRenderer
//    private var paused by Delegates.notNull<Boolean>()



    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        //Load assets
        Assets

//        worldController = WorldController(this)
//        worldRenderer = WorldRenderer(worldController)
//        paused = false

        addScreen(MenuScreen(this))
        addScreen(GameScreen(this))
        setScreen<MenuScreen>()


    }

//    override fun render() {
//
//
//        if(!paused){
//            worldController.update(Gdx.graphics.deltaTime)
//
//        }
//        Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f)
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
//
//        worldRenderer.render()
//    }

//    override fun resize(width: Int, height: Int) {
//        worldRenderer.resize(width , height)
//    }

    override fun dispose() {
//        worldRenderer.dispose()
        Assets.dispose()
    }

//    override fun pause()  {
//        paused = true
//    }
//
//    override fun resume() {
//        Assets
//        paused = false
//    }


}