package com.yalchenko.screens

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.yalchenko.MainGame
import com.yalchenko.ui.background.MenuBackground
import com.yalchenko.ui.buttons.GameButton

class MenuScreen(game: MainGame) : BaseScreen(game) {

    private val background = MenuBackground(
        stage,
        game.woodTexture,
        game.paperTexture)

    init {
        val table = Table()
        table.setFillParent(true)

        // Кнопка Старт (button4)
        val startButton = GameButton(
            game.atlasVanil,
            game.atlasClick,
            "button4"
        ) {
            game.screen = game.placementScreen
        }

        table.add(startButton).size(400f, 200f).center()
        stage.addActor(table)
    }

    override fun render(delta: Float) {
        clearScreen()

        background.renderBackground(batch)

        //
        viewport.apply()

        stage.act(delta)
        stage.draw()
    }

    override fun dispose() {
        super.dispose()
    }
}
