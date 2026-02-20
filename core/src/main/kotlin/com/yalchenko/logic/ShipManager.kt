package com.yalchenko.logic

import com.badlogic.gdx.scenes.scene2d.Stage
import com.yalchenko.MainGame
import com.yalchenko.ui.grid.GridDebugRenderer
import com.yalchenko.ui.grid.PaperGrid
import com.yalchenko.ui.ships.Ship
import com.yalchenko.ui.ships.ShipInputController

class ShipManager(
    private val game: MainGame,
    private val grid: PaperGrid,
    val gridRenderer: GridDebugRenderer
) {

    private val ships = mutableListOf<Ship>()
    //Разрешенная зона расстановки кораблей
    private val allowedRowStart = 2
    private val allowedRowEnd = 11

    private val allowedColStart = 1
    private val allowedColEnd = 10
    fun createShips(stage: Stage) {

        // ==== 4 однопалубных ====
        repeat(4) { index ->
            val ship = Ship(game.shipAtlas, "oneDeck", 1, grid, this)
            ShipInputController(ship, gridRenderer)
            ships.add(ship)
            stage.addActor(ship)
        }

        // ==== 3 двухпалубных ====
        repeat(3) {
            val ship = Ship(game.shipAtlas, "twoDeck", 2, grid, this)
            ShipInputController(ship, gridRenderer)
            ships.add(ship)
            stage.addActor(ship)
        }

        // ==== 2 трехпалубных ====
        repeat(2) {
            val ship = Ship(game.shipAtlas, "threeDeck", 3, grid, this)
            ShipInputController(ship, gridRenderer)
            ships.add(ship)
            stage.addActor(ship)
        }

        // ==== 1 четырехпалубный ====
        val fourDeck = Ship(game.shipAtlas, "fourDeck", 4, grid, this)
        ShipInputController(fourDeck, gridRenderer)
        ships.add(fourDeck)
        stage.addActor(fourDeck)
        setDefaultPositions()
    }


    private fun setDefaultPositions() {

        var index = 0

        // 4 однопалубных
        ships[index++].setGridPosition(2, 12)
        ships[index++].setGridPosition(2, 13)
        ships[index++].setGridPosition(2, 14)
        ships[index++].setGridPosition(2, 15)

        // 3 двухпалубных
        ships[index++].setGridPosition(6, 12)
        ships[index++].setGridPosition(5, 12)
        ships[index++].setGridPosition(4, 12)

        // 2 трехпалубных
        ships[index++].setGridPosition(9, 12)
        ships[index++].setGridPosition(8, 12)

        // 1 четырехпалубный
        ships[index].setGridPosition(11, 12)

        ships.forEach { it.savePosition() }
    }

    fun updatePositions() {
        ships.forEach {
            it.updateSize(grid.worldCellWidth, grid.worldCellHeight)
            it.updateFromGrid()
        }
    }

    fun getForbiddenCells(movingShip: Ship): Set<Pair<Int, Int>> {
        val forbidden = mutableSetOf<Pair<Int, Int>>()

        ships.filter { it != movingShip }.forEach { other ->
            other.getOccupiedCells().forEach { (r, c) ->
                // включаем соседние клетки, чтобы учесть касание
                for (dr in -1..1) {
                    for (dc in -1..1) {
                        val nr = r + dr
                        val nc = c + dc
                        if (nr in allowedRowStart..allowedRowEnd && nc in allowedColStart..allowedColEnd) {
                            forbidden.add(nr to nc)
                        }
                    }
                }
            }
        }

        return forbidden
    }

    fun autoPlaceAllShips() {
        ships.forEach { ship ->
            var placed = false
            var attempts = 0

            while (!placed && attempts < 100) {
                val vertical = listOf(true, false).random()
                val row = (allowedRowStart..allowedRowEnd).random()
                val col = (allowedColStart..allowedColEnd).random()

                if (canPlaceShip(ship, row, col, ship.deckCount, vertical)) {
                    ship.setOrientation(vertical)
                    ship.moveTo(row, col)
                    ship.savePosition()
                    placed = true
                }
                attempts++
            }

            if (!placed) {
                println("Не удалось разместить корабль ${ship.deckCount}-палубный")
            }
        }
    }

    fun canPlaceShip(
        ship: Ship,
        row: Int,
        col: Int,
        deckCount: Int,
        isVertical: Boolean
    ): Boolean {

        val newCells = mutableListOf<Pair<Int, Int>>()

        for (i in 0 until deckCount) {
            val r = if (isVertical) row + i else row
            val c = if (isVertical) col else col + i

            //Выход за границы всей сетки
            if (r !in 0 until grid.rows || c !in 0 until grid.cols) {
                return false
            }

            //Выход за разрешённую зону
            if (r !in allowedRowStart..allowedRowEnd ||
                c !in allowedColStart..allowedColEnd) {
                return false
            }

            newCells.add(r to c)
        }

        // проверяем пересечение и касание
        ships.filter { it != ship }.forEach { other ->

            other.getOccupiedCells().forEach { (r, c) ->

                for (newCell in newCells) {

                    val dr = kotlin.math.abs(r - newCell.first)
                    val dc = kotlin.math.abs(c - newCell.second)

                    // если расстояние <= 1 касается
                    if (dr <= 1 && dc <= 1) {
                        return false
                    }
                }
            }
        }

        return true
    }
}
