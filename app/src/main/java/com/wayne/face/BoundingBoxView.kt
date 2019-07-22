package com.wayne.face

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.*

/**
 * Display a square view of the detected face area.
 *
 * @author kuang on 2019/07/22.
 */
class BoundingBoxView(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs),
    SurfaceHolder.Callback {

    protected var mSurfaceHolder: SurfaceHolder

    private val mPaint: Paint

    private var mIsCreated: Boolean = false

    /**
     * 构造器初始化
     */
    init {
        mSurfaceHolder = holder
        mSurfaceHolder.addCallback(this)
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT)
        setZOrderOnTop(true)

        mPaint = Paint()
        mPaint.setAntiAlias(true)
        mPaint.setColor(Color.RED)
        mPaint.setStrokeWidth(5f)
        mPaint.setStyle(Paint.Style.STROKE)
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        mIsCreated = true
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        mIsCreated = false
    }

    /**
     * https://www.jianshu.com/p/51b2e5aa3dd8
     *
     * ?.   
     * kotlin:
     * a?.run()
     *
     * 与 java 相同:
     * if(a != null) {
     *   a.run();
     * }
     *
     * !!.  
     * kotlin:
     * a!!.run()
     * 与 java 相同:
     *
     * if (a != null) {
     *   a.run();
     * } else {
     *    throw new KotlinNullPointException();
     * }
     * ?. 与 !!. 都是 Kotlin 提供的检测空指针的方法
     */
    fun setResults(detRets: Vector<Box>?, scale: Float) {
        if (!mIsCreated) {
            return
        }
        val canvas = mSurfaceHolder.lockCanvas()
        // 清除掉上一次的画框
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        canvas.drawColor(Color.TRANSPARENT)
        try {
            // name?.length ?: 0 仅仅在左边的表达式结果为空时才会计算 ?: 后面的表达式
            val size = detRets?.size ?: 0
            for (i in 0 until size) {
                val rect = detRets?.get(i)?.transform2Rect()
                rect?.left = (rect?.left ?: 0 * scale) as Int
                rect?.right = (rect?.right ?: 0 * scale) as Int
                rect?.top = (rect?.top ?: 0 * scale) as Int
                rect?.bottom = (rect?.bottom ?: 0 * scale) as Int
                canvas.drawRect(rect, mPaint)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val surface = mSurfaceHolder.surface
        // java.lang.IllegalArgumentException: Surface had no valid native Surface.
        if (surface != null && surface.isValid) {
            try {
                mSurfaceHolder.unlockCanvasAndPost(canvas)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
