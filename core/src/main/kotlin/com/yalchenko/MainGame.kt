package com.yalchenko

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.yalchenko.screens.MenuScreen
import com.yalchenko.screens.PlacementScreen

class MainGame : Game() {
    // Общие ресурсы для всех экранов
    lateinit var batch: SpriteBatch
    lateinit var atlasVanil: TextureAtlas
    lateinit var atlasClick: TextureAtlas

    lateinit var shipAtlas: TextureAtlas

    lateinit var woodTexture: Texture
    lateinit var paperTexture: Texture

    lateinit var menuScreen: MenuScreen
    lateinit var placementScreen: PlacementScreen

    val WORLD_WIDTH = 1280f
    val WORLD_HEIGHT = 720f

    override fun create() {
        batch = SpriteBatch()

        atlasVanil = TextureAtlas("atlas/buttons_vanil/buttons_vanil.atlas")
        atlasClick = TextureAtlas("atlas/buttons_click/buttons_click.atlas")
        shipAtlas = TextureAtlas("atlas/ships/ships.atlas")

        woodTexture = Texture("wood_texture.jpg")
        paperTexture = Texture("paper_texture.png")

        menuScreen = MenuScreen(this)
        placementScreen = PlacementScreen(this)


        setScreen(MenuScreen(this))
    }

    override fun dispose() {
        woodTexture.dispose()
        paperTexture.dispose()
        batch.dispose()
        atlasVanil.dispose()
        atlasClick.dispose()
        shipAtlas.dispose()
        screen?.dispose()
    }
}
