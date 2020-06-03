package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.V
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.Align

class WorldRenderer(val worldController: WorldController) : Disposable {

    private var camera: OrthographicCamera = OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT)
    private var batch: SpriteBatch = SpriteBatch()
    private var cameraGUI = OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT)


    init {
        camera.position.set(0f, 0f, 0f)
        camera.update()

        cameraGUI.position.set(0f, 0f, 0f)
        cameraGUI.setToOrtho(true)      //flip y-axis
        cameraGUI.update()
    }


    fun render() {
        renderWorld(batch)
        renderGui(batch)
    }

    private fun renderWorld(batch: SpriteBatch) {
        worldController.cameraHelper.applyTo(camera)
        batch.projectionMatrix = camera.combined
        batch.begin()
        worldController.level.render(batch)
        batch.end()
    }

    fun SpriteBatch.setWhite(){
        this.setColor(1f , 1f , 1f , 1f)
    }

    private fun renderGuiFeatherPowerUp(batch: SpriteBatch){
        val x = -15f
        val y = 20f
        val timeLeftFeatherPowerUp = worldController.level.bunnyHead!!.timeLeftFeatherPowerUp
        if(timeLeftFeatherPowerUp > 0){
            //Start icon fade in/out if power up time is less than 4second. Fade interval is set to 5 changes per sec
            if(timeLeftFeatherPowerUp < 4){
                if ((timeLeftFeatherPowerUp.toInt() * 5 % 2) != 0) batch.setColor(1f , 1f , 1f , 0.5f)
            }
        }
        batch.draw(Assets.feather.feather , x , y , 50f , 50f , 100f , 100f , 0.4f , -0.4f , 0f)
        batch.setWhite()
        Assets.fonts.defaultSmall.draw(batch , "${timeLeftFeatherPowerUp.toInt()}" , x + 60f , y + 57f)

    }

    private fun renderGuiGameOverMessage(batch: SpriteBatch){
        val x = cameraGUI.viewportWidth / 2f
        val y = cameraGUI.viewportHeight / 2f
        val glyph = BitmapFont.Glyph()
        if (worldController.isGameOver()){
            val fontGameOver : BitmapFont = Assets.fonts.defaultBig
            fontGameOver.setColor(1f , 0.75f , 0.25f , 1f)
            fontGameOver.draw(batch , "GAME OVER" , x , y , 0f , Align.center , true)
        }

    }

    private fun renderGuiScore(batch: SpriteBatch) {
        val x = -15f
        val y = -15f
        batch.draw(Assets.goldCoin.goldCoin, x, y, 50f, 50f, 100f, 100f, 0.35f, -0.35f, 0f)
        Assets.fonts.defaultBig.draw(batch, "" + worldController.score, x + 75f, y + 37f)
    }

    private fun renderGuiExtraLives(batch: SpriteBatch) {
        val x = cameraGUI.viewportWidth - 50f - Constants.LIVES_START * 50f
        val y = -15f
        for (i in 0 until Constants.LIVES_START) {
            if (worldController.lives > i) {
                batch.setColor(0.5f, 0.5f, 0.5f, 0.5f)
                batch.draw(Assets.bunny.head, x + i * 50, y, 50f, 50f, 120f, 100f, 0.35f, -0.35f, 0f)
                batch.setColor(1f, 1f, 1f, 1f)
            }
        }
    }

    private fun renderGuiFpsCounter(batch: SpriteBatch) {
        val x = cameraGUI.viewportWidth - 55f
        val y = cameraGUI.viewportHeight - 15f
        val fps : Int = Gdx.graphics.framesPerSecond
        val fpsFont : BitmapFont = Assets.fonts.defaultNormal

        when {
            fps >= 45 -> fpsFont.setColor(0f , 1f , 0f ,1f)         //Green color
            fps >= 30 -> fpsFont.setColor(1f , 1f , 0f , 1f)        //Yellow color
            else -> fpsFont.setColor(1f , 0f , 0f , 1f)             //Red color
        }

        fpsFont.draw(batch , "FPS: $fps" , x , y)
        fpsFont.setColor(1f , 1f , 1f ,1f)      //White
    }

    private fun renderGui(batch: SpriteBatch){
        batch.projectionMatrix = cameraGUI.combined
        batch.begin()
        //Draw collected gold coins icon + text (set to left edge)
        renderGuiScore(batch)
        //Render extra lives icon + text (top right edge)
        renderGuiExtraLives(batch)
        //Draw fps text
        if(GamePreferences.showFpsCounter) renderGuiFpsCounter(batch)
        renderGuiGameOverMessage(batch)
        renderGuiFeatherPowerUp(batch)
        batch.end()
    }


    fun resize(width: Int, height: Int) {
        camera.viewportWidth = Constants.VIEWPORT_HEIGHT / height * width
        camera.update()

        cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT
        cameraGUI.viewportWidth = Constants.VIEWPORT_GUI_WIDTH / height * width
        cameraGUI.position.set(cameraGUI.viewportWidth / 2f, cameraGUI.viewportHeight / 2f, 0f)
        cameraGUI.update()
    }

    override fun dispose() {
        batch.dispose()
    }
}