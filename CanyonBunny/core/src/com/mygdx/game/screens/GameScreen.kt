/*
Author : Arda
Company : PhysTech
Date : 4/18/2020
*/

package com.mygdx.game.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.mygdx.game.CanyonBunny
import com.mygdx.game.WorldController
import com.mygdx.game.WorldRenderer
import ktx.app.KtxGame
import ktx.app.KtxScreen


class GameScreen(game : KtxGame<KtxScreen>) : AbstractGameScreen(game) {

    private lateinit var worldController : WorldController
    private lateinit var worldRenderer : WorldRenderer
    private var paused : Boolean = false


    override fun render(delta: Float) {
        //Don't update when game is paused
        if(!paused){
            //Update game world by the time that has passed since last rendered frame
            worldController.update(delta)
        }
        //Sets the clear screen color to : Cornflower blue
        Gdx.gl.glClearColor(0x64 / 255f , 0x95 / 255f , 0xed/255f , 0xff / 255f)
        //Clears screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        //Render game world to screen
        worldRenderer.render()
    }

    override fun show() {

        Gdx.input.setCatchKey(Input.Keys.BACK , true)
    }

    override fun hide(){
        worldRenderer.dispose()
        Gdx.input.setCatchKey(Input.Keys.BACK , false)
    }

    override fun pause() {
        paused = true
    }

    override fun resume() {
        super.resume()
        //Only called on android
        paused = false
    }

    override fun resize(width: Int, height: Int) {
        worldController = WorldController(game)
        worldRenderer = WorldRenderer(worldController)
        worldRenderer.resize(width , height)
    }


}