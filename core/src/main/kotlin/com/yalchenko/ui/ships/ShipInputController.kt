package com.yalchenko.ui.ships

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.DragListener
import kotlin.math.roundToInt

class ShipInputController(
    private val ship: Ship,
    private val gridRenderer: com.yalchenko.ui.grid.GridDebugRenderer
) {

    private var dragOffsetX = 0f
    private var dragOffsetY = 0f

    init {
        addDragLogic()
        addClickLogic()
    }

    private fun addDragLogic() {
        ship.addListener(object : DragListener() {
            override fun drag(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                val grid = ship.grid
                val stageX = event!!.stageX - dragOffsetX
                val stageY = event.stageY - dragOffsetY

                val snappedCol = ((stageX - grid.worldStartX) / grid.worldCellWidth).roundToInt()
                val snappedRow = ((stageY - grid.worldStartY) / grid.worldCellHeight).roundToInt()

                val newCol = snappedCol.coerceIn(-5, grid.cols + 5)
                val newRow = snappedRow.coerceIn(-5, grid.rows + 5)

                val forbiddenCells = ship.manager.getForbiddenCells(ship)
                val canPlace = ship.manager.canPlaceShip(ship, newRow, newCol, ship.deckCount, ship.isVertical)

                gridRenderer.clearHighlights()
                val faintRed = Color(1f, 0f, 0f, 0.08f)
                forbiddenCells.forEach { (r, c) ->
                    gridRenderer.highlightCell(r, c, faintRed)
                }

                ship.moveTowards(newRow, newCol, speed = 50f)
                ship.color.a = if (canPlace) 1f else 0.5f
            }

            override fun dragStop(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                val manager = ship.manager
                if (manager.canPlaceShip(ship, ship.currentRow, ship.currentCol, ship.deckCount, ship.isVertical)) {
                    ship.savePosition()
                } else {
                    ship.returnToLastValid()
                }
                gridRenderer.clearHighlights()
                ship.color.a = 1f
            }
        })
    }

    private fun addClickLogic() {
        val clickListener = object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (tapCount == 2) {
                    val manager = ship.manager
                    if (manager.canPlaceShip(
                            ship,
                            ship.currentRow,
                            ship.currentCol,
                            ship.deckCount,
                            !ship.isVertical
                        )
                    ) {
                        ship.toggleRotation()
                        ship.updateFromGrid()
                    }
                }
            }
        }
        //Для телефонов чтоефонов что бы корректно работал двойной клик для поворота кораблей
        clickListener.tapSquareSize = 25f
        ship.addListener(clickListener)
    }
}
