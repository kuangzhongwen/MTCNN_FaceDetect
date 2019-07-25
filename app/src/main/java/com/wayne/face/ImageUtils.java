package com.wayne.face;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

/**
 * Utility class for manipulating images.
 *
 * @author kuang on 2019/07/25.
 **/
public class ImageUtils {

    private static final String TAG = "ImageUtils";

    static {
        try {
            System.loadLibrary("faceSwapImageJni_lib");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Native library not found, native RGB -> YUV conversion may be unavailable.");
        }
    }

    // This value is 2 ^ 18 - 1, and is used to clamp the RGB values before their ranges
    // are normalized to eight bits.
    private static final int kMaxChannelValue = 262143;

    // Always prefer the native implementation if available.
    private static boolean useNativeConversion = true;

    private static int YUV2RGB(int y, int u, int v) {
        // Adjust and check YUV values
        y = (y - 16) < 0 ? 0 : (y - 16);
        u -= 128;
        v -= 128;

        // This is the floating point equivalent. We do the conversion in integer
        // because some Android devices do not have floating point in hardware.
        // nR = (int)(1.164 * nY + 2.018 * nU);
        // nG = (int)(1.164 * nY - 0.813 * nV - 0.391 * nU);
        // nB = (int)(1.164 * nY + 1.596 * nV);
        int y1192 = 1192 * y;
        int r = (y1192 + 1634 * v);
        int g = (y1192 - 833 * v - 400 * u);
        int b = (y1192 + 2066 * u);

        // Clipping RGB values to be inside boundaries [ 0 , kMaxChannelValue ]
        r = r > kMaxChannelValue ? kMaxChannelValue : (r < 0 ? 0 : r);
        g = g > kMaxChannelValue ? kMaxChannelValue : (g < 0 ? 0 : g);
        b = b > kMaxChannelValue ? kMaxChannelValue : (b < 0 ? 0 : b);

        return 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
    }


    static void convertYUV420ToARGB8888(
        byte[] yData,
        byte[] uData,
        byte[] vData,
        int width,
        int height,
        int yRowStride,
        int uvRowStride,
        int uvPixelStride,
        int[] out) {
        if (useNativeConversion) {
            try {
                convertYUV420ToARGB8888(
                    yData, uData, vData, out, width, height, yRowStride, uvRowStride, uvPixelStride, false);
                return;
            } catch (UnsatisfiedLinkError e) {
                Log.e(TAG,
                    "Native YUV420 -> RGB implementation not found, falling back to Java implementation");
                useNativeConversion = false;
            }
        }

        int yp = 0;
        for (int j = 0; j < height; j++) {
            int pY = yRowStride * j;
            int pUV = uvRowStride * (j >> 1);

            for (int i = 0; i < width; i++) {
                int uv_offset = pUV + (i >> 1) * uvPixelStride;

                out[yp++] = YUV2RGB(
                    0xff & yData[pY + i],
                    0xff & uData[uv_offset],
                    0xff & vData[uv_offset]);
            }
        }
    }

    /**
     * Converts YUV420 semi-planar data to ARGB 8888 data using the supplied width
     * and height. The input and output must already be allocated and non-null.
     * For efficiency, no error checking is performed.
     *
     * @param width    The width of the input image.
     * @param height   The height of the input image.
     * @param halfSize If true, downsample to 50% in each dimension, otherwise not.
     * @param output   A pre-allocated array for the ARGB 8:8:8:8 output data.
     */
    public static native void convertYUV420ToARGB8888(
        byte[] y,
        byte[] u,
        byte[] v,
        int[] output,
        int width,
        int height,
        int yRowStride,
        int uvRowStride,
        int uvPixelStride,
        boolean halfSize);

    static Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        try {
            return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
        } catch (OutOfMemoryError ex) {
            ex.printStackTrace();
        }
        return bm;
    }
}
