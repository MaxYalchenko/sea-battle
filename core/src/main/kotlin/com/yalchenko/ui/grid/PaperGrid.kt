package com.yalchenko.ui.grid
import com.badlogic.gdx.math.Vector2

class PaperGrid(
    var startX: Float,
    var startY: Float,

    var cellWidth: Float,
    var cellHeight: Float,

    val rows: Int,
    val cols: Int,

) {
    var worldStartX = 0f
    var worldStartY = 0f

    var worldCellWidth = 0f
    var worldCellHeight = 0f

    fun getWorldCellPosition(row: Int, col: Int): Vector2 {
        return Vector2(
            worldStartX + col * worldCellWidth,
            worldStartY + row * worldCellHeight
        )
    }

}
