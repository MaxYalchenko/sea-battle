package com.yalchenko.screens
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.yalchenko.MainGame
import com.yalchenko.ui.background.MenuBackground
import com.yalchenko.ui.buttons.GameButton
import com.badlogic.gdx.math.Vector2
import com.yalchenko.ui.grid.PaperGrid

class PlacementScreen(game: MainGame) : BaseScreen(game) {

    private val background = MenuBackground(stage)

    private val grid = PaperGrid(
        startX = 156.19f,
        startY = 41.98f,
        cellWidth = 54.93f,
        cellHeight = 45.58f,
        rows = 14,
        cols = 20
    )

    private val paperCoords = Vector2()
    private val basePaperWidth = 1300f
    private val basePaperHeight = 720f

    // Список для хранения тестовых кнопок
    private val testButtons = mutableListOf<GameButton>()

    init {
        createMenu()
        spawnGridButtons()
    }

    private fun spawnGridButtons() {
        // Заполняем всё поле кнопками
        for (row in 0 until grid.rows) {
            for (col in 0 until grid.cols) {
                val btn = GameButton(game.atlasVanil, game.atlasClick, "button1") {
                    println("Нажата клетка: $row, $col")
                }
                testButtons.add(btn)
                stage.addActor(btn)
            }
        }
    }
    private fun createMenu() {

        val startButton = GameButton(game.atlasVanil, game.atlasClick, "button1") {
            game.screen = MenuScreen(game)
        }

        val table = Table()
        table.setFillParent(true)

        table.add(startButton).size(300f, 150f)

        stage.addActor(table)
    }



    override fun render(delta: Float){
        clearScreen()
        //Рисуем фон (дерево)
        background.renderBackground(batch)
        //Возвращаем viewport для stage
        viewport.apply()

        //Рисуем stage(лист)
        stage.act(delta)
        stage.draw()


        // Эти расчеты пригодятся, когда ставить объекты на лист
        val paper = background.paperImg
        paper.localToStageCoordinates(paperCoords.set(0f, 0f))

        val scaleX = paper.width / basePaperWidth
        val scaleY = paper.height / basePaperHeight

        // 2. Обновляем положение и размер каждой кнопки согласно сетке
        var index = 0
        for (row in 0 until grid.rows) {
            for (col in 0 until grid.cols) {
                val cellPos = grid.getCellPosition(row, col)
                val btn = testButtons[index]

                // Размер кнопки подгоняем под размер клетки с учетом масштаба
                btn.setSize(grid.cellWidth * scaleX, grid.cellHeight * scaleY)

                // Позиция: угол бумаги + (смещение клетки * масштаб)
                btn.setPosition(
                    paperCoords.x + (cellPos.x * scaleX),
                    paperCoords.y + (cellPos.y * scaleY)
                )
                index++
            }
        }
        stage.act(delta)
        stage.draw()
    }

    override fun dispose() {
        super.dispose()
        background.dispose()
    }
}
