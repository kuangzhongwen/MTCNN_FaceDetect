package com.wayne.face

import android.app.ProgressDialog
import android.content.Context
import android.graphics.SurfaceTexture
import android.support.v4.app.Fragment
import android.util.Log
import android.view.TextureView

/**
 * 相机 fragment
 *
 * @author kuang on 2019/07/24.
 */
class CameraFragment : Fragment(), TextureView.SurfaceTextureListener {

    private val mContext: Context? = FaceDetectApp.sAppContext

    /**
     * TextureView
     */
    private val mTextureView: AutoFitTextureView? = null
    /**
     * 人脸框和关键点控件
     */
    private val mBoundingBoxView: BoundingBoxView? = null
    /**
     * 等待框
     */
    private val mWaitingDialog: ProgressDialog? = null

    /**
     * MTCNN 进行人脸检测，检测效果比 DLib 更佳，目前返回的数据包括人脸的 (x, y, w, h) 和 5 个关键点
     */
    private val mtcnn: MTCNN? = null

    private val mHandler = android.os.Handler()

    private val mStopTask: Thread? = null

    /**
     * 构造器
     */
    init {
        if (Constants.LOG_DEBUG)
            Log.i(TAG, "CameraFragment construct")
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private val TAG = "CameraFragment"

        /**
         * 检测人脸时喂给检测器的图片缩放比例
         */
        private val DETECT_SCALE = 2.0f

        fun newInstance(): CameraFragment {
            return CameraFragment()
        }
    }
}
