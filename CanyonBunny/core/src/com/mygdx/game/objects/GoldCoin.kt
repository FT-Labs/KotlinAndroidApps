/*
Author : Arda
Company : PhysTech
Date : 4/10/2020
*/

package com.mygdx.game.objects

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.Assets

class GoldCoin() : AbstractGameObject(){
    private var regGoldCoin = Assets.goldCoin.goldCoin
    var collected : Boolean = false

    init {
        dimension.set(0.5f , 0.5f)
        bounds.set(0f , 0f , dimension.x , dimension.y)
    }

    override fun render(batch: SpriteBatch) {
        if (collected) return

        var reg = regGoldCoin
        batch.draw(reg.texture , position.x , position.y , origin.x , origin.y , dimension.x , dimension.y , scale.x , scale.y ,
        rotation , reg.regionX , reg.regionY , reg.regionWidth , reg.regionHeight , false , false)
    }

    fun getScore() : Int = 100

}