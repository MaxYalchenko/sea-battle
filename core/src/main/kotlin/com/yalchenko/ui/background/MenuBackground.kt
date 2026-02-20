package com.yalchenko.ui.background

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.yalchenko.MainGame

class MenuBackground(
    private val stage: Stage,
    private val woodTexture: Texture,
    private val paperTexture: Texture
) {

    lateinit var paperImg: Image
    private val backgroundMatrix = Matrix4()

    init {
        createPaperLayout()
    }

    private fun createPaperLayout() {

        val mainTable = Table()
        mainTable.setFillParent(true)

        val stack = Stack()
        paperImg = Image(paperTexture)
        paperImg.setScaling(com.badlogic.gdx.utils.Scaling.stretch)

        stack.add(paperImg)

        mainTable.add(stack)
            .fillY()
            .width(1300f)
            .expandX()
            .center()

        stage.addActor(mainTable)
    }
    fun renderBackground(batch: SpriteBatch) {

        batch.projectionMatrix = backgroundMatrix.setToOrtho2D(
            0f,
            0f,
            Gdx.graphics.width.toFloat(),
            Gdx.graphics.height.toFloat()
        )

        batch.begin()

        batch.draw(
            woodTexture,
            0f,
            0f,
            Gdx.graphics.width.toFloat(),
            Gdx.graphics.height.toFloat()
        )
        batch.end()
    }
}
