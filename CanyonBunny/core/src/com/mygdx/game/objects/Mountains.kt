/*
Author : Arda
Company : PhysTech
Date : 4/8/2020
*/

package com.mygdx.game.objects

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.mygdx.game.Assets

class Mountains(var length: Int) : AbstractGameObject() {
    private var regMountainLeft = Assets.levelDecoration.mountainLeft
    private var regMountainRight = Assets.levelDecoration.mountainRight

    init {
        dimension.set(10f, 2f)

        //Shift mountain and extend length
        origin.x = -dimension.x * 2f
        length += dimension.x.toInt() * 2
    }

    private fun drawMountain(batch: SpriteBatch, offsetX: Float, offsetY: Float, tintColor: Float) {
        var reg = regMountainLeft
        batch.setColor(tintColor, tintColor, tintColor, 1f)
        var xRel = dimension.x * offsetX
        var yRel = dimension.y * offsetY


        //Mountains span whole level
        var mountainLength = 0
        mountainLength += MathUtils.ceil(length / (2 * dimension.x))
        mountainLength += MathUtils.ceil(0.5f + offsetX)

        for (i in 0 until mountainLength) {
            //MountainLeft
            reg = regMountainLeft
            batch.draw(reg.texture, origin.x + xRel, position.y + origin.y + yRel,
                    origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.regionX, reg.regionY, reg.regionWidth, reg.regionHeight, false, false)
            xRel += dimension.x


            //Mountain right
            reg = regMountainRight
            batch.draw(reg.texture , origin.x + xRel , position.y + origin.y + yRel,
                    origin.x , origin.y , dimension.x , dimension.y , scale.x , scale.y , rotation , reg.regionX , reg.regionY , reg.regionWidth , reg.regionHeight , false , false)
            xRel += dimension.x

        }
        //Reset color to white
        batch.setColor(1f , 1f , 1f ,1f)
    }

    override fun render(batch: SpriteBatch) {
        //Distant mountains(dark gray)
        drawMountain(batch , 0.5f , 0.5f , 0.5f)
        //Distant mountains(gray)
        drawMountain(batch , 0.25f , 0.25f , 0.7f)
        //Distant mountains(light gray)
        drawMountain(batch , 0f , 0f , 0.9f)
    }


}