/*
Author : Arda
Company : PhysTech
Date : 5/20/2020
*/

package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils

object GamePreferences {
    private val TAG = "GamePreferences"
    private val prefs = Gdx.app.getPreferences(Constants.PREFERENCES)
    var sound = false
    var music = false
    var volSound = 0f
    var volMusic = 0f
    var charSkin = 0
    var showFpsCounter = false

    fun load(){
        val sound = prefs.getBoolean("sound" , true)
        val music = prefs.getBoolean("music" , true)
        val volSound = MathUtils.clamp(prefs.getFloat("volSound" , 0.5f) , 0f , 0f)
        val volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f),
                0.0f, 1.0f);
        val charSkin = MathUtils.clamp(prefs.getInteger("charSkin", 0),
                0, 2);
        val showFpsCounter = prefs.getBoolean("showFpsCounter", false);
    }

    fun save(){
        prefs.putBoolean("sound", sound);
        prefs.putBoolean("music", music);
        prefs.putFloat("volSound", volSound);
        prefs.putFloat("volMusic", volMusic);
        prefs.putInteger("charSkin", charSkin);
        prefs.putBoolean("showFpsCounter", showFpsCounter);
        prefs.flush();
    }

}