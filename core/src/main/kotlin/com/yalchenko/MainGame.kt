package com.yalchenko

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.yalchenko.screens.MenuScreen

class MainGame : Game() {
    // Общие ресурсы для всех экранов
    lateinit var batch: SpriteBatch
    lateinit var atlasVanil: TextureAtlas
    lateinit var atlasClick: TextureAtlas


    // Мировые размеры (один раз задали и забыли)
    val WORLD_WIDTH = 1280f
    val WORLD_HEIGHT = 720f

    override fun create() {
        batch = SpriteBatch()
        atlasVanil = TextureAtlas("atlas/buttons_vanil/buttons_vanil.atlas")
        atlasClick = TextureAtlas("atlas/buttons_click/buttons_click.atlas")
        setScreen(MenuScreen(this))
    }

    override fun dispose() {
        batch.dispose()
        atlasVanil.dispose()
        atlasClick.dispose()
        screen?.dispose()
    }
}
