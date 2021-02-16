package com.martinboy.wigetlibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.martinboy.slide_verify_bar.SlideVerifyBar

class MainActivity : AppCompatActivity(), SlideVerifyBar.SlideVerifyBarListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val slideVerifyBar = findViewById<SlideVerifyBar>(R.id.slide_verify_bar)
        slideVerifyBar.setListener(this)

//        val testAnimation = findViewById<TestAnimation>(R.id.test_animation)
//        testAnimation.startAnim()
    }

    override fun successVerify() {
        Toast.makeText(this, "Success Verify", Toast.LENGTH_SHORT).show()
    }
}