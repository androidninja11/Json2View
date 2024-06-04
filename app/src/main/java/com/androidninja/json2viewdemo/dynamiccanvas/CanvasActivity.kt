package com.androidninja.json2viewdemo.dynamiccanvas

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.androidninja.json2viewdemo.CustomTextView
import com.androidninja.json2viewdemo.TextViewData
import com.androidninja.json2viewdemo.models.CardData
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sddemo.json2viewdemo.R
import timber.log.Timber

class CanvasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        initView()

//        val jsonString = """
//            [
//                {"x": 50, "y": 100, "type": "textview", "size": 40, "textColor": "#FF0000", "text": "Hello, World!"},
//                {"x": 50, "y": 200, "type": "textview", "size": 30, "textColor": "#00FF00", "text": "Another TextView"}
//            ]
//        """.trimIndent()
//
//
//        val textViewDataList = parseJson(jsonString)
//        val customTextView = CustomTextView(this, textViewDataList)
//
//        setContentView(customTextView)



        val constraintLayout = ConstraintLayout(this).apply {
            id = View.generateViewId()
        }
        setContentView(constraintLayout)

        val jsonData = """
            {
              "type": "birthday",
              "background": {"imageUrl": "https://images.rawpixel.com/image_800/cHJpdmF0ZS9sci9pbWFnZXMvd2Vic2l0ZS8yMDIzLTA3L2pvYjE0NDgtYmFja2dyb3VuZC0wNGEteF8xLmpwZw.jpg"},
              "images": [
              {"imageUrl": "https://fakeimg.pl/250x100/", "xPosition": 50, "yPosition": 50, "width": 500, "height": 500},
              {"imageUrl": "https://fakeimg.pl/250x100/", "xPosition": 50, "yPosition": 900, "width": 200, "height": 200}
              ],
              "text": [
                {"content": "Happy Birthday!", "color": "#FF000000", "size": 40, "xPosition": 100, "yPosition": 300},
                {"content": "John Doe", "color": "#000000", "size": 24, "xPosition": 300, "yPosition": 450}
              ]
            }
        """
        val cardData = Gson().fromJson(jsonData, CardData::class.java)

//        val constraintSet = ConstraintSet()
//        constraintSet.clone(constraintLayout)
//
//        // Load and set the background image
//        cardData.background?.imageUrl?.let { url ->
//            val backgroundImageView = ImageView(this).apply {
//                id = View.generateViewId()
//                layoutParams = ConstraintLayout.LayoutParams(
//                    ConstraintLayout.LayoutParams.MATCH_PARENT,
//                    ConstraintLayout.LayoutParams.MATCH_PARENT
//                )
////                scaleType = ImageView.ScaleType.CENTER_CROP
//            }
//            Glide.with(this).load(url).into(backgroundImageView)
//            constraintLayout.addView(backgroundImageView)
//
//            constraintSet.connect(backgroundImageView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
//            constraintSet.connect(backgroundImageView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
//            constraintSet.connect(backgroundImageView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
//            constraintSet.connect(backgroundImageView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
//        }
//
//        // Load and add images
//        cardData.images?.forEach { image ->
//            val imageView = ImageView(this).apply {
//                id = View.generateViewId()
//                layoutParams = ConstraintLayout.LayoutParams(image.width, image.height)
//
//                setOnClickListener { view ->
//                    // Handle click event for this TextView
//                    Toast.makeText(this@CanvasActivity,"Image clicked: ${image.yPosition}",Toast.LENGTH_LONG).show()
//                }
//            }
//            Glide.with(this).load(image.imageUrl).override(image.width, image.height).into(imageView)
//            constraintLayout.addView(imageView)
//
//
//
//
//            constraintSet.connect(imageView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
//            constraintSet.connect(imageView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
//            constraintSet.connect(imageView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
//            constraintSet.connect(imageView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
//            constraintSet.setMargin(imageView.id, ConstraintSet.TOP, image.yPosition)
//            constraintSet.setMargin(imageView.id, ConstraintSet.START, image.xPosition)
//            constraintSet.setMargin(imageView.id, ConstraintSet.END, image.xPosition)
//        }
//
//// Load and add text views
//        cardData.text?.forEach { text ->
//            val textView = TextView(this).apply {
//                id = View.generateViewId()
//                layoutParams = ConstraintLayout.LayoutParams(
//                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                    ConstraintLayout.LayoutParams.WRAP_CONTENT
//                )
//                setTextColor(Color.parseColor(text.color))
//                textSize = text.size.toFloat()
//                typeface = Typeface.DEFAULT_BOLD
////                setText(text.content)
//                setText(text.content)
//
//                setOnClickListener { view ->
//                    // Handle click event for this TextView
//                    Toast.makeText(this@CanvasActivity,"TextView clicked: ${text.content}",Toast.LENGTH_LONG).show()
//                }
//            }
//            Timber.d("Adding TextView with content: ${text.content}, id: ${textView.id}, x: ${text.xPosition}, y: ${text.yPosition}")
//            constraintLayout.addView(textView)
//            constraintSet.connect(textView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
//            constraintSet.connect(textView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
//            constraintSet.connect(textView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
//            constraintSet.connect(textView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
//            constraintSet.setMargin(textView.id, ConstraintSet.TOP, text.yPosition)
//            constraintSet.setMargin(textView.id, ConstraintSet.START, text.xPosition)
//        }
//        constraintSet.applyTo(constraintLayout)



        // Draw background image
        drawBackground(cardData.background?.imageUrl)

        // Draw images
        cardData.images?.forEach { image ->
            drawImage(image.imageUrl, image.xPosition, image.yPosition, image.width, image.height)
        }

        // Draw text views
        cardData.text?.forEach { text ->
            drawText(text.content, text.color, text.size, text.xPosition, text.yPosition)
        }
    }

    private fun initView() {


    }

    private fun parseJson(jsonString: String): List<TextViewData> {
        val gson = Gson()
        val type = object : TypeToken<List<TextViewData>>() {}.type
        return gson.fromJson(jsonString, type)
    }



    private fun drawBackground(imageUrl: String?) {
        // Load and set background image
        val backgroundImageView = ImageView(this)
        Glide.with(this)
            .load(imageUrl)
            .into(backgroundImageView)
        setContentView(backgroundImageView)
    }

    private fun drawImage(imageUrl: String?, xPosition: Int, yPosition: Int, width: Int, height: Int) {
        // Load and add image view
        val imageView = ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(width, height)
            x = xPosition.toFloat()
            y = yPosition.toFloat()
            setOnClickListener {
                Toast.makeText(this@CanvasActivity, "Image clicked", Toast.LENGTH_SHORT).show()
            }
        }
        Glide.with(this)
            .load(imageUrl)
            .into(imageView)
        addContentView(imageView, imageView.layoutParams)
    }

    private fun drawText(content: String, color: String, size: Int, xPosition: Int, yPosition: Int) {
        // Create and add text view
        val textView = TextView(this).apply {
            text = content
            setTextColor(Color.parseColor(color))
            textSize = size.toFloat()
            typeface = Typeface.DEFAULT_BOLD
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            x = xPosition.toFloat()
            y = yPosition.toFloat()

            setOnClickListener {
                Toast.makeText(this@CanvasActivity, "Text clicked ${content}", Toast.LENGTH_SHORT).show()
            }
        }
        addContentView(textView, textView.layoutParams)
    }
}