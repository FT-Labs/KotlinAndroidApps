/*
Author : Arda
Company : PhysTech
Date : 4/9/2020
*/

package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.objects.*


class Level(private val fileName : String) {
    private val TAG = "Level"

    enum class BLOCKTYPE(r: Int, g: Int, b: Int) {
        EMPTY(0,0,0), //black
        ROCK(0, 255, 0),  //green
        PLAYER_SPAWPOINT(255 , 255 , 255),  //White
        ITEM_FEATHER(255, 0, 255),   //Purple
        ITEM_GOLD_COIN(255 , 255 , 0);   //Yellow
        val color : Int = r shl 24 or (g shl 16) or (b shl 8) or 0xff

        fun sameColor(color : Int) = this.color == color

    }

    //objects
    var rocks : ArrayList<Rock> = arrayListOf()
    var goldcoins : ArrayList<GoldCoin> = arrayListOf()
    var feathers : ArrayList<Feather> = arrayListOf()
    var bunnyHead : BunnyHead? = null

    //Loading image file for level data

    val pixmap : Pixmap = Pixmap(Gdx.files.internal(fileName))
    //Scan pixels from top-left to bottom-right
    var lastPixel = -1

    init {
        for (pixelY in 0 until pixmap.height) {
            for (pixelX in 0 until pixmap.width) {
                var offsetHeight = 0.25f
                //Height grows from bottom to top
                val baseHeight = pixmap.height - pixelY
                //Color of current pixel 32-bit RGBA
                val currentPixel: Int = pixmap.getPixel(pixelX, pixelY)
                //find matching color to identify block type at x,y point and create the game object if there is a match

                if (BLOCKTYPE.EMPTY.sameColor(currentPixel)) {
                    //do nothing
                } else if (BLOCKTYPE.ROCK.sameColor(currentPixel)) {
                    if (lastPixel != currentPixel) {
                        val obj = Rock()
                        val heightIncreaseFactor = 0.25f
                        val offSetHeight = -2.5f
                        obj.position.set(pixelX.toFloat(), baseHeight * obj.dimension.y * heightIncreaseFactor + offSetHeight)
                        obj.increaseLength(0)
                        rocks.add(obj)
                    } else {
                        rocks[rocks.size - 1].increaseLength(1)
                    }
                } else if (BLOCKTYPE.PLAYER_SPAWPOINT.sameColor(currentPixel)) {
                    //Player spawn point
                    val obj = BunnyHead()
                    offsetHeight = -3f
                    obj.position.set(pixelX.toFloat(), baseHeight * obj.dimension.y + offsetHeight)
                    bunnyHead = obj
                } else if (BLOCKTYPE.ITEM_FEATHER.sameColor(currentPixel)) {
                    //feather
                    val obj = Feather()
                    offsetHeight = -1.5f
                    obj.position.set(pixelX.toFloat() , baseHeight * obj.dimension.y + offsetHeight)
                    feathers.add(obj)
                } else if (BLOCKTYPE.ITEM_GOLD_COIN.sameColor(currentPixel)) {
                    //Gold coin
                    val obj = GoldCoin()
                    offsetHeight = -1.5f
                    obj.position.set(pixelX.toFloat() , baseHeight * obj.dimension.y + offsetHeight)
                    goldcoins.add(obj)
                } else {
                    //Unknown obj/pixel color
                    val r = 0xff and (currentPixel ushr 24) //red color channel
                    val g = 0xff and (currentPixel ushr 16) //green color channel
                    val b = 0xff and (currentPixel ushr 8) //blue color channel
                    val a = 0xff and currentPixel //alpha channel
                    Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<"
                            + pixelY + ">: r<" + r + "> g<" + g + "> b<" + b + "> a<" + a + ">")
                }
                lastPixel = currentPixel
            }

        }
    }
    private val clouds = Clouds(pixmap.width.toFloat())
    private val mountains = Mountains(pixmap.width)
    private val waterOverlay = WaterOverlay(pixmap.width.toFloat())
    init{
        clouds.position.set(0f , 2f)
        mountains.position.set(-1f , -1f)
        waterOverlay.position.set(0f , -3.75f)
        pixmap.dispose()
        Gdx.app.debug(TAG , "Level : $fileName loaded.")}

    fun render(batch : SpriteBatch){
        mountains.render(batch)
        rocks.forEach { it.render(batch) }
        waterOverlay.render(batch)
        clouds.render(batch)
        //Draw coins
        goldcoins.forEach { it.render(batch) }
        //Draw feathers
        feathers.forEach { it.render(batch) }
        //Draw player
        bunnyHead?.render(batch)
    }

    fun update(deltaTime : Float){
        bunnyHead?.update(deltaTime)
        rocks.forEach { it.update(deltaTime) }
        feathers.forEach { it.update(deltaTime) }
        goldcoins.forEach { it.update(deltaTime) }
        clouds.update(deltaTime)


    }

}