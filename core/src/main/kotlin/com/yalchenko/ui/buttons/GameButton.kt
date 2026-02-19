package com.yalchenko.ui.buttons

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

class GameButton(
    atlasUp: TextureAtlas,
    atlasDown: TextureAtlas,
    regionName: String,
    onClick: () -> Unit
):  ImageButton(createStyle(atlasUp, atlasDown, regionName)){
    init {
        // Добавляем слушатель клика
        addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                onClick()
            }
        })
        setTransform(true)
    }

    companion object {
        private fun createStyle(up: TextureAtlas, down: TextureAtlas, name: String): ImageButtonStyle {
            val style = ImageButtonStyle()
            // Берем картинку из "ванильного" атласа
            style.imageUp = TextureRegionDrawable(up.findRegion(name))
            // Берем картинку из "клик" атласа
            style.imageDown = TextureRegionDrawable(down.findRegion(name))
            return style
        }
    }
}
