package com.yalchenko.screens
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.yalchenko.MainGame
import com.yalchenko.ui.background.MenuBackground
import com.yalchenko.ui.buttons.GameButton
import com.yalchenko.ui.grid.PaperGrid

class PlacementScreen(game: MainGame) : BaseScreen(game) {

    private val background = MenuBackground(stage)
    private val font = com.badlogic.gdx.graphics.g2d.BitmapFont()
    private val shapeRenderer = ShapeRenderer()

    private val grid = PaperGrid(
        startX = 156.19f,
        startY = 41.98f,
        cellWidth = 54.93f,
        cellHeight = 45.58f,
        rows = 14,
        cols = 20
    )

    // Логика управления
    private fun handleDebugInput() {
        val step = when {
            Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) -> 1f      // Грубая настройка
            Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) -> 0.01f    // Ювелирная настройка (0.01)
            else -> 0.1f                                            // Стандарт (средний шаг)
        }
        // Позиция (Стрелки)
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) grid.startX -= step
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) grid.startX += step
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) grid.startY += step
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) grid.startY -= step

        // Размер клетки (WASD)
        if (Gdx.input.isKeyPressed(Input.Keys.A)) grid.cellWidth -= step
        if (Gdx.input.isKeyPressed(Input.Keys.D)) grid.cellWidth += step
        if (Gdx.input.isKeyPressed(Input.Keys.W)) grid.cellHeight += step
        if (Gdx.input.isKeyPressed(Input.Keys.S)) grid.cellHeight -= step
    }

    init {
        createMenu()
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
        handleDebugInput()
        clearScreen()
        //Рисуем фон (дерево)
        background.renderBackground(batch)
        //Возвращаем viewport для stage
        viewport.apply()
        //Рисуем stage(лист)
        stage.act(delta)
        stage.draw()

        val paper = background.paperImg

        // Создаем временный вектор, чтобы перевести локальные координаты листа в мировые
        val paperCoords = com.badlogic.gdx.math.Vector2(0f, 0f)
        // Эта магия берет (0,0) листика и говорит, где это в координатах твоего Viewport
        paper.localToStageCoordinates(paperCoords)

        val basePaperWidth = 1300f
        val basePaperHeight = 720f

        val scaleX = paper.width / basePaperWidth
        val scaleY = paper.height / basePaperHeight

        //Рисуем "тестовую" сетку проверяя текстуру
        shapeRenderer.projectionMatrix = viewport.camera.combined
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.setColor(0f, 1f, 0f, 1f)

        for(row in 0 until grid.rows){
            for(col in 0 until grid.cols){
                val pos = grid.getCellPosition(row, col)
                shapeRenderer.rect(
                    paperCoords.x + (pos.x * scaleX),
                    paperCoords.y + (pos.y * scaleY),
                    grid.cellWidth * scaleX,
                    grid.cellHeight * scaleY
                )
            }
        }
        shapeRenderer.end()

        //Выводим текущие значения текстом поверх всего
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        font.setColor(1f, 0f, 0f, 1f) // Красный текст, чтоб лучше видно было

        val cam = viewport.camera
        val textX = cam.position.x - cam.viewportWidth / 2 + 20f
        val textY = cam.position.y + cam.viewportHeight / 2 - 20f
        font.draw(batch,
            "DEBUG MODE:\n" +
                "Pos (Arrows): X=${"%.2f".format(grid.startX)}, Y=${"%.2f".format(grid.startY)}\n" +
                "Size (WASD): W=${"%.2f".format(grid.cellWidth)}, H=${"%.2f".format(grid.cellHeight)}\n" +
                "Hold SHIFT for precise (0.1) step",
            textX, textY)
        batch.end()
    }

    override fun dispose() {
        super.dispose()
        background.dispose()
        shapeRenderer.dispose()
    }
}
