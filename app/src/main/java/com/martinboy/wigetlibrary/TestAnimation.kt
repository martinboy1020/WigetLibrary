package com.martinboy.wigetlibrary

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import kotlin.math.abs

class TestAnimation @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var attrs: AttributeSet? = null
    private var mPath: Path = Path()
    private var mBackPaint: Paint = Paint()
    private var mFrontPaint: Paint = Paint()
    private var mOuterPaint: Paint = Paint()
    private var mWaveHeight = 0f
    private var mHeadHeight = 0f
    private var mSpringRatio = 0f
    private val mFinishRatio = 0f

    //    private val mState: RefreshState? = null
    private var mBollY = 0f //弹出球体的Y坐标
    private var mShowBoll = true //是否显示中心球体
    private var mShowBollTail = false //是否显示球体拖拽的尾巴
    private var mShowOuter = false
    private var mBollRadius = 0f //球体半径

    private var mRefreshStop = 90
    private var mRefreshStart = 90
    private var mOuterIsStart = true

    private val TARGET_DEGREE = 270

    var drawable: Drawable? = null
    private var angle = 0

    init {
        this.attrs = attrs
        initView(context, attrs)
    }

    private fun initView(
        context: Context,
        attrs: AttributeSet?
    ) {
        minimumHeight = DensityUtil.dp2px(100f)
        mBackPaint.color = -0xee4401
        mBackPaint.isAntiAlias = true
        mFrontPaint.color = -0x1
        mFrontPaint.isAntiAlias = true
        mOuterPaint.isAntiAlias = true
        mOuterPaint.color = -0x1
        mOuterPaint.style = Paint.Style.STROKE
        mOuterPaint.strokeWidth = DensityUtil.dp2px(2f).toFloat()
        drawable = resources.getDrawable(R.drawable.boll_loading)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            resolveSize(suggestedMinimumWidth, widthMeasureSpec),
            resolveSize(suggestedMinimumHeight, heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val viewWidth = width
        val viewHeight = height
        if (canvas != null) {
//            drawWave(canvas, viewWidth, viewHeight)
//            drawSpringUp(canvas, viewWidth)
//            drawBoll(canvas, viewWidth)
//            drawOuter(canvas, viewWidth)
//            drawFinish(canvas, viewWidth)
            drawBoll(canvas, viewWidth)
        }
    }

    private fun drawWave(
        canvas: Canvas,
        viewWidth: Int,
        viewHeight: Int
    ) {
        val baseHeight = mHeadHeight.coerceAtMost(viewHeight.toFloat())
        if (mWaveHeight != 0f) {
            mPath.reset()
            mPath.lineTo(viewWidth.toFloat(), 0f)
            mPath.lineTo(viewWidth.toFloat(), baseHeight)
            mPath.quadTo(viewWidth / 2.toFloat(), baseHeight + mWaveHeight * 2, 0f, baseHeight)
            mPath.close()
            canvas.drawPath(mPath, mBackPaint)
        } else {
            canvas.drawRect(0f, 0f, viewWidth.toFloat(), baseHeight, mBackPaint)
        }
    }

    private fun drawSpringUp(canvas: Canvas, viewWidth: Int) {
        if (mSpringRatio > 0) {
            val leftX =
                viewWidth / 2 - 4 * mBollRadius + mSpringRatio * 3 * mBollRadius
            if (mSpringRatio < 0.9) {
                mPath.reset()
                mPath.moveTo(leftX, mBollY)
                mPath.quadTo(
                    viewWidth / 2.toFloat(), mBollY - mBollRadius * mSpringRatio * 2,
                    viewWidth - leftX, mBollY
                )
                canvas.drawPath(mPath, mFrontPaint)
            } else {
                canvas.drawCircle(viewWidth / 2.toFloat(), mBollY, mBollRadius, mFrontPaint)
            }
        }
    }

    private fun drawBoll(canvas: Canvas, viewWidth: Int) {
        Log.d("tag123456789", "drawBoll mBollRadius: $mBollRadius")
        if (mShowBoll) {
            //canvas.drawCircle(viewWidth / 2, mBollY, mBollRadius, mFrontPaint);
            drawable!!.setBounds(
                (viewWidth / 2 - mBollRadius).toInt(),
                (mBollY - mBollRadius).toInt(),
                (viewWidth / 2 + mBollRadius).toInt(),
                (mBollY + mBollRadius).toInt()
            )
            drawable!!.draw(canvas)
            drawBollTail(canvas, viewWidth, (mHeadHeight + mWaveHeight) / mHeadHeight)
        }
    }

    private fun drawBollTail(
        canvas: Canvas,
        viewWidth: Int,
        fraction: Float
    ) {
        if (mShowBollTail) {
            val bottom = mHeadHeight + mWaveHeight
            val starty = mBollY + mBollRadius * fraction / 2
            val startx =
                viewWidth / 2 + Math.sqrt(mBollRadius * mBollRadius * (1 - fraction * fraction / 4).toDouble())
                    .toFloat()
            val bezier1x = viewWidth / 2 + mBollRadius * 3 / 4 * (1 - fraction)
            val bezier2x = bezier1x + mBollRadius
            mPath.reset()
            mPath.moveTo(startx, starty)
            mPath.quadTo(bezier1x, bottom, bezier2x, bottom)
            mPath.lineTo(viewWidth - bezier2x, bottom)
            mPath.quadTo(viewWidth - bezier1x, bottom, viewWidth - startx, starty)
            canvas.drawPath(mPath, mFrontPaint)
        }
    }

    private fun drawOuter(canvas: Canvas, viewWidth: Int) {
        if (mShowOuter) {
            val outerR = mBollRadius + mOuterPaint.strokeWidth * 2
            mRefreshStart += if (mOuterIsStart) 3 else 10
            mRefreshStop += if (mOuterIsStart) 10 else 3
            mRefreshStart = mRefreshStart % 360
            mRefreshStop = mRefreshStop % 360
            var swipe = mRefreshStop - mRefreshStart
            swipe = if (swipe < 0) swipe + 360 else swipe
            canvas.drawArc(
                RectF(
                    viewWidth / 2 - outerR,
                    mBollY - outerR,
                    viewWidth / 2 + outerR,
                    mBollY + outerR
                ),
                mRefreshStart.toFloat(), swipe.toFloat(), false, mOuterPaint
            )
            if (swipe >= TARGET_DEGREE) {
                mOuterIsStart = false
            } else if (swipe <= 10) {
                mOuterIsStart = true
            }
            angle += if (mOuterIsStart) 3 else 5
            canvas.save()
            canvas.rotate(angle.toFloat(), viewWidth / 2.toFloat(), mBollY)
            drawable!!.setBounds(
                (viewWidth / 2 - mBollRadius).toInt(),
                (mBollY - mBollRadius).toInt(),
                (viewWidth / 2 + mBollRadius).toInt(),
                (mBollY + mBollRadius).toInt()
            )
            drawable!!.draw(canvas)
            canvas.restore()
            if (angle == 360) angle = 0
            invalidate()
        }
    }

    private fun drawFinish(canvas: Canvas, viewWidth: Int) {
        if (mFinishRatio > 0) {
            val beforeColor = mOuterPaint.color
            if (mFinishRatio < 0.3) {
                // canvas.drawCircle(viewWidth / 2, mBollY, mBollRadius, mFrontPaint);
                canvas.save()
                canvas.rotate(angle.toFloat(), viewWidth / 2.toFloat(), mBollY)
                drawable!!.setBounds(
                    (viewWidth / 2 - mBollRadius).toInt(),
                    (mBollY - mBollRadius).toInt(),
                    (viewWidth / 2 + mBollRadius).toInt(),
                    (mBollY + mBollRadius).toInt()
                )
                drawable!!.draw(canvas)
                canvas.restore()
                val outerR =
                    (mBollRadius + mOuterPaint.strokeWidth * 2 * (1 + mFinishRatio / 0.3f)).toInt()
                val afterColor = Color.argb(
                    (0xff * (1 - mFinishRatio / 0.3f)).toInt(),
                    Color.red(beforeColor),
                    Color.green(beforeColor),
                    Color.blue(beforeColor)
                )
                mOuterPaint.color = afterColor
                canvas.drawArc(
                    RectF(
                        (viewWidth / 2 - outerR).toFloat(),
                        mBollY - outerR,
                        (viewWidth / 2 + outerR).toFloat(),
                        mBollY + outerR
                    ), 0f, 360f, false, mOuterPaint
                )
            }
            mOuterPaint.color = beforeColor
            if (mFinishRatio in 0.7..1.0) {
                val fraction = (mFinishRatio - 0.7f) / 0.3f
                val leftX =
                    (viewWidth / 2 - mBollRadius - 2 * mBollRadius * fraction).toInt()
                mPath.reset()
                mPath.moveTo(leftX.toFloat(), mHeadHeight)
                mPath.quadTo(
                    viewWidth / 2.toFloat(), mHeadHeight - mBollRadius * (1 - fraction),
                    viewWidth - leftX.toFloat(), mHeadHeight
                )
                canvas.drawPath(mPath, mFrontPaint)
            }
        }
    }

    fun startAnim() {
        mHeadHeight = height.toFloat()
        mBollRadius = height / 6.toFloat()
        val interpolator =
            DecelerateInterpolator()
        val reboundHeight = Math.min(mWaveHeight * 0.8f, mHeadHeight / 2)
        val waveAnimator = ValueAnimator.ofFloat(
            mWaveHeight, 0f,
            -(reboundHeight * 1.0f), 0f,
            -(reboundHeight * 0.4f), 0f
        )
        waveAnimator.addUpdateListener(object : AnimatorUpdateListener {
            var speed = 0f
            var springBollY = 0f
            var springRatio = 0f
            var springstatus = 0 //0 还没开始弹起 1 向上弹起 2 在弹起的最高点停住
            override fun onAnimationUpdate(animation: ValueAnimator) {
                Log.d("tag123456789", "onAnimationUpdate springstatus: $springstatus")
                val curValue = animation.animatedValue as Float
                if (springstatus == 0 && curValue <= 0) {
                    springstatus = 1
                    speed = abs(curValue - mWaveHeight)
                }
                if (springstatus == 1) {
                    springRatio = -curValue / reboundHeight
                    if (springRatio >= mSpringRatio) {
                        mSpringRatio = springRatio
                        mBollY = mHeadHeight + curValue
                        speed = Math.abs(curValue - mWaveHeight)
                    } else {
                        springstatus = 2
                        mSpringRatio = 0f
                        mShowBoll = true
                        mShowBollTail = true
                        springBollY = mBollY
                    }
                }
                if (springstatus == 2) {
                    if (mBollY > mHeadHeight / 2) {
                        mBollY = Math.max(mHeadHeight / 2, mBollY - speed)
                        val bolly =
                            animation.animatedFraction * (mHeadHeight / 2 - springBollY) + springBollY
                        if (mBollY > bolly) {
                            mBollY = bolly
                        }
                    }
                }
                if (mShowBollTail && curValue < mWaveHeight) {
                    mShowOuter = true
                    mShowBollTail = false
                    mOuterIsStart = true
                    mRefreshStart = 90
                    mRefreshStop = 90
                }
                mWaveHeight = curValue
                invalidate()
            }
        })
        waveAnimator.interpolator = interpolator
        waveAnimator.duration = 1000
        waveAnimator.start()

    }

}