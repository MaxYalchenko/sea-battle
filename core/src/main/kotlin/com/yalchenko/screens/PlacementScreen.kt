package com.yalchenko.screens

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.yalchenko.MainGame
import com.yalchenko.logic.ShipManager
import com.yalchenko.ui.background.MenuBackground
import com.yalchenko.ui.buttons.GameButton
import com.yalchenko.ui.grid.GridDebugRenderer
import com.yalchenko.ui.grid.PaperGrid

class PlacementScreen(game: MainGame) : BaseScreen(game) {

    private val background = MenuBackground(stage, game.woodTexture, game.paperTexture)
    private val grid = PaperGrid(startX = 156.19f, startY = 41.98f, cellWidth = 54.93f, cellHeight = 45.58f, rows = 14, cols = 20)
    private val gridDebugRenderer = GridDebugRenderer()
    private val shipManager = ShipManager(game, grid, gridDebugRenderer)


    private val paperCoords = Vector2()
    private val basePaperWidth = 1300f
    private val basePaperHeight = 720f

    init {
        createMenu()
        shipManager.createShips(stage)

    }

    private fun createMenu() {
        // Кнопка Назад (button2)
        val backButton = GameButton(
            game.atlasVanil,
            game.atlasClick,
            "button2"
        ) {
            game.screen = game.menuScreen
        }

        // Кнопка Авто (button1)
        val autoButton = GameButton(
            game.atlasVanil,
            game.atlasClick,
            "button1"
        ) {
            shipManager.autoPlaceAllShips()
        }

        val table = Table().apply {
            setFillParent(true)
            add(backButton).size(150f, 150f).expand().bottom().left().pad(25f)
            add(autoButton).size(150f, 150f).expand().bottom().right().pad(25f)
        }
        stage.addActor(table)
    }

    override fun render(delta: Float) {
        clearScreen()
        background.renderBackground(batch)

        viewport.apply()
        updateGridLogic()

        stage.act(delta)
        stage.draw()

        gridDebugRenderer.draw(stage, grid)
    }

    private fun updateGridLogic() {
        val paper = background.paperImg
        paper.localToStageCoordinates(paperCoords.set(0f, 0f))

        val scaleX = paper.width / basePaperWidth
        val scaleY = paper.height / basePaperHeight

        // Обновляем параметры сетки
        grid.apply {
            worldStartX = paperCoords.x + startX * scaleX
            worldStartY = paperCoords.y + startY * scaleY
            worldCellWidth = cellWidth * scaleX
            worldCellHeight = cellHeight * scaleY
        }

        // Просим менеджер кораблей пересчитать их позиции
        shipManager.updatePositions()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        viewport.update(width, height, true)
    }

    override fun dispose() {
        super.dispose()
        gridDebugRenderer.dispose()
    }
}
