/*
Author : Arda
Company : PhysTech
Date : 4/7/2020
*/

package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetErrorListener
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Disposable

object Assets : Disposable, AssetErrorListener {
    val TAG = "Assets"
    val assetManager = AssetManager()
    val fonts = AssetFonts()

    class AssetFonts(val defaultSmall: BitmapFont = BitmapFont(Gdx.files.internal("images/arial-15.fnt") , true),
                     val defaultNormal: BitmapFont = BitmapFont(Gdx.files.internal("images/arial-15.fnt") , true),
                     val defaultBig: BitmapFont = BitmapFont(Gdx.files.internal("images/arial-15.fnt") , true)) {

        init {
            //Set font sizes
            defaultSmall.data.setScale(0.75f)
            defaultNormal.data.setScale(1f)
            defaultBig.data.setScale(2f)
            //Enable linear texture filtering for smooth fonts
            defaultSmall.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            defaultNormal.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            defaultBig.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        }

    }

    init {
        //Set asset manager error handler
        assetManager.setErrorListener(this)
        //Load texture atlas

        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas::class.java)
        //Start loading assets and wait
        assetManager.finishLoading()
        Gdx.app.debug(TAG, "${assetManager.assetNames.size} Assets loaded.")

        assetManager.assetNames.forEach { Gdx.app.debug(TAG, "Asset : $it") }
        assetManager.update()

    }

    val atlas: TextureAtlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS)

    init {
        atlas.textures.forEach { it.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear) }
    }

    //Game objects
    val bunny = AssetBunny(atlas)
    val goldCoin = AssetGoldCoin(atlas)
    val rock = AssetRock(atlas)
    val feather = AssetFeather(atlas)
    val levelDecoration = AssetDecoration(atlas)


    override fun dispose() {
        assetManager.dispose()
        fonts.defaultBig.dispose()
        fonts.defaultNormal.dispose()
        fonts.defaultSmall.dispose()
    }

    override fun error(asset: AssetDescriptor<*>?, throwable: Throwable?) {
        Gdx.app.error(TAG, "Asset couldn't be loaded.   ->    ${asset?.fileName}    ->   Exception : $throwable  ")
    }

    class AssetBunny(atlas: TextureAtlas) {
        val head = atlas.findRegion("bunny_head")
    }

    class AssetRock(atlas: TextureAtlas) {
        val edge = atlas.findRegion("rock_edge")
        val middle = atlas.findRegion("rock_middle")
    }

    class AssetGoldCoin(atlas: TextureAtlas) {
        val goldCoin = atlas.findRegion("item_gold_coin")
    }

    class AssetFeather(atlas: TextureAtlas) {
        val feather = atlas.findRegion("item_feather")
    }

    class AssetDecoration(atlas: TextureAtlas) {
        val mountainLeft = atlas.findRegion("mountain_left")
        val mountainRight = atlas.findRegion("mountain_right")
        val cloud01 = atlas.findRegion("cloud01")
        val cloud02 = atlas.findRegion("cloud02")
        val cloud03 = atlas.findRegion("cloud03")
        val waterOverlay = atlas.findRegion("water_overlay")

    }

}