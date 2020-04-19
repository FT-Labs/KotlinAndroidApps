/*
Author : Arda
Company : PhysTech
Date : 4/18/2020
*/

package com.mygdx.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.mygdx.game.CanyonBunny

class MenuScreen(game : CanyonBunny) : AbstractGameScreen(game ) {
    private val TAG = "MenuScreen"


    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f , 0f , 0f , 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        if(Gdx.input.isTouched) game.setScreen<GameScreen>()

    }

}