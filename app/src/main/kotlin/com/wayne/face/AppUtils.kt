package com.wayne.face

import android.app.Activity
import android.app.ActivityManager
import android.os.Handler
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Looper.getMainLooper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import kotlin.collections.ArrayList

/**
 * app相关工具，如安装，运行堆栈，跳转，前后台信息等
 *
 * @author kuang on 2019/07/18.
 */
object AppUtils {

    private val TAG = "AppUtils"
    private val SCHEME = "package"

    /**
     * Kotlin 的空安全设计对于声明可为空的参数，在使用时要进行空判断处理，有两种处理方式，
     * 字段后加 !! 像Java一样抛出空异常，另一种字段后加 ? 可不做处理返回值为 null 或配合 ?: 做空判断处理
     */
    var handler: Handler? = Handler(getMainLooper())

    /**
     * 获取 Activity
     */
    fun getActivity(obj: Any): Activity? {
        if (obj is Fragment)
        // 可以自动转型，在 if 内
            return obj.activity
        else if (obj is Activity)
            return obj
        return null
    }

    /**
     * 判断用于UI上下文的activity对象是否安全
     *
     * @param activity 需要判断的activity
     * @return true: UI上下文安全的activity对象
     */
    fun isSecureContextForUI(activity: Activity?): Boolean {
        if (activity == null || activity.isFinishing)
            return false
        if (APIUtils.hasJellyBeanMR2()) {
            try {
                return !activity.isDestroyed()
            } catch (e: Exception) {
                Log.e(TAG, "bugfix umeng 三星SM-w2014 4.3 系统 NoSuchMethodError 崩溃")
            }
        }
        return true
    }

    /**
     * 判断某个 Activity 是否处于最前端
     *
     * @param className Class.getName 判断该ClassName对应的类是否是TopActivity
     */
    fun isTopActivity(ctx: Context, className: String): Boolean {
        // 使用 as 可以强转类型
        val activityManager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val taskInfos = activityManager.getRunningTasks(100)
        if (taskInfos.size == 0)
            return false
        for (taskInfo in taskInfos) {
            // 判断是否为我们的应用跑起来的 task
            if (taskInfo.baseActivity.toString().contains(ctx.packageName)) {
                /*
                * Kotlin的 == 与Java的 == 的对比在于：Java的 == 是比较字符串的内存地址，
                * Kotlin的 == 与Java语言的equals方法相等
                */
                if (taskInfo.topActivity.className == className) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 获取class的Simple name
     *
     * 注意 * 相当于 Java 泛型中的 ?
     */
    fun getClassSimpleName(cla: Class<*>): String {
        return cla.simpleName
    }

    /**
     * 是否是同一个Activity类
     *
     * 注意 * 相当于 Java 泛型中的 ?
     */
    fun isTopActivity(activity: Activity?, destClaz: Class<*>?): Boolean {
        // 注意这种写法
        return if (activity == null || destClaz == null) {
            false
            // 注意 .javaClass 可以获取到对应的 java class
        } else getClassSimpleName(activity.javaClass) == getClassSimpleName(destClaz)
    }

    /**
     * 获取栈顶Activity名称
     */
    fun getTopActivityName(context: Context): String? {
        var topActivityClassName: String? = null
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTaskInfos = activityManager.getRunningTasks(1)
        if (runningTaskInfos != null) {
            val f = runningTaskInfos[0].topActivity
            topActivityClassName = f.className
        }
        return topActivityClassName
    }

    /**
     * 是否运行在前台
     */
    fun isRunningForeground(context: Context?): Boolean {
        if (context != null) {
            val packageName = context.packageName
            val topActivityClassName = getTopActivityName(context)
            return packageName != null && topActivityClassName != null && topActivityClassName.startsWith(packageName)
        }
        return false
    }

    /**
     * Get info for given package.
     *
     * @param packageName package name
     * @return [PackageInfo], null if package cannot be found in the system.
     */
    fun getPackageInfo(context: Context?, packageName: String?): PackageInfo? {
        var pkgInfo: PackageInfo? = null
        if (context != null && packageName != null) {
            val pm = context.packageManager
            try {
                pkgInfo = pm.getPackageInfo(packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                Log.e(TAG, e.message)
            }
        }
        return pkgInfo
    }

    /**
     * If given package is installed.
     *
     * @param packageName package name
     * @return is installed or not.
     */
    fun isPackageInstalled(context: Context, packageName: String): Boolean {
        return getPackageInfo(context, packageName) != null
    }

    /**
     * 获取:管理应用程序，应用程序详情Intent
     * <pre>
     * 支持android 2.3（ApiLevel 9）以上
     * </pre>
     */
    fun getInstalledAppDetailsIntent(pkgName: String): Intent {
        val intent = Intent()
        if (APIUtils.hasGingerbread()) {
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts(SCHEME, pkgName, null)
            intent.data = uri
        }
        return intent
    }

    /**
     * 获取正在运行的进程
     */
    fun getRunningProcess(context: Context): List<String> {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcess = activityManager
            .runningAppProcesses
        if (runningProcess == null || runningProcess.isEmpty()) {
            return emptyList()
        }
        val processNames = ArrayList<String>()
        for (ra in runningProcess) {
            processNames.add(ra.processName)
        }
        return processNames
    }

    /**
     * 安全启动应用程序，截获Exception。
     *
     * @param activity activity
     * @param intent   Intent
     * @param newTask  是否添加Intent.FLAG_ACTIVITY_NEW_TASK
     * @return 是否成功启动Activity。
     */
    private fun startActivitySafely(activity: Activity?, intent: Intent?, newTask: Boolean = true) {
        if (activity == null || intent == null) {
            return
        }
        try {
            if (newTask) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            activity.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(activity, "Related programs were not found!", Toast.LENGTH_LONG).show()
        }

    }

    /**
     * 安全启动应用程序，截获Exception。 必须在主线程被调用
     *
     * @param context context
     * @param intent  Intent
     * @return 是否成功启动Activity。
     */
    fun startActivitySafely(context: Context?, intent: Intent?) {
        if (context == null || intent == null) {
            return
        }
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            Log.i(TAG, "startActivitySafely")
        } catch (e: Exception) {
            Toast.makeText(context, "Related programs were not found!", Toast.LENGTH_LONG).show()
        }

    }

    /**
     * Safe version to start an activity for result
     *
     * @return 是否成功启动
     */
    fun startActivityForResultSafely(
        activity: Activity?, intent: Intent?,
        requestCode: Int
    ) {
        if (activity == null || intent == null) {
            return
        }
        try {
            activity.startActivityForResult(intent, requestCode)
        } catch (e: Exception) {
            Toast.makeText(activity, "Related programs were not found!", Toast.LENGTH_LONG).show()
        }

    }

    /**
     * Safe version to start an activity for result
     *
     * @return 是否成功启动
     */
    fun startActivityForResultSafely(
        fragment: android.support.v4.app.Fragment?,
        intent: Intent?, requestCode: Int
    ) {
        if (fragment == null || intent == null) {
            return
        }
        try {
            fragment.startActivityForResult(intent, requestCode)
        } catch (e: Exception) {
            if (fragment.activity != null) {
                Toast.makeText(fragment.activity, "Related programs were not found!", Toast.LENGTH_LONG).show()
            }
        }

    }

    /**
     * 获取fragment依附的Activity
     */
    fun getAttachActivity(fragment: android.support.v4.app.Fragment): Activity? {
        val ac = fragment.activity
        return if (ac != null && !ac.isFinishing) {
            ac
        } else null

    }

    /**
     * 回到系统桌面
     */
    fun backtoSystemDesktop(context: Context?) {
        if (context != null) {
            val i = Intent(Intent.ACTION_MAIN)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            i.addCategory(Intent.CATEGORY_HOME)
            startActivitySafely(context, i)
        }
    }

    fun runOnUiThreadSafely(activity: Activity?, runnable: Runnable?) {
        if (activity == null || activity.isFinishing || runnable == null) {
            return
        }
        try {
            activity.runOnUiThread(runnable)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @JvmOverloads
    fun runOnMainThread(runnable: Runnable?, delay: Long = 0L) {
        if (runnable == null) {
            return
        }
        if (handler == null) {
            handler = Handler()
        }

        if (delay > 0) {
            handler?.postDelayed(runnable, delay)
        } else {
            handler?.post(runnable)
        }
    }

    fun runAtFrontOfQueue(runnable: Runnable?) {
        if (runnable == null) {
            return
        }
        if (handler == null) {
            handler = Handler()
        }
        handler?.postAtFrontOfQueue(runnable)
    }

    /**
     * 终止ActivityThread的looper循环
     */
    fun mainLooperQuit() {
        try {
            val looper = getMainLooper()
            if (looper != null) {
                if (APIUtils.hasJellyBeanMR2()) {
                    looper.quitSafely()
                } else {
                    looper.quit()
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}