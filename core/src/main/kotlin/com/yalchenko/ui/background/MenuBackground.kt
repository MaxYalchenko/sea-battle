package com.yalchenko.ui.background

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table

class MenuBackground(
    private val stage: Stage
) {

    private val woodTexture = Texture("wood_texture.jpg")
    private val paperTexture = Texture("paper_texture.jpg")
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

        // stretch заставит текстуру бумаги занять ровно столько места,
        // сколько мы выделим в ячейке таблицы ниже
        paperImg.setScaling(com.badlogic.gdx.utils.Scaling.stretch)

        stack.add(paperImg)

        // Изменения здесь:
        mainTable.add(stack)
            .fillY()         // Растягивает по высоте до 720 (на весь экран)
            .width(1300f)    // Делаем шире (было 700f, поставим 1000f для солидности)
            .expandX()       // Занимает всё свободное место по горизонтали
            .center()        // Центрирует листик по горизонтали

        stage.addActor(mainTable)
    }

    fun renderBackground(batch: SpriteBatch) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)

        backgroundMatrix.setToOrtho2D(
            0f,
            0f,
            Gdx.graphics.width.toFloat(),
            Gdx.graphics.height.toFloat()
        )

        batch.projectionMatrix = backgroundMatrix
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

    fun dispose() {
        woodTexture.dispose()
        paperTexture.dispose()
    }
}
