/*
Author : Arda
Company : PhysTech
Date : 4/8/2020
*/

package com.mygdx.game.objects

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.Assets

class Rock(var length : Int = 1) : AbstractGameObject() {
    init {
        dimension.set(1f , 1.5f)
    }

    private val regEdge = Assets.rock.edge
    private val regMiddle = Assets.rock.middle

    fun increaseLength(amount : Int) {
        length += amount
        bounds.set(0f , 0f , dimension.x * length , dimension.y)
    }

    fun updateBounds(length : Int){
        //Update bounding box for collision detection
        bounds.set(0f , 0f , dimension.x * length , dimension.y)
    }

    override fun render(batch: SpriteBatch) {


        var relX = 0f
        var relY = 0f

        //Draw left edge
        var reg = regEdge
        relX -= dimension.x / 4f

        batch.draw(reg.texture , position.x + relX , position.y + relY , origin.x , origin.y , relX , dimension.y / 4f, scale.x , scale.y , rotation , reg.regionX , reg.regionY
                , reg.regionWidth , reg.regionHeight , false , false)

        //Draw middle

        relX = 0f
        reg = regMiddle

        for (i in 0 until length) {
            batch.draw(reg.texture , position.x + relX, position.y + relY, origin.x , origin.y , dimension.x , dimension.y ,
            scale.x , scale.y , rotation , reg.regionX , reg.regionY , reg.regionWidth , reg.regionHeight , false , false)
            relX += dimension.x
        }

        //Draw right edge
        reg = regEdge
        batch.draw(reg.texture , position.x + relX , position.y + relY , origin.x + dimension.x / 8f , origin.y, dimension.x/4f , dimension.y , scale.x , scale.y
                , rotation , reg.regionX, reg.regionY,  reg.regionWidth , reg.regionWidth , true , false)

    }


}