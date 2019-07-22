package com.wayne.face

import android.graphics.*
import android.util.Log
import java.util.*

/**
 * 工具类.
 *
 * @author kuang on 2019/07/22.
 */
object Utils {

    /**
     * 复制图片，并设置 isMutable=true
     */
    fun copyBitmap(bitmap: Bitmap): Bitmap {
        return bitmap.copy(bitmap.config, true)
    }

    /**
     * 在 bitmap 中画矩形
     */
    fun drawRect(bitmap: Bitmap, rect: Rect) {
        try {
            val canvas = Canvas(bitmap)
            val paint = Paint()
            val r = 255//(int)(Math.random()*255);
            val g = 0//(int)(Math.random()*255);
            val b = 0//(int)(Math.random()*255);
            paint.setColor(Color.rgb(r, g, b))
            // 转换成 float
            paint.setStrokeWidth((1 + bitmap.width / 500).toFloat())
            paint.setStyle(Paint.Style.STROKE)
            canvas.drawRect(rect, paint)
        } catch (e: Exception) {
            Log.i("Utils", "[*] error$e")
        }
    }

    /**
     * 在图中画点
     */
    fun drawPoints(bitmap: Bitmap, landmark: Array<Point>) {
        for (i in landmark.indices) {
            val x = landmark[i].x
            val y = landmark[i].y
            // Log.i("Utils","[*] landmarkd "+x+ "  "+y);
            drawRect(bitmap, Rect(x - 1, y - 1, x + 1, y + 1))
        }
    }

    /**
     * Flip alone diagonal
     *
     * 对角线翻转。data 大小原先为 h*w*stride，翻转后变成 w*h*stride
     */
    fun flip_diag(data: FloatArray, h: Int, w: Int, stride: Int) {
        val tmp = FloatArray(w * h * stride)
        for (i in 0 until w * h * stride) {
            tmp[i] = data[i]
        }
        for (y in 0 until h) {
            for (x in 0 until w) {
                for (z in 0 until stride) {
                    data[(x * h + y) * stride + z] = tmp[(y * w + x) * stride + z]
                }
            }
        }
    }

    /**
     * src 转为二维存放到 dst 中
     */
    fun expand(src: FloatArray, dst: Array<FloatArray>) {
        var idx = 0
        for (y in dst.indices) {
            for (x in 0 until dst[0].size) {
                dst[y][x] = src[idx++]
            }
        }
    }

    /**
     * src 转为三维存放到 dst 中
     */
    fun expand(src: FloatArray, dst: Array<Array<FloatArray>>) {
        var idx = 0
        for (y in dst.indices) {
            for (x in 0 until dst[0].size) {
                for (c in 0 until dst[0][0].size) {
                    dst[y][x][c] = src[idx++]
                }
            }
        }

    }

    /**
     * dst=src[:,:,1]
     */
    fun expandProb(src: FloatArray, dst: Array<FloatArray>) {
        var idx = 0
        for (y in dst.indices) {
            for (x in 0 until dst[0].size) {
                dst[y][x] = src[idx++ * 2 + 1]
            }
        }
    }

    /**
     * box 转化为 rect
     */
    fun boxes2rects(boxes: Vector<Box>?): Array<Rect?> {
        var cnt = 0
        for (i in 0 until (boxes?.size ?: 0)) {
            if (!((boxes?.get(i)?.deleted) ?: false))
                cnt++
        }
        val r = arrayOfNulls<Rect>(cnt)
        var idx = 0
        for (i in 0 until (boxes?.size ?: 0)) {
            if (!((boxes?.get(i)?.deleted) ?: false))
                r[idx++] = boxes?.get(i)?.transform2Rect()
        }
        return r
    }

    /**
     * 删除做了 delete 标记的 box
     */
    fun updateBoxes(boxes: Vector<Box>?): Vector<Box> {
        val b = Vector<Box>()
        for (i in 0 until (boxes?.size ?: 0)) {
            if (!((boxes?.get(i)?.deleted) ?: false))
                b.addElement(boxes?.get(i))
        }
        return b
    }

    fun showPixel(v: Int) {
        Log.i("MainActivity", "[*]Pixel:R" + (v shr 16 and 0xff) + "G:" + (v shr 8 and 0xff) + " B:" + (v and 0xff))
    }
}