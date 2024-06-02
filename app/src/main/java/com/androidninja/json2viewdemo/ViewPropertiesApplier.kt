package com.androidninja.json2viewdemo

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.json.JSONObject

object ViewPropertiesApplier {

    fun applyProperties(context: Context, view: View, properties: JSONObject) {
        val layoutParams = createLayoutParams(context, view, properties)

        // Apply FrameLayout specific parameters
        if (view.parent is FrameLayout) {
            val frameParams = FrameLayout.LayoutParams(layoutParams.width, layoutParams.height)
            properties.keys().forEach { key ->
                when (key) {
                    "android:layout_gravity" -> {
                        val gravity = properties.getString(key)
                        frameParams.gravity = parseGravity(gravity)
                    }
                    "android:layout_marginTop" -> {
                        frameParams.topMargin = getDimension(context, properties.getString(key))
                    }
                    "android:layout_marginBottom" -> {
                        frameParams.bottomMargin = getDimension(context, properties.getString(key))
                    }
                    "android:layout_marginLeft" -> {
                        frameParams.leftMargin = getDimension(context, properties.getString(key))
                    }
                    "android:layout_marginRight" -> {
                        frameParams.rightMargin = getDimension(context, properties.getString(key))
                    }
                }
            }
            view.layoutParams = frameParams
        }

        // Apply LinearLayout specific parameters
        if (view.parent is LinearLayout) {
            val linearParams = layoutParams as LinearLayout.LayoutParams
            properties.keys().forEach { key ->
                when (key) {
                    "android:layout_gravity" -> {
                        val gravity = properties.getString(key)
                        linearParams.gravity = parseGravity(gravity)
                    }
                    "android:layout_marginTop" -> {
                        linearParams.topMargin = getDimension(context, properties.getString(key))
                    }
                    "android:layout_marginBottom" -> {
                        linearParams.bottomMargin = getDimension(context, properties.getString(key))
                    }
                    "android:layout_marginLeft" -> {
                        linearParams.leftMargin = getDimension(context, properties.getString(key))
                    }
                    "android:layout_marginRight" -> {
                        linearParams.rightMargin = getDimension(context, properties.getString(key))
                    }
                }
            }
            view.layoutParams = linearParams
        }

        // Apply RelativeLayout specific parameters
        if (view.parent is RelativeLayout) {
            val relativeParams = RelativeLayout.LayoutParams(layoutParams.width, layoutParams.height)
            properties.keys().forEach { key ->
                when (key) {
                    "android:layout_below" -> {
                        val id = properties.getString(key).substringAfter("@id/")
                        relativeParams.addRule(RelativeLayout.BELOW, context.resources.getIdentifier(id, "id", context.packageName))
                    }
                }
            }
            view.layoutParams = relativeParams
        }

        // Apply common view properties
        when (view) {
            is LinearLayout -> {
                if (properties.has("android:orientation")) {
                    view.orientation = when (properties.getString("android:orientation")) {
                        "vertical" -> LinearLayout.VERTICAL
                        "horizontal" -> LinearLayout.HORIZONTAL
                        else -> LinearLayout.VERTICAL
                    }
                }
            }
            is TextView -> {
                if (properties.has("android:text")) {
                    view.text = properties.getString("android:text")
                }
                if (properties.has("android:textColor")) {
                    view.setTextColor(Color.parseColor(properties.getString("android:textColor")))
                }
                if (properties.has("android:textSize")) {
                    view.textSize = properties.getString("android:textSize").replace("sp", "").toFloat()
                }
            }
            is ImageView -> {
                if (properties.has("android:src")) {
                    val resourceId = context.resources.getIdentifier(
                        properties.getString("android:src").substringAfter("/"),
                        "drawable",
                        context.packageName
                    )
                    view.setImageResource(resourceId)
                }
                if (properties.has("android:scaleType")) {
                    val scaleType = properties.getString("android:scaleType").uppercase().replace("_", "_")
                    view.scaleType = ImageView.ScaleType.valueOf(scaleType)
                }
            }
        }

        // Handle background color
        if (properties.has("android:background")) {
            view.setBackgroundColor(Color.parseColor(properties.getString("android:background")))
        }

        // Set the ID if it exists
        if (properties.has("android:id")) {
            val id = properties.getString("android:id")
            if (id.startsWith("@+id/")) {
                view.id = context.resources.getIdentifier(id.substring(5), "id", context.packageName)
            }
        }
    }

    private fun createLayoutParams(context: Context, view: View, properties: JSONObject): ViewGroup.LayoutParams {
        return if (properties.has("android:layout_weight") && view.parent is LinearLayout) {
            LinearLayout.LayoutParams(
                getDimension(context, properties.optString("android:layout_width", "wrap_content")),
                getDimension(context, properties.optString("android:layout_height", "wrap_content")),
                properties.optString("android:layout_weight").toFloat()
            )
        } else {
            ViewGroup.LayoutParams(
                getDimension(context, properties.optString("android:layout_width", "wrap_content")),
                getDimension(context, properties.optString("android:layout_height", "wrap_content"))
            )
        }
    }

    private fun parseGravity(gravity: String): Int {
        var result = 0
        gravity.split("|").forEach {
            result = result or when (it) {
                "top" -> Gravity.TOP
                "bottom" -> Gravity.BOTTOM
                "left" -> Gravity.LEFT
                "right" -> Gravity.RIGHT
                "center" -> Gravity.CENTER
                "center_horizontal" -> Gravity.CENTER_HORIZONTAL
                "center_vertical" -> Gravity.CENTER_VERTICAL
                "start" -> Gravity.START
                "end" -> Gravity.END
                "fill" -> Gravity.FILL
                "fill_horizontal" -> Gravity.FILL_HORIZONTAL
                "fill_vertical" -> Gravity.FILL_VERTICAL
                "clip_horizontal" -> Gravity.CLIP_HORIZONTAL
                "clip_vertical" -> Gravity.CLIP_VERTICAL
                else -> Gravity.NO_GRAVITY
            }
        }
        return result
    }

    private fun getDimension(context: Context, dimension: String): Int {
        return when {
            dimension.endsWith("dp") -> {
                val value = dimension.substring(0, dimension.length - 2).toFloat()
                (value * context.resources.displayMetrics.density).toInt()
            }
            dimension.endsWith("sp") -> {
                val value = dimension.substring(0, dimension.length - 2).toFloat()
                (value * context.resources.displayMetrics.scaledDensity).toInt()
            }
            dimension == "match_parent" -> ViewGroup.LayoutParams.MATCH_PARENT
            dimension == "wrap_content" -> ViewGroup.LayoutParams.WRAP_CONTENT
            else -> dimension.toInt()
        }
    }
}
