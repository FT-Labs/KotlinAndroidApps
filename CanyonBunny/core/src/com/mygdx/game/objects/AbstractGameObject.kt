/*
Author : Arda
Company : PhysTech
Date : 4/8/2020
*/

package com.mygdx.game.objects

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

abstract class AbstractGameObject {
    val position: Vector2 = Vector2()
    val dimension: Vector2 = Vector2(1f, 1f)
    val origin: Vector2 = Vector2()
    val scale: Vector2 = Vector2(1f, 1f)
    val rotation: Float = 0f
    val velocity = Vector2()
    val terminalVelocity = Vector2(1f, 1f)
    val friction = Vector2()
    val acceleration = Vector2()
    val bounds = Rectangle()

    protected open fun updateMotionX(deltaTime: Float) {
        if (velocity.x != 0f) {
            //Apply friction
        }
        if (velocity.x > 0) {
            velocity.x = Math.max(velocity.x - friction.x * deltaTime, 0f)
        } else {
            velocity.x = Math.min(velocity.x + friction.x * deltaTime, 0f)
        }
        //Apply acceleration
        velocity.x += acceleration.x * deltaTime
        //Making sure it doesnt accede terminal velocity
        velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x, terminalVelocity.x)
    }

    protected open fun updateMotionY(deltaTime: Float) {
        if (velocity.y != 0f) {
            //Apply friction
        }
        if (velocity.y > 0) {
            velocity.y = Math.max(velocity.y - friction.y * deltaTime, 0f)
        } else {
            velocity.y = Math.min(velocity.y + friction.y * deltaTime, 0f)
        }
        //Apply acceleration
        velocity.y += acceleration.y * deltaTime
        //Making sure it doesnt accede terminal velocity
        velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y)
    }

    open fun update(deltaTime: Float) {
        updateMotionX(deltaTime)
        updateMotionY(deltaTime)
        //Move to new position
        position.x += velocity.x * deltaTime
        position.y += velocity.y * deltaTime
    }

    abstract fun render(batch: SpriteBatch)
}
