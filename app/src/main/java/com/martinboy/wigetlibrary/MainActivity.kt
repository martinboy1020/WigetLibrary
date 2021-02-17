package com.martinboy.wigetlibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.martinboy.slide_verify_bar.SlideVerifyBar

class MainActivity : AppCompatActivity(), SlideVerifyBar.SlideVerifyBarListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val slideVerifyBar = findViewById<SlideVerifyBar>(R.id.slide_verify_bar)
        slideVerifyBar.setListener(this)
        initMarqueeView()
//        val testAnimation = findViewById<TestAnimation>(R.id.test_animation)
//        testAnimation.startAnim()
    }

    private fun initMarqueeView() {

        val textViewMarquee: MarqueeView = findViewById(R.id.text_view_marquee)
        textViewMarquee.setBgColorId(android.R.color.holo_red_light)
        val textStringList = listOf("春天不洗澡", "處處蚊子咬", "夜來巴掌聲", "打死知多少", "夏天睡不著", "蚊子吵三小", "巴掌加幹譙", "蚊子跑多少")
        val list = arrayListOf<TextView>()

        textStringList.forEach {
            val textView = TextView(this)
            textView.text = it
            textView.textSize = 30f
            textView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            list.add(textView)
        }
        //        binding!!.viewMarquee.addSingleViewInQueue(textView)
        textViewMarquee.addViewsInQueue(list, 100)
        textViewMarquee.setScrollDirection(MarqueeView.RIGHT_TO_LEFT)
        textViewMarquee.setScrollSpeed(20)
        textViewMarquee.startScroll()
    }

    override fun successVerify() {
        Toast.makeText(this, "Success Verify", Toast.LENGTH_SHORT).show()
    }
}