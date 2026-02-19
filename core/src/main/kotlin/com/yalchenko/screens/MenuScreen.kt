package com.yalchenko.screens

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.yalchenko.MainGame
import com.yalchenko.ui.background.MenuBackground
import com.yalchenko.ui.buttons.GameButton

class MenuScreen(game: MainGame) : BaseScreen(game) {

    private val background = MenuBackground(stage)

    init {
        val table = Table()
        table.setFillParent(true)

        // Используем универсальную кнопку из атласа
        val startButton = GameButton(game.atlasVanil, game.atlasClick, "button1") {
            game.screen = PlacementScreen(game)
        }

        table.add(startButton).size(300f, 150f)
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
        background.dispose()
    }
}
