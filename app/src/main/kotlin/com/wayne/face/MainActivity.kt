package com.wayne.face

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.pm.PackageManager
import android.Manifest.permission
import android.widget.Toast

/**
 * 主界面
 *
 * @author kuang on 2019/07/17.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Constants.LOG_DEBUG)
            Log.i(TAG, "into MainActivity")

        if (hasPermission()) {
            if (null == savedInstanceState) {
                setFragment()
            }
        } else {
            requestPermission()
        }
    }

    private fun hasPermission(): Boolean {
        // 这种 return 语句 return if - else
        return if (APIUtils.hasM()) {
            checkSelfPermission(permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun requestPermission() {
        if (APIUtils.hasM()) {
            if (shouldShowRequestPermissionRationale(permission.CAMERA)) {
                Toast.makeText(this, "Camera permission are required for this demo", Toast.LENGTH_LONG).show();
            }
            requestPermissions(arrayOf(permission.CAMERA), REQUEST_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setFragment()
            } else {
                requestPermission()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private fun setFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, CameraFragment.newInstance())
            .commitNowAllowingStateLoss()
    }

    private companion object {

        /**
         * const val 可见性为 public final static，可以直接访问。
         * val 可见性为 private final static，并且 val 会生成方法 getNormalObject()，通过方法调用访问。
         *
         * 当定义常量时，出于效率考虑，我们应该使用 const val 方式，避免频繁函数调用。
         */
        private const val TAG = "MainActivity"

        private const val REQUEST_PERMISSION = 1
    }
}
