package com.martinboy.wigetlibrary

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.*
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

class MarqueeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr), Runnable {

    private var windowManager: WindowManager? = null
    private var outSideViewWidth: Int? = 0
    private var viewMargin = 0 //View間距
    private var viewWidth = 0 //View寬度
    private var scrollSpeed = 5 //滾動速度
    private var scrollDirection = LEFT_TO_RIGHT //滾動方向
    private var currentX = 0 //當前x座標
    private var mainLayout: LinearLayout? = null
    private var isScrolling = false
    private var isOnlySingleView = false
    private var checkItemIsViewType = true

    companion object {
        const val LEFT_TO_RIGHT = 1
        const val RIGHT_TO_LEFT = 2
    }

    init {
        initView()
    }

    private fun initView() {
        this.setBackgroundColor(ContextCompat.getColor(this.context, android.R.color.black))
        isFillViewport = true
        foregroundGravity = Gravity.CENTER
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        val displayMetrics = DisplayMetrics()
        windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        outSideViewWidth = displayMetrics.widthPixels
        mainLayout = LayoutInflater.from(context)
            .inflate(R.layout.layout_marquee_view, this, false) as LinearLayout?
        this.addView(mainLayout)
    }

    /**
     * 用於只增加單一View情況
     */
    fun addSingleViewInQueue(view: View) {
        checkItemIsViewType = true
        isOnlySingleView = true
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(viewMargin, 0, 0, 0)
        view.layoutParams = lp
        mainLayout!!.addView(view)
        view.measure(0, 0) //測量view
        viewWidth += view.measuredWidth + viewMargin
    }

    /**
     * 用於增加多個View情況
     */
    fun addViewsInQueue(views: List<TextView>, viewMargin: Int) {
        if (isOnlySingleView)
            return

        this.viewMargin = viewMargin

        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(viewMargin, 0, 0, 0)

        for (i in views.indices) {

            if (views[i] is View) {
                (views[i] as View).layoutParams = lp
                mainLayout!!.addView(views[i] as View)
                (views[i] as View).measure(0, 0) //測量view
                viewWidth += (views[i] as View).measuredWidth + viewMargin
            } else {
                checkItemIsViewType = false
                break
            }

        }

        if (!checkItemIsViewType)
            return

    }

    /**
     * 啟動跑馬燈
     */
    fun startScroll() {
        if (!checkItemIsViewType) {
            return
        }
        isScrolling = true
        removeCallbacks(this)
        post {
            outSideViewWidth = this.measuredWidth
            currentX = if (scrollDirection == LEFT_TO_RIGHT) viewWidth else -outSideViewWidth!!
            post(this)
        }
    }

    /**
     * 停止跑馬燈
     */
    fun stopScroll() {
        isScrolling = false
        removeCallbacks(this)
    }

    /**
     * 確認是否正在滾動
     */
    fun isMarqueeViewScrolling(): Boolean {
        return isScrolling
    }

    /**
     * 設定滾動速度
     */
    fun setScrollSpeed(scrollSpeed: Int) {
        this.scrollSpeed = scrollSpeed
    }

    /**
     * 設定滾動方向 預設從左向右
     */
    fun setScrollDirection(scrollDirection: Int) {
        this.scrollDirection = scrollDirection
    }

    /**
     * 設定背景顏色
     */
    fun setBgColorId(color: Int){
        this.setBackgroundColor(ContextCompat.getColor(this.context, color))
    }

    override fun run() {
        when (scrollDirection) {
            LEFT_TO_RIGHT -> {
                mainLayout!!.scrollTo(currentX, 0)
                currentX--
                if (-currentX >= outSideViewWidth!!) {
                    mainLayout!!.scrollTo(viewWidth, 0)
                    currentX = viewWidth
                }
            }
            RIGHT_TO_LEFT -> {
                mainLayout!!.scrollTo(currentX, 0)
                currentX++
                if (currentX >= viewWidth) {
                    mainLayout!!.scrollTo(-outSideViewWidth!!, 0)
                    currentX = -outSideViewWidth!!
                }
            }
            else -> {
            }
        }
        postDelayed(this, 50 / scrollSpeed.toLong())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}