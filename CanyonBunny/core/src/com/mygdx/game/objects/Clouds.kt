/*
Author : Arda
Company : PhysTech
Date : 4/9/2020
*/

package com.mygdx.game.objects

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.Assets
import kotlin.random.Random
import kotlin.random.nextInt

class Clouds(var length: Float) : AbstractGameObject() {

    private class Cloud(var regCloud: TextureRegion) : AbstractGameObject() {

        override fun render(batch: SpriteBatch) {
            var reg = regCloud
            batch.draw(reg.texture, position.x + origin.x, position.y + origin.y, origin.x, origin.y,
                    dimension.x, dimension.y, scale.x, scale.y, rotation, reg.regionX, reg.regionY, reg.regionWidth, reg.regionHeight, false, false)

        }
    }


    private var regClouds: ArrayList<TextureRegion> = arrayListOf()
    private var clouds : ArrayList<Clouds> = arrayListOf()
    private val distFac = 5
    private val numClouds = length / distFac

    init {
        dimension.set(3.0f, 1.5f)
        regClouds.add(Assets.levelDecoration.cloud01)
        regClouds.add(Assets.levelDecoration.cloud02)
        regClouds.add(Assets.levelDecoration.cloud03)

    }
   private  fun spawnCloud() : Cloud{
       val cloud = Cloud(regClouds.random())
       cloud.dimension.set(dimension)
       //Position
       val pos = Vector2()
       pos.x = length + 10f     //Position after end of level
       pos.y += 1.75f   //Base position
       pos.y += MathUtils.random(0f , 0.2f) * randBool()        //Random additional pos
       cloud.position.set(pos)
       return cloud
   }

    private fun randBool() : Int{
        val rand = Random.nextInt(IntRange(-10 , 10))
        return if(rand > 0) 1 else 0
    }

    override fun render(batch: SpriteBatch) {
        clouds.forEach { it.render(batch) }
    }

}