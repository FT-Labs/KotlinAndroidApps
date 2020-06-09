/*
Author : Arda
Company : PhysTech
Date : 4/18/2020
*/

package com.mygdx.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.mygdx.game.*
import kotlin.properties.Delegates


class MenuScreen(game: CanyonBunny) : AbstractGameScreen(game) {
    private val TAG = "MenuScreen"
    private lateinit var stage: Stage
    private lateinit var skinCanyonBunny: Skin
    private lateinit var skinLibgdx : Skin
    private lateinit var imgBackground: Image
    private lateinit var imgLogo: Image
    private lateinit var imgInfo: Image
    private lateinit var imgCoins: Image
    private lateinit var imgBunny: Image
    private lateinit var btnMenuPlay: Button
    private lateinit var btnMenuOptions: Button

    //options
    private lateinit var winOptions: Window
    private lateinit var btnWinOptSave: TextButton
    private lateinit var btnWinOptCancel: TextButton
    private lateinit var chkSound: CheckBox
    private lateinit var sldSound: Slider
    private lateinit var chkMusic: CheckBox
    private lateinit var sldMusic: Slider
    private lateinit var selCharSkin: SelectBox<CharacterSkin>
    private lateinit var imgCharSkin: Image
    private lateinit var chkShowFpsCounter: CheckBox


    //debug
    private val DEBUG_REBUILD_INTERVAL = 5f
    private var debugEnabled = false
    private var debugRebuildStage by Delegates.notNull<Float>()

    private fun rebuildStage() {
        skinCanyonBunny = Skin(Gdx.files.internal(Constants.SKIN_CANYONBUNNY_UI), TextureAtlas(Constants.TEXTURE_ATLAS_UI))
        skinLibgdx = Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI) , TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI))
        //Build layers
        val layerBackground: Table = buildBackGroundLayer()
        val layerObjects = buildObjectsLayer()
        val layerLogos = buildLogosLayer()
        val layerControls = buildControlsLayer()
        val layerOptionsWindow = buildOptionsWindowLayer()
        //Assemble stage for menu screen
        stage.clear()
        val stack = Stack()
        stage.addActor(stack)
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT)
        stack.add(layerBackground)
        stack.add(layerObjects)
        stack.add(layerLogos)
        stack.add(layerControls)
        stage.addActor(layerOptionsWindow)

    }

    private fun buildBackGroundLayer(): Table {
        val layer = Table()
        //+ Bg
        imgBackground = Image(skinCanyonBunny, "background")
        layer.add(imgBackground)
        return layer
    }

    private fun buildObjectsLayer(): Table {
        val layer = Table()
        //Coins
        imgCoins = Image(skinCanyonBunny, "coins")
        layer.addActor(imgCoins)
        imgCoins.setPosition(135f, 80f)
        //Bunny
        imgBunny = Image(skinCanyonBunny, "bunny")
        layer.addActor(imgBunny)
        imgBunny.setPosition(355f, 40f)
        return layer
    }

    private fun buildLogosLayer(): Table {
        val layer = Table()
        layer.left().top()
        //Game logo
        imgLogo = Image(skinCanyonBunny, "logo")
        layer.add(imgLogo)
        layer.row().expandY()
        //Info logos
        imgInfo = Image(skinCanyonBunny, "info")
        layer.add(imgInfo).bottom()
        if (debugEnabled) layer.debug()
        return layer
    }

    private fun buildControlsLayer(): Table {
        val layer = Table()
        layer.right().bottom()
        //play
        btnMenuPlay = Button(skinCanyonBunny, "play")
        layer.add(btnMenuPlay)
        btnMenuPlay.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                onPlayClicked()
            }
        })
        layer.row()
        //options
        btnMenuOptions = Button(skinCanyonBunny , "options")
        layer.add(btnMenuOptions)
        btnMenuOptions.addListener(object : ChangeListener(){
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                onOptionsClicked()
            }
        })
        if (debugEnabled) layer.debug()
        return layer
    }

    private fun onPlayClicked(){
        game.setScreen<GameScreen>()
    }


    private fun buildOptWinAudioSettings() : Table {
        val tbl = Table()
        //+ title : "audio"
        tbl.pad(10f , 10f , 0f , 10f)
        tbl.add(Label("Audio" , skinLibgdx , "default-font" , Color.ORANGE)).colspan(3)
        tbl.row()
        tbl.columnDefaults(0).padRight(10f)
        tbl.columnDefaults(1).padRight(10f)
        //checkbox , "sound" label, sound volume slider
        chkSound = CheckBox("" , skinLibgdx)
        tbl.add(chkSound)
        tbl.add(Label("Sound" , skinLibgdx))
        sldSound = Slider(0f , 1f , 0.1f , false ,skinLibgdx)
        tbl.add(sldSound)
        tbl.row()
        //+ checkbox , "Music" label , music volume slider
        chkMusic = CheckBox("" , skinLibgdx)
        tbl.add(chkMusic)
        tbl.add(Label("Music" , skinLibgdx))
        sldMusic = Slider(0f , 1f , 0.1f , false ,skinLibgdx)
        tbl.add(sldMusic)
        tbl.row()
        return tbl
    }

    private fun buildOptWinSkinSelection() : Table{
        val tbl = Table()
        //title : character skin
        tbl.pad(10f , 10f , 0f , 10f)
        tbl.add(Label("Character Skin" , skinLibgdx , "default-font" , Color.ORANGE)).colspan(2)
        tbl.row()
        //Drop down box skin items
        selCharSkin = SelectBox<CharacterSkin>(skinLibgdx)
        selCharSkin.setItems(Array(CharacterSkin.values()))

        selCharSkin.addListener(object : ChangeListener(){
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                onCharSkinSelected((actor as SelectBox<*>).selectedIndex)
            }
        })
        tbl.add(selCharSkin).width(120f).padRight(20f)
        //Preview image
        imgCharSkin = Image(Assets.bunny.head)
        tbl.add(imgCharSkin).width(50f).height(50f)
        return tbl
    }

    private fun buildOptWinDebug(): Table? {
        val tbl = Table()
        // + Title: "Debug"
        tbl.pad(10f, 10f, 0f, 10f)
        tbl.add(Label("Debug", skinLibgdx, "default-font",
                Color.RED)).colspan(3)
        tbl.row()
        tbl.columnDefaults(0).padRight(10f)
        tbl.columnDefaults(1).padRight(10f)
        // + Checkbox, "Show FPS Counter" label
        chkShowFpsCounter = CheckBox("", skinLibgdx)
        tbl.add(Label("Show FPS Counter", skinLibgdx))
        tbl.add(chkShowFpsCounter)
        tbl.row()
        return tbl
    }

    private fun buildOptWinButtons() : Table{
        val tbl = Table()
        //seperator
        var lbl = Label("" , skinLibgdx)
        lbl.setColor(0.75f , 0.75f , 0.75f , 1f)
        lbl.style = Label.LabelStyle(lbl.style)
        lbl.style.background = skinLibgdx.newDrawable("white")
        tbl.add(lbl).colspan(2).height(1f).width(220f).pad(0f , 0f , 0f , 1f)
        tbl.row()
        lbl = Label("" , skinLibgdx)
        lbl.setColor(0.5f , 0.5f , 0.5f , 1f)
        lbl.style = Label.LabelStyle(lbl.style)
        lbl.style.background = skinLibgdx.newDrawable("white")
        tbl.add(lbl).colspan(2).height(1f).width(220f).pad(0f , 1f , 5f , 0f)
        tbl.row()
        //Save button event handler
        btnWinOptSave = TextButton("Save" , skinLibgdx)
        tbl.add(btnWinOptSave).padRight(30f)
        btnWinOptSave.addListener(object  : ChangeListener(){
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                onSaveClicked()
            }
        })
        //Cancel button with event handler
        btnWinOptCancel = TextButton("Cancel" , skinLibgdx)
        tbl.add(btnWinOptCancel)
        btnWinOptCancel.addListener(object : ChangeListener(){
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                onCancelClicked()
            }
        })
        return tbl
    }


    private fun buildOptionsWindowLayer(): Table {
        winOptions = Window("Options" , skinLibgdx)
        //Audio
        winOptions.add(buildOptWinAudioSettings()).row()
        //char skin select box
        winOptions.add(buildOptWinSkinSelection()).row()
        //Debug fps
        winOptions.add(buildOptWinDebug()).row()
        //seperator and buttons
        winOptions.add(buildOptWinButtons()).pad(10f , 0f , 10f , 0f)
        //Options windows slightly transparent
        winOptions.setColor(1f , 1f , 1f , 0.8f)
        //Hide options by default
        winOptions.isVisible = false
        if(debugEnabled) winOptions.debug()
        //Tablelayout recalculate widget sizes and positions
        winOptions.pack()
        //Move to bottom right corner
        winOptions.setPosition(Constants.VIEWPORT_GUI_WIDTH - winOptions.width - 50f , 50f)
        return winOptions
    }

    private fun onOptionsClicked(){
        loadSettings()
        btnMenuPlay.isVisible = false
        btnMenuOptions.isVisible = false
        winOptions.isVisible = true
    }


    override fun render(delta: Float) {
//        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

//        if(Gdx.input.isTouched) game.setScreen<GameScreen>()

        if (debugEnabled) {
            debugRebuildStage -= delta
            if (debugRebuildStage <= 0) {
                debugRebuildStage = DEBUG_REBUILD_INTERVAL
                rebuildStage()
            }
        }
        stage.act(delta)
        stage.draw()
        Table.debugTableColor
    }



    override fun hide() {
        stage.dispose()
        skinCanyonBunny.dispose()
        skinLibgdx.dispose()
    }

    override fun show() {
        stage = Stage(StretchViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT))
        Gdx.input.inputProcessor = stage
        rebuildStage()
    }

    private fun loadSettings() {
        GamePreferences.load()
        chkSound.isChecked = GamePreferences.sound
        sldSound.setValue(GamePreferences.volSound)
        chkMusic.isChecked = GamePreferences.music
        sldMusic.setValue(GamePreferences.volMusic)
        selCharSkin.selectedIndex = GamePreferences.charSkin
        onCharSkinSelected(GamePreferences.charSkin)
        chkShowFpsCounter.isChecked = GamePreferences.showFpsCounter
    }

    private fun saveSettings(){
        GamePreferences.sound = chkSound.isChecked
        GamePreferences.volSound = sldSound.value
        GamePreferences.music = chkMusic.isChecked
        GamePreferences.volMusic = sldMusic.value
        GamePreferences.charSkin = selCharSkin.selectedIndex
        GamePreferences.showFpsCounter = chkShowFpsCounter.isChecked
        GamePreferences.save()
    }

    private fun onCharSkinSelected(index : Int){
        val skin = CharacterSkin.values()[index]
        imgCharSkin.color = skin.color
    }

    private fun onSaveClicked(){
        saveSettings()
        onCancelClicked()
    }

    private fun onCancelClicked(){
        btnMenuPlay.isVisible = true
        btnMenuOptions.isVisible = true
        winOptions.isVisible = false
    }

}