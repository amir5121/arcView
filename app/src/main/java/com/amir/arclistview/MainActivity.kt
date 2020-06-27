package com.amir.arclistview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amir.arcview.ArcLinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_arc_button.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG: String = "MainActivity"
    private lateinit var strokeArc: ArcLinearLayout
    private lateinit var shadowArc: ArcLinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        kick_me.setOnClickListener(this)
        kick_swapped.setOnClickListener(this)
        include_buttons_stroke.setOnClickListener(this)
        include_buttons_shadow.setOnClickListener(this)
        strokeArc =
            layoutInflater.inflate(
                R.layout.stroke_arc_linear_layout,
                include_arc_buttons_temp_arc,
                false
            ) as ArcLinearLayout
        shadowArc =
            layoutInflater.inflate(
                R.layout.shadow_arc_linear_layout,
                include_arc_buttons_temp_arc,
                false
            ) as ArcLinearLayout
    }

    override fun onClick(v: View?) {
        when (v) {
            kick_me -> {
                if (include_buttons_scroll_view.isKnockedIn) {
                    include_buttons_scroll_view.knockout()
                } else {
                    include_buttons_scroll_view.knockIn()
                }
            }
            kick_swapped -> {
                Log.wtf(TAG, "onClick: swapped")
                include_arc_buttons_temp_arc.swapView(null)
            }
            include_buttons_shadow -> {
                include_arc_buttons_temp_arc.swapView(shadowArc)
            }
            include_buttons_stroke -> {
                include_arc_buttons_temp_arc.swapView(strokeArc)
            }
        }
    }
}