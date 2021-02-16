package com.martinboy.animationview

import android.animation.AnimatorInflater
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout

class LabelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var attrs: AttributeSet? = null
    private var view: View? = null
    private var icon: ImageView? = null
    private var bgLabel: ConstraintLayout? = null
    private var isOpen: Boolean = false

    init {
        this.attrs = attrs
        initView()
    }

    private fun initView() {
        view = (context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.layout_label_view, this, true)
        icon = view?.findViewById(R.id.icon)
        bgLabel = view?.findViewById(R.id.bg_label)

        icon?.setOnClickListener {
            isOpen = if(!isOpen) {
                val objectAnimation =
                    AnimatorInflater.loadAnimator(context, R.animator.animator_translation)
                objectAnimation.setTarget(bgLabel)
                objectAnimation.start()
                true
            } else {
                val objectAnimation =
                    AnimatorInflater.loadAnimator(context, R.animator.animator_translation_2)
                objectAnimation.setTarget(bgLabel)
                objectAnimation.start()
                false
            }
        }
    }

}