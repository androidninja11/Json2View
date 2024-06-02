package com.androidninja.json2viewdemo

import ViewProperties
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
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
import com.androidninja.json2viewdemo.dynamiclayoutinflator.DynamicLayoutInflator.DynamicLayoutInfo
import com.androidninja.json2viewdemo.dynamiclayoutinflator.TouchedViewListener
import com.sddemo.json2viewdemo.R
import com.sddemo.json2viewdemo.databinding.ActivityMain1Binding
import org.json.JSONObject
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.InputSource
import timber.log.Timber
import java.io.IOException
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.math.abs


class MainActivity1 : AppCompatActivity() {
    private lateinit var binding: ActivityMain1Binding
    private var isDragging = false
    private lateinit var deleteIcon : ImageView
    private lateinit var deleteIcon1 : ImageView
    private var currentlyTouchedView: View? = null
    private var touchedViewListener: TouchedViewListener? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMain1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        touchedViewListener = object : TouchedViewListener {
            override fun onTouchedViewChanged(viewId: Int) {
                // Handle the touched view change here

                Timber.d("View touched: $viewId")
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        initView()
        dynamicLayoutView()

        // Set touch listener for the parent layout
//        binding.rootLayout.setOnTouchListener { _, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    Timber.d("Parent Touch ACTION_DOWN:")
//                    val touchedView = findTouchedView(binding.rootLayout, event.rawX, event.rawY)
//                    if (touchedView != null) {
//                        val viewId = touchedView.id
//                        resetPreviouslyTouchedView()
//                        currentlyTouchedView = touchedView
////                        printIdOfTouchedView(touchedView)
////                        showDeleteIcon(touchedView)
//                    }
//                }
//                MotionEvent.ACTION_UP -> {
//                    Timber.d("Parent Touch ACTION_UP:")
//                    // Do nothing for now
//                }
//            }
//            true
//        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun dynamicLayoutView() {
        try {

            val jsonString = """
             <?xml version="1.0" encoding="utf-8"?>
             <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
                 android:layout_width="match_parent"
                 android:layout_height="wrap_content">

                 <ImageView
                     android:id="@+id/image1"
                     android:layout_gravity="center"
                     android:visibility="visible"
                     android:scaleType="centerCrop"
                     android:layout_width="wrap_content"
                     android:layout_height="300dp"
                     android:src="@drawable/sample_image"/>
                 
                 <RelativeLayout
                     android:id="@+id/rlName"
                     android:layout_width="wrap_content"
                     android:layout_height="wrap_content">

                     <!-- Centered TextView -->
                     <TextView
                         android:id="@+id/textName"
                         android:layout_width="wrap_content"
                         android:layout_height="wrap_content"
                         android:paddingTop="25dp"
                         android:paddingBottom="25dp"
                         android:paddingLeft="30dp"
                         android:paddingRight="30dp"
                         android:layout_centerInParent="true"
                         android:text="Hello, World!"
                         android:textStyle="bold"
                         android:textSize="16sp"
                         android:textColor="#FFFFFF" />

                     <!-- Top-left ImageView -->
                     <ImageView
                         android:id="@+id/top_left_image"
                         android:layout_width="50px"
                         android:layout_height="50px"
                         android:layout_alignTop="@id/textName"
                         android:layout_toStartOf="@id/textName"
                         android:src="@drawable/ic_delete"
                         android:contentDescription="Top Left Image"
                         android:layout_marginEnd="5dp"
                         android:layout_marginBottom="5dp" />

                     <!-- Top-right ImageView -->
                     <ImageView
                         android:id="@+id/top_right_image"
                         android:layout_width="20dp"
                         android:layout_height="20dp"
                         android:layout_alignTop="@id/textName"
                         android:layout_toEndOf="@id/textName"
                         android:src="@drawable/ic_delete"
                         android:contentDescription="Top Right Image"
                         android:layout_marginStart="5dp"
                         android:layout_marginBottom="5dp" />

                     <!-- Bottom-left ImageView -->
                     <ImageView
                         android:id="@+id/bottom_left_image"
                         android:layout_width="20dp"
                         android:layout_height="20dp"
                         android:layout_alignBottom="@id/textName"
                         android:layout_toStartOf="@id/textName"
                         android:src="@drawable/ic_delete"
                         android:contentDescription="Bottom Left Image"
                         android:layout_marginEnd="5dp"
                         android:layout_marginTop="5dp" />

                     <!-- Bottom-right ImageView -->
                     <ImageView
                         android:id="@+id/bottom_right_image"
                         android:layout_width="20dp"
                         android:layout_height="20dp"
                         android:layout_alignBottom="@id/textName"
                         android:layout_toEndOf="@id/textName"
                         android:src="@drawable/ic_delete"
                         android:contentDescription="Bottom Right Image"
                         android:layout_marginStart="5dp"
                         android:layout_marginTop="5dp" />

                 </RelativeLayout>
             </RelativeLayout>
        """.trimIndent()

//            val view: View = DynamicLayoutInflator.inflate(this, assets.open("testlayout.xml"), binding.rootLayout)
            val view: View = DynamicLayoutInflator.inflate(this, jsonString,binding.rootLayout)
            setTouchListenerForView(view);
            DynamicLayoutInflator.setDelegate(view, this)
            // Set the MainActivity as the TouchedViewListener

            // Attach a touch listener to the inflated view if it's not null
//            view.setOnTouchListener(OnTouchListener { v, event ->
//                // Retrieve the ID of the touched view
//                Timber.d("On Touching: ${v.getId()}")
//                findTouchedView(binding.rootLayout, event.rawX, event.rawY)
//                // Do something with the touchedViewId
//                false // Return false to pass the event to other listeners
//            })


            val countChild = binding.rootLayout.childCount
            val child = binding.rootLayout.getChildAt(0)
            val name = DynamicLayoutInfo().nameToIdNumber.get(child.id.toString())
            Timber.d("Checking view Count onCreate: $countChild , Child : $child , name :$name ")
            Timber.d("Checking view Count Child: ${DynamicLayoutInflator.idNumFromIdString(child, child.id.toString())}")


//            val rootFromCreate = DynamicLayoutInflator.getDynamicLayoutInfo(binding.rootLayout)
//            Timber.d("rootFromCreate Child: $rootFromCreate")
//            val movableTextViewContainer = DynamicLayoutInflator.findViewByIdString(view,"movableTextViewContainer") as RelativeLayout?
//            val movableTextViewContainer1 = DynamicLayoutInflator.findViewByIdString(view,"movableTextViewContainer1") as RelativeLayout?
//            val someTextView = DynamicLayoutInflator.findViewByIdString(view,"textViewName") as TextView?
//            val someTextView1 = DynamicLayoutInflator.findViewByIdString(view,"textViewName1") as TextView?
//            val someImageView = DynamicLayoutInflator.findViewByIdString(view,"image1") as ImageView?
//            deleteIcon = DynamicLayoutInflator.findViewByIdString(view,"deleteIcon") as ImageView
//            deleteIcon = DynamicLayoutInflator.findViewByIdString(view,"deleteIcon1") as ImageView
//            someTextView?.text = "After Update new text Arvind"
////            someImageView?.setImageResource(R.drawable.psd)
//
//            rootFromCreate?.let {
//                setMovable(it)
//                setTouchListeners(it)}
//            movableTextViewContainer?.let { setMovable(it) }
//            movableTextViewContainer1?.let { setMovable(it) }
//            someTextView?.let { setMovable(it) }
//            someTextView1?.let { setMovable(it) }
//            someImageView?.let {
//                setMovable(it)
////                setTouchListeners(it)
//            }

            // Set touch listener for the parent layout to detect touches outside the views
//            binding.rootLayout.setOnTouchListener { _, event ->
//                if (event.action == MotionEvent.ACTION_DOWN) {
//                    Timber.d("Parent Touch ACTION_DOWN:")
//                }
//                false
//            }


//            binding.rootLayout.setOnTouchListener { v, event ->
//                if (event.action == MotionEvent.ACTION_DOWN) {
//                    for (i in 0 until binding.rootLayout.childCount) {
//                        val child = binding.rootLayout.getChildAt(i)
//                        // Check if the touch event is inside the child view
//                        if (isTouchInsideView(event, child)) {
//                            // Handle the touch event on the child view
//                            when (child.id) {
////                                R.id.textViewName -> {
////                                    // Do something for dynamicTextView1
////                                    println("Dynamic TextView 1 was touched")
////                                }
////                                R.id.dynamicButton1 -> {
////                                    // Do something for dynamicButton1
////                                    println("Dynamic Button 1 was touched")
////                                }
//                            }
//                        }
//                    }
//                }
//                true
//            }


        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    private fun setTouchListenerForView(view: View) {
        view.setOnTouchListener { v, event ->
            if (touchedViewListener != null) {
                resetPreviouslyTouchedView()
                currentlyTouchedView = v
                val textTouched = DynamicLayoutInflator.findViewByIdString(v,v.id.toString())
                if(v is TextView){
                    Toast.makeText(this,"${v.text}",Toast.LENGTH_LONG).show()
                }

                Timber.tag("setTouchListenerForView").d("Id :${v.id} , View: ${v} ,ParentClickable: ${v.isClickable} ,Parent:${v.parent} " )
                touchedViewListener!!.onTouchedViewChanged(v.id)
            }
            false // return false so that the event can propagate
        }
        if (view is ViewGroup) {
            val viewGroup = view
            if(viewGroup is RelativeLayout){
                setMovable(viewGroup)
            }

            for (i in 0 until viewGroup.childCount) {
                val child = viewGroup.getChildAt(i)
                Timber.d("IsChildClickable : ${child.isClickable}")
                // Set touch listener recursively for child views
                    setTouchListenerForView(child)
            }
        }
    }

    private fun getParentViewGroup(view: View): ViewGroup? {
        return view.parent as? ViewGroup
    }



    private fun printIdOfTouchedView(touchedView: View) {
        // Print the ID of the touched view
        val viewId = touchedView.id
        Timber.d("view ID: $viewId")
        if (viewId != View.NO_ID) {
            val resourceName = resources.getResourceName(viewId)
            println("Touched view ID: $resourceName")
            Timber.d("Touched view ID: $resourceName")
        } else {
            Timber.d("Touched view has no ID")
            println("Touched view has no ID")
        }
    }

    private fun findTouchedView(viewGroup: ViewGroup, x: Float, y: Float): View? {
        Timber.d("Checking view Count: ${viewGroup.childCount}")

        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)

            if (child is ViewGroup) {
                val touchedView = findTouchedView(child, x, y)
                if (touchedView != null) return touchedView
            } else if (isViewContains(child, x, y)) {
                val noToId = DynamicLayoutInflator.findViewByIdString(child,"textViewName")
                Timber.d("Else ChildId : ${noToId}")

                if (child.id != View.NO_ID) {
                    try {
                        Timber.d("Checking view: ${resources.getResourceEntryName(child.id)}")
                        println("Checking view: ${resources.getResourceEntryName(child.id)}")
                    } catch (e: Resources.NotFoundException) {
                        Timber.d("Checking view with invalid ID Catch : ${e.message},${DynamicLayoutInfo().nameToIdNumber}")
                        DynamicLayoutInfo().nameToIdNumber.forEach {
                            Timber.d("invalid ID Catch Loop : ${it.key} , ${it.value}")
                        }
                    }
                } else {
                    Timber.d("Checking view with no ID")
                }
                return child
            }
        }
        return null
    }

    private fun isViewContains(view: View, x: Float, y: Float): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val viewX = location[0]
        val viewY = location[1]

        val width = view.width
        val height = view.height

        return !(x < viewX || x > viewX + width || y < viewY || y > viewY + height)
    }


    private fun isTouchInsideView(event: MotionEvent, view: View): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val x = location[0]
        val y = location[1]
        return event.rawX >= x && event.rawX <= x + view.width && event.rawY >= y && event.rawY <= y + view.height
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
                            Toast.makeText(this@MainActivity1,"Text Touched",Toast.LENGTH_LONG).show()

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
                            Toast.makeText(this@MainActivity1,"Click Action",Toast.LENGTH_LONG).show()
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
//                        showDeleteIcon(v)
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
//        currentlyTouchedView?.visibility = View.GONE
        currentlyTouchedView = null
    }

    private fun onClickView(view: View) {
        showDeleteIcon(view)
    }

    private fun showDeleteIcon(view: View) {
        val viewId = view.id
        Timber.d("showDeleteIcon view ID: $viewId")
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




