/*
Author : Arda
Company : PhysTech
Date : 4/6/2020
*/

package com.mygdx.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.objects.AbstractGameObject

class CameraHelper {
    private val TAG = "CameraHelper"
    private val MAX_ZOOM_IN = 0.25f
    private val MAX_ZOOM_OUT = 10.0f

    var position : Vector2 = Vector2()
    private var zoom : Float = 1.0f
    var target : AbstractGameObject? = null

    fun update(deltaTime : Float){

        if (target!= null){
            position.x = target!!.position.x + target!!.origin.y
            position.y = target!!.position.y + target!!.origin.y
        }

        //Prevent camera from moving too far
        position.y = Math.max(-1f , position.y)
    }


    fun addZoom(amount : Float) = setZoom(zoom + amount)
    fun setZoom(Zoom : Float) {
            zoom = MathUtils.clamp(Zoom , MAX_ZOOM_IN , MAX_ZOOM_OUT)}
    fun getZoom() : Float = zoom



    fun applyTo(camera : OrthographicCamera){
        camera.position.x = position.x
        camera.position.y = position.y
        camera.zoom = zoom
        camera.update()
    }


}