/*
Author : Arda
Company : PhysTech
Date : 4/10/2020
*/

package com.mygdx.game.objects

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.Assets
import com.mygdx.game.Constants

class BunnyHead() : AbstractGameObject() {

    private val TAG = "BunnyHead"
    private var regHead = Assets.bunny.head
    val JUMP_TIME_MAX = 0.3f
    val JUMP_TIME_MIN = 0.1f
    val JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f
    var viewDirection = VIEW_DIRECTION.RIGHT
    var jumpState = JUMP_STATE.FALLING
    var timeJumping = 0f
    var hasFeatherPowerUp = false
    var timeLeftFeatherPowerUp = 0f


    enum class VIEW_DIRECTION { LEFT, RIGHT }
    enum class JUMP_STATE { GROUNDED , FALLING , JUMP_RISING , JUMP_FALLING }

    init {
        dimension.set(1f , 1f)
        //Center image on game obj
        origin.set(dimension.x / 2f , dimension.y / 2f)
        //Bounding box for collision detection
        bounds.set(0f , 0f , dimension.x , dimension.y)
        //Physics values
        terminalVelocity.set(3f , 4f)
        friction.set(12f , 0f)
        acceleration.set(0f , -25f)
    }

    fun setJumping(jumpKeyPressed : Boolean){
        when (jumpState) {
            JUMP_STATE.GROUNDED -> {
                if (jumpKeyPressed){
                    //Start counting jump time
                    timeJumping = 0f
                    jumpState = JUMP_STATE.JUMP_RISING
                }
            }
            JUMP_STATE.JUMP_RISING -> {
                if(!jumpKeyPressed){
                    jumpState = JUMP_STATE.JUMP_FALLING
                }
            }
            JUMP_STATE.FALLING -> {
                //Falling
            }
            JUMP_STATE.JUMP_FALLING -> {
                //Falling after jump
                if (jumpKeyPressed && hasFeatherPowerUp){
                    timeJumping = JUMP_TIME_OFFSET_FLYING
                    jumpState = JUMP_STATE.JUMP_RISING
                }
            }
        }
    }

    fun setFeatherPowerUp(pickedUp : Boolean){
        hasFeatherPowerUp = pickedUp
        if(pickedUp){
            timeLeftFeatherPowerUp = Constants.ITEM_FEATHER_POWERUP_DURATION
        }
    }

    fun hasFeatherPowerUp() : Boolean = hasFeatherPowerUp && timeLeftFeatherPowerUp > 0

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        if(velocity.x != 0f) {
            viewDirection = if (velocity.x < 0) VIEW_DIRECTION.LEFT else VIEW_DIRECTION.RIGHT
        }
        if (timeLeftFeatherPowerUp > 0){
            timeLeftFeatherPowerUp -= deltaTime
            if(timeLeftFeatherPowerUp < 0){
                timeLeftFeatherPowerUp = 0f
                setFeatherPowerUp(false)
            }
        }
    }

    override fun updateMotionY(deltaTime: Float) {
        when (jumpState){
            JUMP_STATE.GROUNDED -> jumpState = JUMP_STATE.FALLING
            JUMP_STATE.JUMP_RISING -> {
                //Keep track of jump time
                timeJumping += deltaTime
                //Time left?
                if (timeJumping <= JUMP_TIME_MAX){
                    velocity.y = terminalVelocity.y
                }
            }
            JUMP_STATE.JUMP_FALLING -> {
                //Add delta times to track jump time
                timeJumping += deltaTime
                //Jump to minimal height if jump key was pressed too short
                if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN){
                    //Still jumping
                    velocity.y = terminalVelocity.y
                }
            }
            else -> kotlin.run {  }
        }
        if (jumpState != JUMP_STATE.GROUNDED){
            super.updateMotionY(deltaTime)
        }
    }

    override fun render(batch: SpriteBatch) {
        //Set special color when obj has feather power
        if (hasFeatherPowerUp){
            batch.setColor(1f , 0.8f , 0f , 1f)
        }
        //Draw image
        val reg = regHead
        batch.draw(reg.texture , position.x , position.y , origin.x , origin.y , dimension.x , dimension.y , scale.x , scale.y,
        rotation , reg.regionX , reg.regionY , reg.regionWidth , reg.regionHeight , viewDirection == VIEW_DIRECTION.LEFT , false)

        //Reset color white
        batch.setColor(1f , 1f , 1f , 1f)
    }

}