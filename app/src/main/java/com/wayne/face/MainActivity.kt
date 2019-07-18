package com.wayne.face

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

/**
 * 主界面
 *
 * @author kuang on 2019/07/17.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Log.i("kuang", AppUtils.getClassSimpleName(javaClass))
    }
}
