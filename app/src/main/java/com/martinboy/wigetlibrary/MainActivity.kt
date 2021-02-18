package com.martinboy.wigetlibrary

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.martinboy.slide_verify_bar.SlideVerifyBar
import com.martinboy.wigetlibrary.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(), SlideVerifyBar.SlideVerifyBarListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.slideVerifyBar.setListener(this)
        initMarqueeView()
    }

    private fun initMarqueeView() {
        binding.textViewMarquee.setBgColorId(android.R.color.holo_red_light)
        val textStringList = listOf("春天不洗澡", "處處蚊子咬", "夜來巴掌聲", "打死知多少", "夏天睡不著", "蚊子吵三小", "巴掌加幹譙", "蚊子跑多少")
        val list = arrayListOf<TextView>()

        textStringList.forEach {
            val textView = TextView(this)
            textView.text = it
            textView.textSize = 30f
            textView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            list.add(textView)
        }
//        binding.textViewMarquee.addSingleViewInQueue(textView)
        binding.textViewMarquee.addViewsInQueue(list, 100)
        binding.textViewMarquee.setScrollDirection(MarqueeView.RIGHT_TO_LEFT)
        binding.textViewMarquee.setScrollSpeed(20)
        binding.textViewMarquee.startScroll()
    }

    override fun successVerify() {
        Toast.makeText(this, "Success Verify", Toast.LENGTH_SHORT).show()
    }

    override fun init() {

    }
}