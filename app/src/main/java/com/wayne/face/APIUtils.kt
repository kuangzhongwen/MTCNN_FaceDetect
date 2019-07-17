package com.wayne.face

import android.os.Build

/**
 * Class containing some static API utility methods.
 *
 * <br>
 * Kotlin语言中使用 "object" 修饰静态类，被修饰的类，可以使用类名.方法名的形式调用，如下：
 * var version_name1 = Util.getName()
 * </br>
 *
 * @author kuang on 2019/07/17.
 */
object APIUtils {

    /**
     * If platform is Froyo (level 8) or above.
     *
     * @return If platform SDK is above Froyo
     */
    fun hasFroyo(): Boolean {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO
    }


    /**
     * If platform is Gingerbread (level 9) or above.
     *
     * @return If platform SDK is above Gingerbread
     */
    fun hasGingerbread(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
    }

    /**
     * If platform is Honeycomb (level 11) or above.
     *
     * @return If platform SDK is above Honeycomb
     */
    fun hasHoneycomb(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
    }

    /**
     * If platform is Honeycomb MR1 (level 12) or above.
     *
     * @return If platform SDK is above Honeycomb MR1
     */
    fun hasHoneycombMR1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1
    }

    /**
     * If platform is Ice Cream Sandwich (level 14) or above.
     *
     * @return If platform SDK is above Ice Cream Sandwich
     */
    fun hasICS(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
    }

    /**
     * If platform is Ice Cream Sandwich MR1 (level 15) or above.
     *
     * @return If platform SDK is above Ice Cream Sandwich MR1
     */
    fun hasICSMR1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
    }

    /**
     * If platform is JellyBean (level 16) or above.
     *
     * @return If platform SDK is above JellyBean
     */
    fun hasJellyBean(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
    }

    /**
     * If platform is JellyBean MR1 (level 17) or above.
     *
     * @return If platform SDK is above JellyBean MR1
     */
    fun hasJellyBeanMR1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
    }

    /**
     * If platform is JellyBean MR2 (level 18) or above.
     *
     * @return If platform SDK is above JellyBean MR2
     */
    fun hasJellyBeanMR2(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
    }

    /**
     * If platform is KitKat (level 19) or above.
     *
     * @return If platform SDK is above KitKat
     */
    fun hasKitKat(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    }

    /**
     * If platform is Lollipop (level 21) or above.
     *
     * @return If platform SDK is above Lollipop
     */
    fun hasLollipop(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    /**
     * If platform is LOLLIPOP_MR1 (level 22) or above.
     *
     * @return If platform SDK is above LOLLIPOP_MR1
     */
    fun hasLOLLIPOP_MR1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1
    }

    /**
     * If platform is LOLLIPOP_MR1 (level 23) or above.
     *
     * @return If platform SDK is above M
     */
    fun hasM(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    /**
     * Private constructor to prohibit nonsense instance creation.
     */
}