package com.wayne.face

import android.graphics.Point
import android.graphics.Rect

import java.lang.Math.max

/**
 * Box 实体类
 *
 * internal constructor() 相当于：
 * Box() {
 * }
 *
 * @author kuang on 2019/07/19.
 */
internal class Box internal constructor() {

    /**
     * left:box[0], top:box[1], right:box[2], bottom:box[3]
     */
    var box: IntArray
    /**
     * probability
     */
    var score: Float = 0.toFloat()
    /**
     * bounding box regression
     */
    var bbr: FloatArray
    var deleted: Boolean
    /**
     * facial landmark.只有 ONet 输出 Landmark
     */
    var landmark: Array<Point?>

    init {
        box = IntArray(4)
        bbr = FloatArray(4)
        deleted = false
        landmark = arrayOfNulls(5)
    }

    fun left(): Int {
        return box[0]
    }

    fun right(): Int {
        return box[2]
    }

    fun top(): Int {
        return box[1]
    }

    fun bottom(): Int {
        return box[3]
    }

    fun width(): Int {
        return box[2] - box[0] + 1
    }

    fun height(): Int {
        return box[3] - box[1] + 1
    }

    /**
     * 转为 rect
     */
    fun transform2Rect(): Rect {
        val rect = Rect()
        rect.left = Math.round(box[0].toFloat())
        rect.top = Math.round(box[1].toFloat())
        rect.right = Math.round(box[2].toFloat())
        rect.bottom = Math.round(box[3].toFloat())
        return rect
    }

    /**
     * 面积
     */
    fun area(): Int {
        return width() * height()
    }

    /**
     * Bounding Box Regression
     */
    fun calibrate() {
        val w = box[2] - box[0] + 1
        val h = box[3] - box[1] + 1
        box[0] = (box[0] + w * bbr[0]).toInt()
        box[1] = (box[1] + h * bbr[1]).toInt()
        box[2] = (box[2] + w * bbr[2]).toInt()
        box[3] = (box[3] + h * bbr[3]).toInt()
        for (i in 0..3) {
            bbr[i] = 0.0f
        }
    }

    /**
     * 当前 box 转为正方形
     */
    fun toSquareShape() {
        val w = width()
        val h = height()
        if (w > h) {
            box[1] -= (w - h) / 2
            box[3] += (w - h + 1) / 2
        } else {
            box[0] -= (h - w) / 2
            box[2] += (h - w + 1) / 2
        }
    }

    /**
     * 防止边界溢出，并维持 square 大小
     */
    fun limit_square(w: Int, h: Int) {
        if (box[0] < 0 || box[1] < 0) {
            val len = max(-box[0], -box[1])
            box[0] += len
            box[1] += len
        }
        if (box[2] >= w || box[3] >= h) {
            val len = max(box[2] - w + 1, box[3] - h + 1)
            box[2] -= len
            box[3] -= len
        }
    }

    fun limit_square2(w: Int, h: Int) {
        if (width() > w) box[2] -= width() - w
        if (height() > h) box[3] -= height() - h
        if (box[0] < 0) {
            val sz = -box[0]
            box[0] += sz
            box[2] += sz
        }
        if (box[1] < 0) {
            val sz = -box[1]
            box[1] += sz
            box[3] += sz
        }
        if (box[2] >= w) {
            val sz = box[2] - w + 1
            box[2] -= sz
            box[0] -= sz
        }
        if (box[3] >= h) {
            val sz = box[3] - h + 1
            box[3] -= sz
            box[1] -= sz
        }
    }
}
