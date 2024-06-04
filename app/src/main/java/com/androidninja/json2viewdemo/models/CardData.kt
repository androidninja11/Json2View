package com.androidninja.json2viewdemo.models

data class CardData(
    val type: String,
    val background: BackgroundData?,
    val images: List<ImageData>?,
    val text: List<TextData>?
)

data class BackgroundData(val imageUrl: String?)
data class ImageData(val imageUrl: String, val xPosition: Int, val yPosition: Int, val width: Int, val height: Int)
data class TextData(val content: String, val color: String, val size: Int, val xPosition: Int, val yPosition: Int)
