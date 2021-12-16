package com.jie.tungcheung.utils.enshrine

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jie.tungcheung.R
import com.jie.tungcheung.base.AppContext
import com.jie.tungcheung.utils.glide.TGlide
import java.io.File
import java.math.BigDecimal

inline val CONFIG
    get() = TGlide.Config.newConfig().apply {
        cacheMode = DiskCacheStrategy.DATA
    }

inline val SCALE_CONFIG
    get() = TGlide.Config.newConfig().apply {
        cacheMode = DiskCacheStrategy.DATA
        isRevealScale = true
    }
/**
 * 自定义toast 覆盖上次toast
 */
private var toast: Toast? = null
fun toast(info: String?) {
    if (info != null) {
        val ctx = AppContext.get().getActivity()
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.layout_toast, null)
        view.findViewById<TextView>(R.id.toast_text).apply {
            text = info
            val w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            val h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            measure(w, h)
            val measuredWidth = measuredWidth
            layoutParams.width = measuredWidth + dip2px(ctx!!, 40F)
        }
        if (toast != null) {
            toast?.apply {
                cancel()
                toast = Toast(ctx).apply {
                    setGravity(Gravity.CENTER, 0, 0)
                    duration = Toast.LENGTH_SHORT
                    this.view = view
                    show()
                }
            }
        } else toast = Toast(ctx).apply {
            setGravity(Gravity.CENTER, 0, 0)
            duration = Toast.LENGTH_SHORT
            this.view = view
            show()
        }
    }

}

/**
 * Activity中跳转
 * @param isAnimation 是否加上动画固定的标识,false不加,true加,默认不加.动画必须有此标识才会执行
 * @param deleteAnimation 是否删除默认动画,false不删除,true删除,默认不删除
 */
inline fun <reified T> Activity.switchTo(
    bundle: Bundle? = null,
    isAnimation: Boolean = false,
    deleteAnimation: Boolean = false
) {
    val intent = Intent(AppContext.get().context, T::class.java)
    if (bundle != null) intent.putExtras(bundle)
    startActivity(
        intent, if (isAnimation)
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle() else null
    )
    if (deleteAnimation) overridePendingTransition(0, 0)
}

/**
 * 动画类型
 */
enum class Animal() {
    Explode, Slide, Fade
}

data class AnimalServe(
    val Enter: Boolean = true,
    val Exit: Boolean = true,
    val Return: Boolean = false,
    val Reenter: Boolean = false,
)

/**
 * 为当前类设置动画
 * @param type  动画类型,根据传入enum确定,默认滑动
 * Explode 分解
 * Slide 滑动
 * Fade 淡入淡出
 * @param serve 用作哪个地方 传入AnimalServe实例,默认设置入场和离场
 * @param duration 动画时长 单位为毫秒 默认为0.5秒
 * @param extend 用于对动画的扩展,比如组合动画,你可以创建新的动画并通过addTransition()进行添加
 * */
fun Activity.setAnimation(
    type: Animal = Animal.Slide,
    serve: AnimalServe = AnimalServe(),
    duration: Long = 500,
    extend: (TransitionSet.() -> Unit)? = null,
) {
    val transition = when (type) {
        Animal.Explode -> Explode().setDuration(duration)
        Animal.Slide -> Slide().setDuration(duration)
        Animal.Fade -> Fade().setDuration(duration)
        else -> Slide().setDuration(duration)
    }
    val transitionSet = TransitionSet()
    transitionSet.addTransition(transition).ordering = TransitionSet.ORDERING_TOGETHER
    extend?.invoke(transitionSet)
    if (serve.Enter)
    //入场动画  主界面-(跳转)->A，A 进入过渡
        window.enterTransition = transitionSet
    if (serve.Exit)
    //离场动画  A-(跳转)->B，A 出场过渡
        window.exitTransition = transitionSet
    if (serve.Return)
    //返回动画  A-(返回)->主界面，A 出场过渡
        window.returnTransition = transitionSet
    if (serve.Reenter)
    //重新进入动画  B-(返回)->A，A 进入过渡
        window.reenterTransition = transitionSet
}


/**
 *  如果在finish之前有较长时间的动画需要绘制而刚好app出现问题，可以考虑使用
 */
fun Activity.stop() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) finishAfterTransition() else finish()
}

/**
 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 */
fun dip2px(context: Context, dpValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
 */
fun px2dip(context: Context, pxValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * 跳转应用商店
 * @param context 上下文
 * @param appPkg 应用包名
 * @param marketPkg 应用商店包名
 */
fun launchAppDetail(context: Context, appPkg: String, marketPkg: String?) {
    try {
        if (TextUtils.isEmpty(appPkg)) return
        val uri: Uri = Uri.parse("market://details?id=$appPkg")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (!TextUtils.isEmpty(marketPkg)) {
            intent.setPackage(marketPkg)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


/**
 * 获取缓存大小
 * @param context
 * @return
 * @throws Exception
 */
@Throws(java.lang.Exception::class)
fun getTotalCacheSize(context: Context): String {
    var cacheSize = getFolderSize(context.cacheDir)
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
        cacheSize += getFolderSize(context.externalCacheDir)
    }
    return getFormatSize(cacheSize.toDouble())
}

/***
 * 清理所有缓存
 * @param context
 */
fun clearAllCache(context: Context) {
    deleteDir(context.cacheDir)
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
        deleteDir(context.externalCacheDir)
    }
}

private fun deleteDir(dir: File?): Boolean {
    if (dir != null && dir.isDirectory()) {
        val children: Array<String> = dir.list()
        for (i in children.indices) {
            val success = deleteDir(File(dir, children[i]))
            if (!success) {
                return false
            }
        }
    }
    return dir!!.delete()
}

// 获取文件
//Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
//Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
@Throws(java.lang.Exception::class)
fun getFolderSize(file: File?): Long {
    var size: Long = 0
    try {
        val fileList: Array<File> = file!!.listFiles()
        for (i in fileList.indices) {
            // 如果下面还有文件
            if (fileList[i].isDirectory()) {
                size += getFolderSize(fileList[i])
            } else {
                size += fileList[i].length()
            }
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return size
}

/**
 * 格式化单位
 *
 * @param size
 * @return
 */
fun getFormatSize(size: Double): String {
    val kiloByte = size / 1024
    if (kiloByte < 1) {
//            return size + "Byte";
        return "0K"
    }
    val megaByte = kiloByte / 1024
    if (megaByte < 1) {
        val result1 = BigDecimal(kiloByte.toString())
        return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
            .toPlainString().toString() + "KB"
    }
    val gigaByte = megaByte / 1024
    if (gigaByte < 1) {
        val result2 = BigDecimal(megaByte.toString())
        return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
            .toPlainString().toString() + "MB"
    }
    val teraBytes = gigaByte / 1024
    if (teraBytes < 1) {
        val result3 = BigDecimal(gigaByte.toString())
        return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
            .toPlainString().toString() + "GB"
    }
    val result4 = BigDecimal(teraBytes)
    return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
        .toString() + "TB"
}





