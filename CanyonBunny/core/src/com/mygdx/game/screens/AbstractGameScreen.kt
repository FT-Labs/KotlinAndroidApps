/*
Author : Arda
Company : PhysTech
Date : 4/17/2020
*/

package com.mygdx.game.screens

import com.mygdx.game.Assets
import com.mygdx.game.CanyonBunny
import ktx.app.*

abstract class AbstractGameScreen(val game: KtxGame<KtxScreen>) : KtxScreen {

    override fun resume() {
        Assets
    }

    override fun dispose() {
        Assets.dispose()
    }

}