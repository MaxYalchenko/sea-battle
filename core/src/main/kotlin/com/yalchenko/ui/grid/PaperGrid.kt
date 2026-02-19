package com.yalchenko.ui.grid
import com.badlogic.gdx.math.Vector2

class PaperGrid(
    var startX: Float,
    var startY: Float,

    var cellWidth: Float,
    var cellHeight: Float,

    val rows: Int,
    val cols: Int
) {
    // Перевод логических координат в экранные
    fun getCellPosition(row: Int, col: Int): Vector2{
        // Столбцы (col) идут по горизонтали -> умножаем на ширину
        val x = startX + col * cellWidth
        // Строки (row) идут по вертикали -> умножаем на высоту
        val y = startY + row * cellHeight
        return Vector2(x,y)
    }
}
