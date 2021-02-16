package com.martinboy.slide_verify_bar

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener

class SlideVerifyBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var attrs: AttributeSet? = null
    private var view: View? = null
    private var seekBar: SeekBar? = null
    private var listener: SlideVerifyBarListener? = null

    init {
        this.attrs = attrs
        initView()
    }

    interface SlideVerifyBarListener {
        fun successVerify()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        view = (context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.slide_verify_bar, this, true)
        seekBar = view?.findViewById(R.id.sb_verify)

        if (seekBar != null) {
            seekBar?.setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP || motionEvent.action == MotionEvent.ACTION_CANCEL) {
                    if (seekBar!!.progress > 0 && seekBar!!.progress < seekBar!!.max) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            seekBar?.setProgress(0, true)
                        } else {
                            seekBar?.progress = 0
                        }
                    }
                }
                motionEvent.x > seekBar?.thumb!!.bounds
                    .width() + seekBar?.thumb!!
                    .bounds.left + 150 && seekBar!!.thumb
                    .bounds.left == 0
            }

            seekBar?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                    if (i == seekBar.max) {
                        listener?.successVerify()
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    if (seekBar.progress > 0 && seekBar.progress < seekBar.max) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            seekBar.setProgress(0, true)
                        } else {
                            seekBar.progress = 0
                        }
                    }
                }
            })
        }

    }

    fun setListener(listener: SlideVerifyBarListener) {
        this.listener = listener
    }

}