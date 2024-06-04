package com.androidninja.json2viewdemo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class CustomTextView(context: Context, private val textViewDataList: List<TextViewData>) : View(context) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (textViewData in textViewDataList) {
            val paint = Paint().apply {
                color = Color.parseColor(textViewData.textColor)
                textSize = textViewData.size
            }

            canvas.drawText(textViewData.text, textViewData.x, textViewData.y, paint)
        }
    }
}