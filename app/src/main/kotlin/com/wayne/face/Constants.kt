package com.wayne.face

import java.io.File.separator
import android.os.Environment.getExternalStorageDirectory


/**
 * 常量类
 *
 * <br>
 * kotlin 修饰符:
 *
 * 修饰 类和接口
 * ----- public：默认修饰符，被其修饰的在任何位置都能访问 ----------
 * ----- private：表示只在这个类(以及它的所有成员)之内可以访问 -----
 * ----- protected：在当前类及其子类内访问 ----------------------
 * ----- internal：在同一模块内使用 ----------------------------
 * </br>
 *
 * @author kuang on 2019/07/17.
 */
internal object Constants {

    val LOG_DEBUG = BuildConfig.DEBUG

    const val FACE_SHAPE_MODEL_PATH = "shape_predictor_68_face_landmarks.dat"

    /**
     * getFaceShapeModelPath
     *
     * @return default face shape model path
     */
    fun getFaceShapeModelPath(): String {
        val sdcard = getExternalStorageDirectory()
        return sdcard.getAbsolutePath() + separator + FACE_SHAPE_MODEL_PATH
    }
}