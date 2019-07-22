package com.wayne.face

import android.app.Application
import android.content.Context

/**
 * Application
 *
 * <doc>
 * 1. 用 object 修饰的类为静态类，里面的方法和变量都为静态的：
 * object DemoManager {
 *     private val TAG = "DemoManager"
 *     fun a() {
 *        Log.e(TAG,"此时 object 表示 声明静态内部类")
 *     }
 * }
 *
 * 2. 类内部的对象声明，没有被inner 修饰的内部类都是静态的：
 * class DemoManager{
 *     object MyObject {
 *         fun a() {
 *             Log.e(TAG,"此时 object 表示 直接声明类")
 *         }
 *     }
 * }
 *
 * 3. companion object 修饰为伴生对象，伴生对象在类中只能存在一个，类似于 java 中的静态方法。
 * Java 中使用类访问静态成员，静态方法。
 * companion object {
 *      private val TAG = "DemoManager"
 *      fun b() {
 *          Log.e(TAG,"此时 companion object表示 伴生对象")
 *      }
 * }
 * </doc>
 *
 * @author kuang on 2019/07/19.
 */
internal class FaceDetectApp: Application() {

    override fun onCreate() {
        super.onCreate()
        sAppContext = applicationContext
    }

    companion object {
        /**
         * Utility method to get application context
         *
         * @return Application context
         */
        var sAppContext: Context ? = null
            private set
    }
}