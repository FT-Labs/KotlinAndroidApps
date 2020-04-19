/*
Author : Arda
Company : PhysTech
Date : 4/10/2020
*/

package com.mygdx.game.objects

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.Assets

class Feather() : AbstractGameObject() {

    private var regFeather = Assets.feather.feather
    var collected = false

    init {
        dimension.set(0.5f, 0.5f)
        //Set bounding box for collision detection
        bounds.set(0f, 0f, dimension.x, dimension.y)
    }

    override fun render(batch: SpriteBatch) {
        batch.draw(regFeather.texture, position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
                rotation, regFeather.regionX, regFeather.regionY, regFeather.regionWidth, regFeather.regionHeight, false, false)
    }

    fun getScore() : Int = 250

}