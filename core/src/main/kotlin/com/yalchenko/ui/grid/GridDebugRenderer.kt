package com.yalchenko.ui.grid

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage

data class HighlightCell(val row: Int, val col: Int, val color: Color)

class GridDebugRenderer {
    private val shapeRenderer = ShapeRenderer()
    private val highlightedCells = mutableListOf<HighlightCell>()

    fun highlightCell(row: Int, col: Int, color: Color) {
        highlightedCells.add(HighlightCell(row, col, color))
    }

    fun clearHighlights() {
        highlightedCells.clear()
    }

    fun draw(stage: Stage, grid: PaperGrid) {
        if (highlightedCells.isEmpty()) return

        shapeRenderer.projectionMatrix = stage.camera.combined

        //прозрачность
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        highlightedCells.forEach { cell ->
            val x = grid.worldStartX + cell.col * grid.worldCellWidth
            val y = grid.worldStartY + cell.row * grid.worldCellHeight

            shapeRenderer.color = cell.color
            shapeRenderer.rect(x, y, grid.worldCellWidth, grid.worldCellHeight)
        }
        shapeRenderer.end()

        Gdx.gl.glDisable(GL20.GL_BLEND)
    }

    fun dispose() {
        shapeRenderer.dispose()
    }
}
