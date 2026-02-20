package com.yalchenko.ui.ships

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.yalchenko.logic.ShipManager
import com.yalchenko.ui.grid.PaperGrid

class Ship(
    atlas: TextureAtlas,
    regionName: String,
    val deckCount: Int,
    val grid: PaperGrid,
    val manager: ShipManager

) : Image(atlas.findRegion(regionName)) {

    var lastValidRow = 0
    var lastValidCol = 0
    var currentRow = 0
        private set

    var currentCol = 0
        private set

    var isVertical = false
        private set

    private var cellWidth = 0f
    private var cellHeight = 0f


    fun savePosition() {
        lastValidRow = currentRow
        lastValidCol = currentCol
    }

    fun moveTowards(row: Int, col: Int, speed: Float = 10f) {
        val target = grid.getWorldCellPosition(row, col)
        val dx = target.x - x
        val dy = target.y - y

        // двигаем по x и y быстрее, умножая на speed
        setPosition(x + dx * speed * Gdx.graphics.deltaTime,
            y + dy * speed * Gdx.graphics.deltaTime)

        // обновляем логические координаты только для подсветки
        currentRow = row
        currentCol = col
    }
    fun returnToLastValid() {
        moveTo(lastValidRow, lastValidCol)
    }
    fun toggleRotation() {
        isVertical = !isVertical
        rotation = if (isVertical) 90f else 0f
    }

    fun setGridPosition(row: Int, col: Int) {
        currentRow = row
        currentCol = col
    }

    fun setOrientation(vertical: Boolean) {
        isVertical = vertical
        rotation = if (isVertical) 90f else 0f
    }

    fun updateSize(cellWidth: Float, cellHeight: Float) {
        this.cellWidth = cellWidth
        this.cellHeight = cellHeight

        setSize(cellWidth * deckCount, cellHeight)
        setOrigin(0f, 0f)

        rotation = if (isVertical) 90f else 0f
    }

    fun updateFromGrid() {
        val worldPos = grid.getWorldCellPosition(currentRow, currentCol)

        if (isVertical) {
            setPosition(worldPos.x + cellWidth, worldPos.y)
        } else {
            setPosition(worldPos.x, worldPos.y)
        }
    }

    fun getOccupiedCells(): List<Pair<Int, Int>> {
        val cells = mutableListOf<Pair<Int, Int>>()

        for (i in 0 until deckCount) {
            val r = if (isVertical) currentRow + i else currentRow
            val c = if (isVertical) currentCol else currentCol + i
            cells.add(r to c)
        }

        return cells
    }

    fun moveTo(row: Int, col: Int) {
        currentRow = row
        currentCol = col
        updateFromGrid()
    }
}
