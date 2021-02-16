package com.martinboy.wigetlibrary

import android.content.res.Resources

class DensityUtil {

    var density: Float

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * @param dpValue 虚拟像素
     * @return 像素
     */
    fun dip2px(dpValue: Float): Int {
        return (0.5f + dpValue * density).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     * @param pxValue 像素
     * @return 虚拟像素
     */
    fun px2dip(pxValue: Int): Float {
        return pxValue / density
    }

    companion object {
        /**
         * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         * @param dpValue 虚拟像素
         * @return 像素
         */
        fun dp2px(dpValue: Float): Int {
            return (0.5f + dpValue * Resources.getSystem()
                .displayMetrics.density).toInt()
        }

        /**
         * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
         * @param pxValue 像素
         * @return 虚拟像素
         */
        fun px2dp(pxValue: Int): Float {
            return pxValue / Resources.getSystem().displayMetrics.density
        }
    }

    init {
        density = Resources.getSystem().displayMetrics.density
    }
}