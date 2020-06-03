/*
Author : Arda
Company : PhysTech
Date : 5/20/2020
*/

package com.mygdx.game

import com.badlogic.gdx.graphics.Color

enum class CharacterSkin(s: String, fl: Float, fl1: Float, fl2: Float) {
    WHITE("Gray", 0.7f, 0.7f, 0.7f),GRAY("Gray", 0.7f, 0.7f, 0.7f),BROWN("Gray", 0.7f, 0.7f, 0.7f);

    val color = Color()

    init {
        color.set(fl , fl1 , fl2 , 1f)
    }

//    fun getColor() : Color = color
}
