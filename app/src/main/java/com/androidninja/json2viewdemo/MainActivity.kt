package com.androidninja.json2viewdemo

import ViewProperties
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.androidninja.json2viewdemo.dynamiclayoutinflator.DynamicLayoutInflator
import com.sddemo.json2viewdemo.R
import com.sddemo.json2viewdemo.databinding.ActivityMainBinding
import org.json.JSONObject
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.IOException
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.math.abs


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isDragging = false
    private lateinit var deleteIcon : ImageView
    private lateinit var deleteIcon1 : ImageView
    private var currentlyTouchedView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        initView()
        dynamicLayoutView()


//        val layout = DynamicLayoutInflator.inflate(
//            this,
//            "<RelativeLayout\n" +
//                    "        android:layout_width=\"match_parent\"\n" +
//                    "        android:layout_height=\"wrap_content\">\n" +
//                    "        <TextView\n" +
//                    "            android:id=\"@+id/textViewName\"\n" +
//                    "            android:layout_width=\"match_parent\"\n" +
//                    "            android:layout_height=\"50dp\"\n" +
//                    "            android:layout_gravity=\"center\"\n" +
//                    "            android:gravity=\"center\"\n" +
//                    "            android:text=\"Name\"\n" +
//                    "            android:textSize=\"20sp\"\n" +
//                    "            android:textColor=\"#FF000000\"/>\n" +
//                    "    </RelativeLayout>"
//        ) as RelativeLayout
//        binding.rootLayout.addView(layout)
//        DynamicLayoutInflator.setDelegate(layout, this)


    }

    @SuppressLint("ClickableViewAccessibility")
    private fun dynamicLayoutView() {
        try {

            val jsonString = """
             <?xml version="1.0" encoding="utf-8"?>
             <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
                 android:layout_width="match_parent"
                 android:layout_height="wrap_content">

                 <LinearLayout
                     android:layout_marginTop="50dp"
                     android:orientation="vertical"
                     android:background="@color/black"
                     android:layout_width="match_parent"
                     android:layout_height="wrap_content">


                     <RelativeLayout
                         android:id="@+id/movableTextViewContainer"
                         android:layout_width="wrap_content"
                         android:layout_height="wrap_content"
                         android:layout_marginTop="20dp"
                         android:layout_gravity="center">

                         <TextView
                             android:id="@+id/movableTextView"
                             android:layout_width="wrap_content"
                             android:layout_height="wrap_content"
                             android:text="Name Here"
                             android:gravity="center"
                             android:paddingStart="30dp"
                             android:paddingEnd="30dp"
                             android:textSize="20sp"
                             android:textColor="#FFFFFF"/>

                         <ImageView
                             android:id="@+id/deleteIcon"
                             android:layout_width="20dp"
                             android:layout_height="20dp"
                             android:src="@drawable/ic_delete"
                             android:visibility="gone"
                             android:padding="2dp"
                             android:layout_toEndOf="@+id/movableTextView"
                             android:layout_gravity="end"
                             android:layout_margin="5dp"/>
                     </RelativeLayout>

                     <RelativeLayout
                         android:id="@+id/movableTextViewContainer1"
                         android:layout_width="wrap_content"
                         android:layout_height="wrap_content"
                         android:layout_marginTop="20dp"
                         android:layout_gravity="center">

                         <TextView
                             android:id="@+id/movableTextView1"
                             android:layout_width="wrap_content"
                             android:layout_height="wrap_content"
                             android:text="Name Here"
                             android:gravity="center"
                             android:paddingStart="30dp"
                             android:paddingEnd="30dp"
                             android:textSize="20sp"
                             android:textColor="#FFFFFF"/>

                         <ImageView
                             android:id="@+id/deleteIcon1"
                             android:layout_width="20dp"
                             android:layout_height="20dp"
                             android:src="@drawable/ic_delete"
                             android:visibility="gone"
                             android:padding="2dp"
                             android:layout_toEndOf="@+id/movableTextView1"
                             android:layout_gravity="end"
                             android:layout_margin="5dp"/>
                     </RelativeLayout>

                     <ImageView
                         android:id="@+id/image1"
                         android:layout_gravity="center"
                         android:layout_width="wrap_content"
                         android:layout_height="wrap_content"
                         android:src="@drawable/sample_image"/>
                 </LinearLayout>

             </FrameLayout>
        """.trimIndent()

//            val view: View = DynamicLayoutInflator.inflate(this, assets.open("testlayout.xml"), binding.rootLayout)
                val view: View = DynamicLayoutInflator.inflate(this, jsonString, binding.rootLayout)
            DynamicLayoutInflator.setDelegate(view, this)

            val movableTextViewContainer = DynamicLayoutInflator.findViewByIdString(view,"movableTextViewContainer") as RelativeLayout?
            val movableTextViewContainer1 = DynamicLayoutInflator.findViewByIdString(view,"movableTextViewContainer1") as RelativeLayout?
            val someTextView = DynamicLayoutInflator.findViewByIdString(view,"textViewName") as TextView?
            val someTextView1 = DynamicLayoutInflator.findViewByIdString(view,"textViewName1") as TextView?
            val someImageView = DynamicLayoutInflator.findViewByIdString(view,"image1") as ImageView?
            deleteIcon = DynamicLayoutInflator.findViewByIdString(view,"deleteIcon") as ImageView
            deleteIcon = DynamicLayoutInflator.findViewByIdString(view,"deleteIcon1") as ImageView
            someTextView?.text = "After Update new text Arvind"
//            someImageView?.setImageResource(R.drawable.psd)

            movableTextViewContainer?.let { setMovable(it) }
            movableTextViewContainer1?.let { setMovable(it) }
            someTextView?.let { setMovable(it) }
            someTextView1?.let { setMovable(it) }
            someImageView?.let {
                setMovable(it)
//                setTouchListeners(it)
            }

            // Set touch listener for the parent layout to detect touches outside the views
            binding.rootLayout.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    resetPreviouslyTouchedView()
                }
                false
            }



        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setMovable1(view: View) {
        view.setOnTouchListener(object : View.OnTouchListener {
            var dX = 0f
            var dY = 0f
            var lastAction = 0

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = v.x - event.rawX
                        dY = v.y - event.rawY
                        lastAction = MotionEvent.ACTION_DOWN
                    }
                    MotionEvent.ACTION_MOVE -> {
                        v.y = event.rawY + dY
                        v.x = event.rawX + dX
                        lastAction = MotionEvent.ACTION_MOVE
                    }
                    MotionEvent.ACTION_UP -> {
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            // It's a click, handle it if needed
                            Toast.makeText(this@MainActivity,"Text Touched",Toast.LENGTH_LONG).show()

                        }
                    }
                    else -> return false
                }
                return true
            }
        })
    }

    private fun setMovable2(view: View) {
        view.setOnTouchListener(object : View.OnTouchListener {
            var dX = 0f
            var dY = 0f
            var lastAction = 0
            var initialX = 0f
            var initialY = 0f
            val clickThreshold = 10

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = v.x - event.rawX
                        dY = v.y - event.rawY
                        initialX = event.rawX
                        initialY = event.rawY
                        lastAction = MotionEvent.ACTION_DOWN
                        isDragging = false
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val newX = event.rawX + dX
                        val newY = event.rawY + dY
                        if (abs(newX - v.x) > clickThreshold || abs(newY - v.y) > clickThreshold) {
                            v.y = newY
                            v.x = newX
                            lastAction = MotionEvent.ACTION_MOVE
                            isDragging = true
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        if (!isDragging) {
                            onClickView(v)
                            Toast.makeText(this@MainActivity,"Click Action",Toast.LENGTH_LONG).show()
                        }
                        isDragging = false
                    }
                    else -> return false
                }
                return true
            }
        })
    }

    private fun setMovable(view: View) {
        view.setOnTouchListener(object : View.OnTouchListener {
            var dX = 0f
            var dY = 0f
            var lastAction = 0
            var initialX = 0f
            var initialY = 0f
            val clickThreshold = 10

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        resetPreviouslyTouchedView()
                        currentlyTouchedView = v
                        dX = v.x - event.rawX
                        dY = v.y - event.rawY
                        initialX = event.rawX
                        initialY = event.rawY
                        lastAction = MotionEvent.ACTION_DOWN
                        isDragging = false
                        showDeleteIcon(v)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val newX = event.rawX + dX
                        val newY = event.rawY + dY
                        if (abs(newX - v.x) > clickThreshold || abs(newY - v.y) > clickThreshold) {
                            v.y = newY
                            v.x = newX
                            lastAction = MotionEvent.ACTION_MOVE
                            isDragging = true
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        isDragging = false
                    }
                    else -> return false
                }
                return true
            }
        })
    }

    private fun resetPreviouslyTouchedView() {
        currentlyTouchedView?.visibility = View.GONE
        currentlyTouchedView = null
    }

    private fun onClickView(view: View) {
        showDeleteIcon(view)
    }

    private fun showDeleteIcon(view: View) {
//        val deleteIcon = view.findViewById<View>(R.id.deleteIcon)
        deleteIcon.visibility = View.VISIBLE

        deleteIcon.setOnClickListener {
            // Handle delete action
            view.visibility = View.GONE
        }
    }



    private fun setTouchListeners(view: View) {
        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
//                    showOptions(true)
                    Toast.makeText(this,"Text Touched",Toast.LENGTH_LONG).show()
                }
            }
            false
        }

//        deleteIcon.setOnClickListener {
//            // Handle delete action
//            view.visibility = View.GONE
//            showOptions(false)
//        }

//        editIcon.setOnClickListener {
//            // Handle edit action
//            // You can add an edit functionality here, like opening a dialog to edit text
//        }
    }

//    private fun showOptions(show: Boolean) {
//        deleteIcon.visibility = if (show) View.VISIBLE else View.INVISIBLE
//        editIcon.visibility = if (show) View.VISIBLE else View.INVISIBLE
//    }


    private fun initView() {
        val jsonString = """
             {
                "name": "FrameLayout",
                "property": {
                    "android:layout_width": "match_parent",
                    "android:layout_height": "500dp",
                    "android:background": "#FF00FF",
                    "android:paddingLeft": "16dp",
                    "android:paddingTop": "200dp",
                    "android:layout_marginTop": "50dp",
                    "children": [
                        {
                            "name": "TextView",
                            "property": {
                                "android:id": "@+id/textViewName",
                                "android:layout_width": "match_parent",
                                "android:layout_height": "50dp",
                                "android:layout_gravity": "center",
                                "android:gravity": "center",
                                "android:text": "Name",
                                "android:textSize": "20sp",
                                "android:textColor": "#FF000000"
                             }
                         }
                    ]
                }
            }
        """.trimIndent()

        val jsonObject = JSONObject(jsonString)

//        val rootView = createView(this,jsonObject)

//        val jsonObject = JSONObject(jsonString)
////        val rootView = jsonToView(this, jsonObject)
//
//        val viewName = jsonObject.getString("name")
//        val properties = jsonObject.optJSONObject("property")?.toMap() ?: emptyMap()
//
//
//        when (viewName) {
//            "FrameLayout" -> {
//                val frameLayout = FrameLayout(this)
//                ViewProperties.applyFrameLayoutParams(frameLayout, properties)
//                binding.rootLayout.addView(frameLayout)
//            }
//            "TextView" -> {
//                val textView = TextView(this)
//                ViewProperties.applyTextViewProperties(textView, properties)
//                binding.rootLayout.addView(textView)
//            }
//            // Add more view types as needed
//        }

//        binding.rootLayout.addView(rootView)
    }


    private fun createView(context: Context, jsonObject: JSONObject): View {
        val viewName = jsonObject.getString("name")
        val properties = jsonObject.optJSONObject("property")?.toMap() ?: emptyMap()

        val view = when (viewName) {
            "FrameLayout" -> {
                val frameLayout = FrameLayout(context)
                ViewProperties.applyFrameLayoutParams(frameLayout, properties)
                frameLayout
            }
            "TextView" -> {
                val textView = TextView(context)
                ViewProperties.applyTextViewProperties(textView, properties)
                textView
            }
            // Add more view types as needed
            else -> throw IllegalArgumentException("Unsupported view type: $viewName")
        }

        // Handle children
        jsonObject.optJSONArray("children")?.let { childrenArray ->
            for (i in 0 until childrenArray.length()) {
                val childJsonObject = childrenArray.getJSONObject(i)
                val childView = createView(context, childJsonObject)
                if (view is ViewGroup) {
                    view.addView(childView)
                }
            }
        }

        return view
    }


    fun parseXmlLayout(context: Context, xmlLayout: String): View? {
        try {
            // Parse XML layout using DOM parser
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val inputSource = InputSource(StringReader(xmlLayout))
            val document: Document = builder.parse(inputSource)

            // Get the root element of the XML
            val rootElement: Element = document.documentElement

            // Inflate the root element and return the view
            return inflateElement(context, rootElement)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun inflateElement(context: Context, element: Element): View? {
        try {
            // Inflate the element using LayoutInflater
            val inflater = LayoutInflater.from(context)
            return inflater.inflate(getLayoutResourceId(context, element.tagName), null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun getLayoutResourceId(context: Context, tag: String): Int {
        // Replace this with your own mapping of XML tags to layout resources
        return when (tag) {
            "LinearLayout" -> R.layout.layout_linear
            "TextView" -> R.layout.layout_textview
//        "RelativeLayout" -> R.layout.layout_relative
            // Add more mappings as needed
            else -> throw IllegalArgumentException("Unknown tag: $tag")
        }
    }

//fun jsonToView(context: Context, json: JSONObject): View {
//    val viewName = json.getString("name")
//    val properties = json.getJSONObject("property")
//
//    when (viewName) {
//        "FrameLayout" -> {
//        val frameLayout = FrameLayout(this)
//        ViewProperties.applyFrameLayoutParams(frameLayout, properties)
//        rootView.addView(frameLayout)
//    }
//        "TextView" -> {
//        val textView = TextView(this)
//        ViewProperties.applyTextViewProperties(textView, properties)
//        rootView.addView(textView)
//    }
//        // Add more view types as needed
//    }
//    return view
//}


}

