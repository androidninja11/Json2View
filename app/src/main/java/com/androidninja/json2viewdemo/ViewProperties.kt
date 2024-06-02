import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import org.json.JSONObject

class ViewProperties {
    companion object {
        fun applyCommonProperties(view: View, properties: Map<String, String>) {
            val layoutParams = ViewGroup.LayoutParams(
                getPropertyAsInt(properties["android:layout_width"]),
                getPropertyAsInt(properties["android:layout_height"])
            )
            view.layoutParams = layoutParams

            properties["android:id"]?.let { id ->
                view.id = view.context.resources.getIdentifier(id.substring(5), "id", view.context.packageName)
            }

            properties["android:background"]?.let { background ->
                view.setBackgroundColor(Color.parseColor(background))
            }

            applyDimension(view, properties, "android:paddingTop") { paddingTop ->
                view.setPadding(view.paddingLeft, paddingTop, view.paddingRight, view.paddingBottom)
            }

            applyDimension(view, properties, "android:paddingBottom") { paddingBottom ->
                view.setPadding(view.paddingLeft, view.paddingTop, view.paddingRight, paddingBottom)
            }

            applyDimension(view, properties, "android:paddingLeft") { paddingLeft ->
                view.setPadding(paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)
            }

            applyDimension(view, properties, "android:paddingRight") { paddingRight ->
                view.setPadding(view.paddingLeft, view.paddingTop, paddingRight, view.paddingBottom)
            }

            applyMargins(view,properties)
        }

        fun applyMargins(view: View, properties: Map<String, String>) {
            val layoutParams = view.layoutParams

        }

        fun applyDimension(view: View, properties: Map<String, String>, dimensionKey: String, dimensionSetter: (Int) -> Unit) {
            properties[dimensionKey]?.let { dimensionValue ->
                val dimension = getPropertyAsInt(dimensionValue)
                dimensionSetter(dimension)
            }
        }

        fun applyFrameLayoutParams(view: FrameLayout, properties: Map<String, String>) {
            applyCommonProperties(view, properties)
            applyLayoutParams(view.context,view,properties)

            properties["android:layout_gravity"]?.let { gravity ->
//                view.gra = parseGravity(gravity)
            }
        }

        fun applyTextViewProperties(view: TextView, properties: Map<String, String>) {
            applyCommonProperties(view, properties)

            properties["android:text"]?.let { text ->
                view.text = text
            }

            properties["android:textSize"]?.let { textSize ->
                view.textSize = textSize.substringBefore("sp").toFloat()
            }

            properties["android:textColor"]?.let { textColor ->
                view.setTextColor(Color.parseColor(textColor))
            }

            properties["android:gravity"]?.let { gravity ->
                view.gravity = parseGravity(gravity)
            }

            properties["android:textStyle"]?.let { textStyle ->
                when (textStyle) {
                    "bold" -> view.setTypeface(null, android.graphics.Typeface.BOLD)
                    "italic" -> view.setTypeface(null, android.graphics.Typeface.ITALIC)
                    "normal" -> view.setTypeface(null, android.graphics.Typeface.NORMAL)
                }
            }

            properties["android:textAlignment"]?.let { textAlignment ->
                view.textAlignment = when (textAlignment) {
                    "textStart" -> View.TEXT_ALIGNMENT_TEXT_START
                    "textEnd" -> View.TEXT_ALIGNMENT_TEXT_END
                    "center" -> View.TEXT_ALIGNMENT_CENTER
                    "viewStart" -> View.TEXT_ALIGNMENT_VIEW_START
                    "viewEnd" -> View.TEXT_ALIGNMENT_VIEW_END
                    else -> View.TEXT_ALIGNMENT_CENTER
                }
            }
        }

        private fun getPropertyAsInt(property: String?): Int {
            return when (property) {
                "match_parent" -> ViewGroup.LayoutParams.MATCH_PARENT
                "wrap_content" -> ViewGroup.LayoutParams.WRAP_CONTENT
                else -> property?.replace("dp", "")?.toIntOrNull() ?: 0
            }
        }

        private fun parseGravity(gravity: String?): Int {
            var result = 0
            gravity?.split("|")?.forEach {
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


        private fun applyLayoutParams(context: Context, view: View, properties: Map<String, String>) {
            val layoutParams = createLayoutParams(context, view, properties)

            if (view is FrameLayout) {
                val frameParams = FrameLayout.LayoutParams(layoutParams.width, layoutParams.height)
                properties.keys.forEach { key ->
                    when (key) {
                        "android:layout_gravity" -> {
                            val gravity = properties[key]
                            frameParams.gravity = parseGravity(gravity)
                        }
                        "android:layout_marginTop" -> {
                            val marginTop = properties[key]
                            frameParams.topMargin = getDimension(context, marginTop.toString())
                        }
                        // Add other layout parameters specific to FrameLayout if needed
                    }
                }
                view.layoutParams = frameParams
            } else {
                view.layoutParams = layoutParams
            }
        }

        private fun createLayoutParams(context: Context, view: View, properties: Map<String, String>): ViewGroup.LayoutParams {
            val width = getDimension(context, properties["android:layout_width"] ?: "wrap_content")
            val height = getDimension(context, properties["android:layout_height"] ?: "wrap_content")
            return ViewGroup.LayoutParams(width, height)
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
                else -> dimension.toIntOrNull() ?: ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }

    }
}
