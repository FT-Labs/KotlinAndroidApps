package com.mygdx.game.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.tools.texturepacker.TexturePacker
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings
import com.mygdx.game.CanyonBunny


object DesktopLauncher {
    private var rebuildAtlas = true
    private var drawDebugOutline = true
    @JvmStatic
    fun main(arg: Array<String>) {


//        if (rebuildAtlas){
//            val settings = Settings()
//            settings.maxWidth = 2048
//            settings.maxHeight = 2048
//            settings.duplicatePadding = false
//            settings.debug = drawDebugOutline
//            TexturePacker.process(settings , "desktop/assets-raw/images", "android/assets/images", "canyonbunny.pack")
//        }

        val config = LwjglApplicationConfiguration()
        config.title = "CanyonBunny"
        config.width = 800
        config.height = 480
        LwjglApplication(CanyonBunny() , config)

    }
}