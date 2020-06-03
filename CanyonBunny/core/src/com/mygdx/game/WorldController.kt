package com.mygdx.game

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.mygdx.game.objects.BunnyHead
import com.mygdx.game.objects.Feather
import com.mygdx.game.objects.GoldCoin
import com.mygdx.game.objects.Rock
import com.mygdx.game.screens.MenuScreen
import ktx.app.KtxGame
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import kotlin.properties.Delegates


class WorldController(val game : KtxGame<KtxScreen>) : KtxInputAdapter {
    lateinit var testSprites: ArrayList<Sprite>
    private var selectedSprite by Delegates.notNull<Int>()
    private val TAG = "WorldController"
    val cameraHelper = CameraHelper()
    var lives = Constants.LIVES_START
    lateinit var level: Level
    var score by Delegates.notNull<Int>()
    private val r1 = Rectangle()
    private val r2 = Rectangle()
    lateinit var bunnyHead: BunnyHead
    var timeLeftGameOverDelay by Delegates.notNull<Float>()


    fun backToMenu(){
        //switch to menu screen
        game.setScreen<MenuScreen>()
    }

    private fun init(){
        timeLeftGameOverDelay = 0f
        lives = Constants.LIVES_START
    }

    init {
        Gdx.input.inputProcessor = this
        init()
        initLevel()
    }

    private fun onCollisionBunnyHeadWithRock(rock: Rock) {
        when (bunnyHead.jumpState) {
            BunnyHead.JUMP_STATE.GROUNDED -> kotlin.run { }
            BunnyHead.JUMP_STATE.JUMP_FALLING, BunnyHead.JUMP_STATE.FALLING -> {
                bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y
                bunnyHead.jumpState = BunnyHead.JUMP_STATE.GROUNDED
            }
            BunnyHead.JUMP_STATE.JUMP_RISING -> bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y
        }

        val heightDifference = Math.abs((bunnyHead.position.y) - (rock.position.y + rock.bounds.height))
        if (heightDifference > 0.25f) {
            val hitRightEdge: Boolean = bunnyHead.position.x > (rock.position.x + rock.bounds.width / 2f)
            if (hitRightEdge) {
                bunnyHead.position.x = rock.position.x + rock.bounds.width
            } else {
                bunnyHead.position.x = rock.position.x - bunnyHead.bounds.width
            }
            return
        }
    }

    private fun onCollisionBunnyHeadWithGoldCoin(goldCoin: GoldCoin) {
        goldCoin.collected = true
        score += goldCoin.getScore()
        Gdx.app.log(TAG, "Gold coin collected.")

    }

    private fun onCollisionBunnyWithFeather(feather: Feather) {
        feather.collected = true
        score += feather.getScore()
        level.bunnyHead?.setFeatherPowerUp(true)
        Gdx.app.log(TAG, "Feather collected")
    }


    private fun testCollision() {

        r1.set(level.bunnyHead?.position!!.x, level.bunnyHead?.position!!.y, level.bunnyHead?.bounds!!.width, level.bunnyHead?.bounds!!.height)

        if(Gdx.input.isKeyJustPressed(Input.Keys.P)) print("${bunnyHead.position.x} , ${bunnyHead.position.y}\n")

        //Collision BunnyHead <-> Rocks
        for (rock in level.rocks) {
            r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height)
            if (!r1.overlaps(r2)) {
                continue
            }

            onCollisionBunnyHeadWithRock(rock)
            //IMPORTANT: must do all collisions for valid edge testing on rocks
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.O)){
            level.rocks.forEach { print("${it.position.x} , ${it.position.y} , ${it.bounds.width} , ${it.bounds.height}\n") }
        }

        //Collision BunnyHead <-> Coins
        for (goldcoin in level.goldcoins) {
            if (goldcoin.collected) continue
            r2.set(goldcoin.position.x, goldcoin.position.y, goldcoin.bounds.width, goldcoin.bounds.height)
            if (!r1.overlaps(r2)) continue
            onCollisionBunnyHeadWithGoldCoin(goldcoin)
            break
        }
        //Collision BunnyHead <-> Feathers
        for (feather in level.feathers) {
            if (feather.collected) continue
            r2.set(feather.position.x, feather.position.y, feather.bounds.width, feather.bounds.height)
            if (!r1.overlaps(r2)) continue
            onCollisionBunnyWithFeather(feather)
            break
        }
    }


    private fun initLevel() {
        score = 0
        level = Level(Constants.LEVEL_01)
        bunnyHead = level.bunnyHead!!
        cameraHelper.target = level.bunnyHead!!
    }


    private fun createProceduralPixmap(width: Int, height: Int): Pixmap {
        val pixmap: Pixmap = Pixmap(width, height, Pixmap.Format.RGB888)
        pixmap.setColor(1f, 0f, 0f, 0.5f)
        pixmap.fill()
        pixmap.setColor(1f, 1f, 0f, 1f)
        pixmap.drawLine(0, 0, width, height)
        pixmap.drawLine(width, 0, 0, height)
        pixmap.setColor(0f, 1f, 1f, 1f)
        pixmap.drawRectangle(0, 0, width, height)
        return pixmap
    }


    fun update(deltaTime: Float) {
        handleDebugInput(deltaTime)

        if (isGameOver()){
            timeLeftGameOverDelay -= deltaTime
            if (timeLeftGameOverDelay < 0) {
                backToMenu()
//                init()
//                this.lives += 1
            }
            else handleInputGame(deltaTime)
        }

        handleInputGame(deltaTime)
        level.update(deltaTime)
        testCollision()
        cameraHelper.update(deltaTime)
        if (!isGameOver() && isPlayerInWater()){
            lives --
            if (isGameOver()) timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER
            else initLevel()
        }

    }

    fun isGameOver() : Boolean = lives < 0

    fun isPlayerInWater() : Boolean = level.bunnyHead!!.position.y < -5f

    private fun handleInputGame(deltaTime: Float) {
        if(cameraHelper.target == level.bunnyHead){
            //Player movement
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  level.bunnyHead!!.velocity.x = -level.bunnyHead!!.terminalVelocity.x
            else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) level.bunnyHead!!.velocity.x = level.bunnyHead!!.terminalVelocity.x
            else {
                //Execute auto-forward movement on non desktop
                if(Gdx.app.type != Application.ApplicationType.Desktop){
                    level.bunnyHead!!.velocity.x = level.bunnyHead!!.terminalVelocity.x
                }
            }
            //Jump
            if (Gdx.input.isTouched || Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                level.bunnyHead!!.setJumping(true)
            }else{
                level.bunnyHead!!.setJumping(false)
            }
        }
    }


    private fun handleDebugInput(deltaTime: Float) {
        if (Gdx.app.type != Application.ApplicationType.Desktop) return


        //Camera controls
        var camMoveSpeed = 5 * deltaTime
        val camMoveAccelarationFactor = 5f

        if (cameraHelper.target == null) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) camMoveSpeed *= camMoveAccelarationFactor
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveCamera(-camMoveSpeed, 0f)
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveCamera(camMoveSpeed, 0f)
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) moveCamera(0f, camMoveSpeed)
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) moveCamera(0f, -camMoveSpeed)
            if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) cameraHelper.position.set(0f, 0f)
        }


        //Camera zoom controls
        var camZoomSpeed = 1 * deltaTime
        val camZoomSpeedAccelerationFactor = 5f
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor
        if (Gdx.input.isKeyPressed(Input.Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed)
        if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed)
        if (Gdx.input.isKeyPressed(Input.Keys.P)) cameraHelper.setZoom(1f)
    }

    private fun moveCamera(x: Float, y: Float) {
        cameraHelper.position.set(cameraHelper.position.x + x, cameraHelper.position.y + y)
    }


    override fun keyUp(keycode: Int): Boolean {
        //Resetting game world
        if (keycode == Input.Keys.R) {
            initLevel()
            Gdx.app.debug(TAG, "Game has been resetted.")
        } else if (keycode == Input.Keys.ENTER) {
            cameraHelper.target = null
            Gdx.app.debug(TAG, "Camera follow enabled.")
        }else if(keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK){
            backToMenu()
        }

        return false
    }
}



