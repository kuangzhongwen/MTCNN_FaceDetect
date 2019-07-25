package com.wayne.face

import android.content.Context
import android.util.Log
import java.io.*

/**
 * 文件工具类.
 *
 * @author kuang on 2019/07/25.
 */
object FileUtils {

    private const val TAG = "FileUtils"

    /**
     * Copies a file from assets.
     *
     * @param context    application context used to discover assets.
     * @param assetName  the relative file name within assets.
     * @param targetName the target file name, always over write the existing file.
     * @throws IOException if operation fails.
     */
    fun copyFileFromAssetsToOthers(context: Context?, assetName: String, targetName: String) {
        val targetFile: File
        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null
        try {
            val assets = context?.assets
            targetFile = File(targetName)
            if (!targetFile.parentFile.exists()) {
                targetFile.parentFile.mkdirs()
            }
            if (!targetFile.exists()) {
                targetFile.createNewFile()
            }
            inputStream = assets?.open(assetName)
            outputStream = FileOutputStream(targetFile, false /* append */)
            val buf = ByteArray(2048)
            while (true) {
                val r = inputStream?.read(buf) ?: -1
                if (r == -1) {
                    break
                }
                outputStream.write(buf, 0, r)
            }
        } catch (e: Exception) {
            if (Constants.LOG_DEBUG)
                Log.e(TAG, e.message)
        } finally {
            closeSafely(outputStream)
            closeSafely(inputStream)
        }
    }

    private fun closeSafely(closeable: Closeable?) {
        try {
            closeable?.close()
        } catch (e: IOException) {
            if (Constants.LOG_DEBUG)
                Log.e(TAG, e.message)
        }
    }
}