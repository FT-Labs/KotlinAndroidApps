package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import kotlin.collections.ArrayList
import kotlin.random.Random


class FlappyBird : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var background: Texture
    lateinit var sprite: Sprite
    lateinit var bottomTube: Texture
    lateinit var topTube: Texture
//    lateinit var shapeRenderer : ShapeRenderer
    var birds: ArrayList<Texture> = ArrayList<Texture>(2)
    var flapState = 0
    var BirdY = 0.0
    var birdVelocity = 0f
    var gravity = 2f
    var gap = 400f
    lateinit var birdCircle : Circle
    lateinit var pipeRectangleBottom : Array<Rectangle>
    lateinit var pipeRectangleTop : Array<Rectangle>
    var score : Int = 0
    var scoringTube : Int = 0


    //variables for tubes #1
    var tubeVelocity = 7f
    //var tubeX : Float? = null
    val numberOfTubes: Int = 4
    var distanceBetweenTubes: Float = 0f
    val tubeOffset = Array<Float>(4) { 0f }
    val tubeX = Array<Float>(4) { 0f }
    lateinit var font : BitmapFont


    //Variables i added
    var x = 0.1f
    var isTouched = false
    var ReverseFly = false
    var SecTimer = System.currentTimeMillis()
    var BirdX = 0.0
    var fallVelocity = 0.3f


    override fun create() {

        batch = SpriteBatch()
        background = Texture("bg.png")
        topTube = Texture("toptube.png")
        bottomTube = Texture("bottomtube.png")
//        birds[0] = Texture("bird.png")
//        birds[1]= Texture("bird2.png")
//        shapeRenderer = ShapeRenderer()
        birdCircle = Circle()
        font = BitmapFont()
        font.setColor(Color.WHITE)
        font.data.setScale(10f)
        font.setOwnsTexture(true)



//        Another method or arraylist

        birds.add(Texture("bird.png"))
        birds.add(Texture("bird2.png"))
        BirdY = ((Gdx.graphics.height - birds[0].height) / 2.0)
        BirdX = (Gdx.graphics.width - 3.8 * birds[flapState].width) / 2.0
        sprite = Sprite(birds[flapState])


        //Variables for tubes
        distanceBetweenTubes = (Gdx.graphics.width / 2 + 150).toFloat()
        var maxTubeOffset = (Gdx.graphics.height - gap) / 2 - 100
        pipeRectangleBottom = Array(4){ Rectangle() }
        pipeRectangleTop = Array(4){Rectangle()}



        for (i in 0 until numberOfTubes) {
            tubeOffset[i] = (Random.nextFloat() - 0.5f) * (Gdx.graphics.height - gap - 275)
            tubeX[i] = ((Gdx.graphics.width - topTube.width) / 2).toFloat() + + Gdx.graphics.width + i * distanceBetweenTubes
        }


    }

    override fun render() {//Ekrana bir kere basıldıktan sonra sürekli çalıştırılacak kod
        //batch.draw(birds[flapState], (BirdX).toFloat(), (( BirdY + x )).toFloat())
        when {
            tubeX[0] < -bottomTube.width -> {
                tubeOffset[0] = (Random.nextFloat() - 0.5f) * (Gdx.graphics.height - gap - 275)
            }
            tubeX[1] < -bottomTube.width -> {
                tubeOffset[1] = (Random.nextFloat() - 0.5f) * (Gdx.graphics.height - gap - 275)
            }
            tubeX[2] < -bottomTube.width -> {
                tubeOffset[2] = (Random.nextFloat() - 0.5f) * (Gdx.graphics.height - gap - 275)
            }
            tubeX[3] < -bottomTube.width -> {
                tubeOffset[3] = (Random.nextFloat() - 0.5f) * (Gdx.graphics.height - gap - 275)
            }
            //Kanat cırpısı icin
        }


        flapTimer()  //Kanat cırpısı icin

        batch.begin()
        batch.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())


        if (tubeX[scoringTube] < (Gdx.graphics.width - 3.8 * birds[flapState].width) / 2.0){
            score += 1
            Gdx.app.log("Score = ","$score")
            if (scoringTube < 3){
                scoringTube += 1
            }else{
                scoringTube = 0
            }
        }

        if (BirdY + (birds[0].height) / 2 > 0) {

            if (Gdx.input.justTouched()) {
                drawTubes(isTouched)

                birdVelocity = -21f
                //batch.draw(birds[flapState], (BirdX).toFloat(), (( BirdY + x )).toFloat())
                rotationClicked()

                font.draw(batch, score.toString(), Gdx.graphics.width / 2.1f, Gdx.graphics.height - 100f)

                isTouched = true



            } else if (!isTouched) {
                beforeGameStart()
            } else if (isTouched) {
                //Ekrana bir kere basıldıktan sonra sürekli çalıştırılacak kod
                drawTubes(isTouched)
                font.draw(batch, score.toString(), Gdx.graphics.width / 2.1f, Gdx.graphics.height - 100f)


                rotationNormal()
                birdVelocity++
                BirdY -= birdVelocity + gravity
            }
        } else {
            sprite.draw(batch)
        }

        batch.end()

        birdCircle.set(((Gdx.graphics.width - 3 * birds[flapState].width) / 2 ).toFloat(), (BirdY + birds[flapState].height / 2 + x).toFloat(), (birds[flapState].width / 2).toFloat())

        for(i in 0 until numberOfTubes) {
            pipeRectangleBottom[i] = (Rectangle(tubeX[i], (Gdx.graphics.height + gap) / 2 + tubeOffset[i],bottomTube.width.toFloat(), bottomTube.height.toFloat()))
            pipeRectangleTop[i] = (Rectangle(tubeX[i], (Gdx.graphics.height + gap) / 2 + tubeOffset[i],topTube.width.toFloat(), topTube.height.toFloat()))

        }


//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
//        shapeRenderer.color = Color.RED
//        shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius)

        for(i in pipeRectangleTop.indices){

//            shapeRenderer.rect(tubeX[i], (Gdx.graphics.height + gap) / 2 + tubeOffset[i],pipeRectangleBottom[i].width, pipeRectangleBottom[i].height)
//            shapeRenderer.rect(tubeX[i], (Gdx.graphics.height - gap) / 2 - bottomTube.height + tubeOffset[i], pipeRectangleTop[i].width, pipeRectangleTop[i].height)

            if (Intersector.overlaps(birdCircle, pipeRectangleTop[i]) || Intersector.overlaps(birdCircle, pipeRectangleBottom[i])){
                Gdx.app.log("Collisio","pilawbi")
            }

        }



//        shapeRenderer.end()
    }

    fun drawTubes(isTouched: Boolean) {
        if (isTouched) {

            for (i in 0 until numberOfTubes) {
                if (tubeX[i] < -bottomTube.width) {
                    tubeX[i] += numberOfTubes * distanceBetweenTubes
                }else{
                    tubeX[i] -= tubeVelocity
                }

                batch.draw(topTube, (tubeX[i]), (Gdx.graphics.height + gap) / 2 + tubeOffset[i])
                batch.draw(bottomTube, (tubeX[i]), ((Gdx.graphics.height - gap) / 2 - bottomTube.height + tubeOffset[i]))
            }
        }
    }


    fun beforeGameStart() {
        //Basılı değilse aşağı yukarı uç
        if (!ReverseFly) {
            x += 3.5f
            if (x > 40f) {
                ReverseFly = true
            }
        } else if (ReverseFly) {
            x -= 3.5f
            if (x < -60f) {
                ReverseFly = false
            }
        }
        batch.draw(birds[flapState], (BirdX).toFloat(), ((BirdY + x)).toFloat())
    }

    fun flapTimer() {
        //Kanatları çırpmak için timer kodu
        if (System.currentTimeMillis() > SecTimer + 220) {
            if (flapState == 0) {
                flapState = 1
            } else {
                flapState = 0
            }
            SecTimer = System.currentTimeMillis()
        }
    }

    fun rotationNormal() {

        sprite.texture = birds[flapState]
        sprite.setPosition((BirdX).toFloat(), (BirdY + x).toFloat())
//        val fallRotation = sprite.getRotation()
//        val timeToFall = (BirdY + x) / birdVelocity
//        sprite.rotate(((-90f - abs(fallRotation)) / timeToFall).toFloat())
        if (sprite.getRotation() > -90)
            sprite.rotate(-fallVelocity)

        sprite.draw(batch)
        fallVelocity += 0.1f

    }

    fun rotationClicked() {
        sprite.texture = birds[flapState]
        sprite.setPosition((BirdX).toFloat(), (BirdY + x).toFloat())
        val initialRotation = sprite.getRotation()

        sprite.rotate(30f - initialRotation)

        sprite.draw(batch)
        fallVelocity = 0.3f
    }


    override fun dispose() {
        batch.dispose()
        background.dispose()

    }
}

