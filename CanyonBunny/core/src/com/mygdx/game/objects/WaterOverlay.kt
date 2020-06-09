/*
Author : Arda
Company : PhysTech
Date : 4/8/2020
*/

package com.mygdx.game.objects

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.Assets

class WaterOverlay(length : Float) : AbstractGameObject() {

    private var regWaterOverlay = Assets.levelDecoration.waterOverlay

    init {
        dimension.set(length * 10 , 3f)
        origin.x = -dimension.x / 2f
    }

    override fun render(batch: SpriteBatch) {
        var reg = regWaterOverlay

        batch.draw(reg.texture , position.x + origin.x , position.y + origin.y , origin.x , origin.y , dimension.x , dimension.y , scale.x , scale.y ,
        rotation , reg.regionX , reg.regionY , reg.regionWidth , reg.regionHeight , false , false)
    }

}