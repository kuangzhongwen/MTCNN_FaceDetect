package com.wayne.face

import android.content.Context
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import java.io.File
import java.util.*


/**
 * 相机 fragment
 *
 * @author kuang on 2019/07/24.
 */
class CameraFragment : Fragment(), TextureView.SurfaceTextureListener, Camera2Wrapper.OnCamera2FrameListener {

    private val mContext: Context? = FaceDetectApp.sAppContext

    /**
     * TextureView
     */
    private var mTextureView: AutoFitTextureView? = null
    /**
     * 人脸框和关键点控件
     */
    private var mBoundingBoxView: BoundingBoxView? = null

    /**
     * MTCNN 进行人脸检测，检测效果比 DLib 更佳，目前返回的数据包括人脸的 (x, y, w, h) 和 5 个关键点
     */
    private var mtcnn: MTCNN? = null
    /**
     * 相机包装类
     */
    private var mCamera2Wrapper: Camera2Wrapper? = null

    /**
     * Handler 对象
     */
    private val mHandler = android.os.Handler()

    /**
     * 构造器
     */
    init {
        if (Constants.LOG_DEBUG)
            Log.i(TAG, "CameraFragment construct")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mTextureView = view.findViewById(R.id.textureView)
        mCamera2Wrapper = Camera2Wrapper(activity, mTextureView)
        mCamera2Wrapper?.setOnCamera2FrameListener(this)
        mBoundingBoxView = view.findViewById(R.id.boundingBoxView)
        val switchCameraBtn: View = view.findViewById(R.id.camera_switch_View)
        switchCameraBtn.setOnClickListener { mCamera2Wrapper?.switchCamera() }
        init_MTCNN()
    }

    override fun onResume() {
        super.onResume()
        mCamera2Wrapper?.startBackgroundThread()
        when (mTextureView?.isAvailable()) {
            true -> {
                mCamera2Wrapper?.openCamera(
                    mTextureView?.getWidth() ?: 0,
                    mTextureView?.getHeight() ?: 0
                )
            }
            false -> {
                mTextureView?.setSurfaceTextureListener(this)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mCamera2Wrapper?.closeCamera()
        mCamera2Wrapper?.stopBackgroundThread()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCamera2Wrapper?.release()
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        mCamera2Wrapper?.openCamera(width, height)
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        mCamera2Wrapper?.configureTransform(width, height)
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        return false
    }

    override fun onImageAvailable(bitmap: Bitmap) {
        if (mtcnn != null) {
            Thread(Runnable {
                Log.d(TAG, "begin detect faces")
                val results: Vector<Box>?
                // 同步 this || this@CameraFragment
                synchronized(this@CameraFragment) {
                    results = mtcnn?.detectFaces(bitmap, 40)
                }
                Log.d(TAG, "end detect faces")
                if (results != null && results.size > 0) {
                    mHandler.postAtFrontOfQueue {
                        if (AppUtils.isSecureContextForUI(activity)) {
                            mBoundingBoxView?.setResults(results)
                        }
                    }
                } else {
                    bitmap.recycle()
                }
            }).start()
        }
    }

    private fun init_MTCNN() {
        Thread(Runnable {
            Log.d(TAG, "initMTCNN_DLib begin")
            /**
             * 拷贝 DLib 需要的模型到 SD 卡
             */
            /**
             * 拷贝 DLib 需要的模型到 SD 卡
             */
            val faceShapeModelPath = Constants.getFaceShapeModelPath()
            if (!File(faceShapeModelPath).exists()) {
                FileUtils.copyFileFromAssetsToOthers(
                    mContext,
                    Constants.FACE_SHAPE_MODEL_PATH, faceShapeModelPath
                )
            }
            mtcnn = MTCNN(activity?.assets)
            Log.d(TAG, "initMTCNN_DLib end")
        }).start()
    }

    companion object {
        private const val TAG = "CameraFragment"

        fun newInstance(): CameraFragment {
            return CameraFragment()
        }
    }
}
