package com.jie.tungcheung.utils.enshrine

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.jie.tungcheung.base.AppContext
import com.jie.tungcheung.utils.enshrine.ResultsApi.readForCamera
import com.jie.tungcheung.utils.enshrine.ResultsApi.readForStart
import com.jie.tungcheung.utils.enshrine.ResultsApi.readForVideo
import com.jie.tungcheung.utils.enshrine.ResultsApi.startCamera
import com.jie.tungcheung.utils.enshrine.ResultsApi.startVideo
import java.io.File

/**
 * ResultsApi工具类
 * Created by Priscilla Cheung on 2021/7/20/11:58:25
 * info:通过lifecycle绑定进行监听
 * 使用说明:
 * 1、在BaseActivity和BaseFragment调用initializeApi()进行初始化
 * 2、调用需要的方法
 * 该类提供的方法:
 * @see startActivity 跳转Activity并请求回调
 * @see startCamera 调用相册
 * @see startTakePhotos 调用相机
 * @see startVideo 调用录屏
 * @see startPermission 获取一个权限
 * @see startPermissions 获取一组权限
 * @see startDocument 调用文件管理获取一个文件
 * @see startDocuments 调用文件管理获取多个文件
 */
object ResultsApi {
    //作为Camera和Document的参数使用,意为筛选
    const val CAMERA = "image/*"
    const val VIDEO = "video/*"
    val activityResultMap = HashMap<String, ActivityResultLauncher<Intent>>()
    private val cameraMap = HashMap<String, ActivityResultLauncher<String>>()
    private val takePhotosMap = HashMap<String, ActivityResultLauncher<Uri>>()
    private val videoMap = HashMap<String, ActivityResultLauncher<Uri>>()
    private val permissionMap = HashMap<String, ActivityResultLauncher<String>>()
    private val permissionsMap = HashMap<String, ActivityResultLauncher<Array<String>>>()
    private val documentMap = HashMap<String, ActivityResultLauncher<Array<String>>>()
    private val documentsMap = HashMap<String, ActivityResultLauncher<Array<String>>>()

//-------------------------------------------------------------------------------------------------//
    /**
     *  @author Priscilla
     *  跳转Activity并请求回调
     */

    fun FragmentActivity.readForStart(activityResult: ((ActivityResult) -> Unit)) {
        if (activityResultMap[this.javaClass.canonicalName] == null) {
            activityResultMap[this.javaClass.canonicalName] =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (it != null)
                        activityResult?.invoke(it)
                }
        }
    }

    inline fun <reified T> FragmentActivity.startActivityForResults(
        bundle: Bundle? = null,
    ) {
        val intent = Intent(AppContext.get().context, T::class.java)
        if (bundle != null) intent.putExtras(bundle)
        if (activityResultMap[this.javaClass.canonicalName] != null)
            activityResultMap[this.javaClass.canonicalName]!!.launch(intent)
        else Toast.makeText(this, "未初始化", Toast.LENGTH_SHORT).show()
    }
//-------------------------------------------------------------------------------------------------//

//-------------------------------------------------------------------------------------------------//
    /**
     *  @author Priscilla
     *  调用相册
     *  @param result 拥有权限时所执行的结果
     */
    fun FragmentActivity.readForCamera(result: ((Uri) -> Unit)) {
        if (cameraMap[this.javaClass.canonicalName] == null) {
            cameraMap[this.javaClass.canonicalName] =
                registerForActivityResult(ActivityResultContracts.GetContent()) {
                    if (it != null)
                        result.invoke(it)
                }
        }
    }

    /**
     * input:
     * @param type 一般传入CAMERA,可以自定义筛选类型
     * @param permissionError 缺少权限时执行的操作
     */
    fun FragmentActivity.startCamera(
        type: String,
    ) {
        if (cameraMap[this.javaClass.canonicalName] != null)
            cameraMap[this.javaClass.canonicalName]!!.launch(type)
    }

//-------------------------------------------------------------------------------------------------//

//-------------------------------------------------------------------------------------------------//
    /**
     *  @author Priscilla
     *  调用相机
     *  @param result 是否保存成功
     */
    fun FragmentActivity.readForTakePhotos(result: (Boolean) -> Unit) {
        if (takePhotosMap[this.javaClass.canonicalName] == null) {
            takePhotosMap[this.javaClass.canonicalName] =
                registerForActivityResult(ActivityResultContracts.TakePicture()) {
                    result.invoke(it)
                }
        }
    }

    /**
     * input:
     * @param context 上下文
     * @param authority Provider的authorities字段
     * @param file 一个文件路径  示例:   File(externalCacheDir, "${System.currentTimeMillis()}priscilla.jpg"))
     * @param permissionError 缺少权限时执行的操作
     */
    fun FragmentActivity.startTakePhotos(
        authority: String,
        file: File,
    ) {
        val uri = FileProvider.getUriForFile(
            this,
            authority,
            file
        )
        if (takePhotosMap[this.javaClass.canonicalName] != null)
            takePhotosMap[this.javaClass.canonicalName]!!.launch(uri)
    }

//-------------------------------------------------------------------------------------------------//

//-------------------------------------------------------------------------------------------------//
    /**
     *  @author Priscilla
     *  调用录屏
     *  @param videoUrl 返回视频缩略图
     */
    fun FragmentActivity.readForVideo(videoUrl: (Bitmap?) -> Unit) {
        if (videoMap[this.javaClass.canonicalName] == null) {
            videoMap[this.javaClass.canonicalName] =
                registerForActivityResult(ActivityResultContracts.TakeVideo()) {
                    videoUrl.invoke(it)
                }
        }
    }

    /**
     * input:
     * @param context 上下文
     * @param authority Provider的authorities字段
     * @param file 一个文件路径  示例:   File(externalCacheDir, "${System.currentTimeMillis()}priscilla.mp4"))
     *
     */

    fun FragmentActivity.startVideo(
        context: Context,
        authority: String,
        file: File,
    ) {
        val uri = FileProvider.getUriForFile(
            context,
            authority,
            file
        )
        if (videoMap[this.javaClass.canonicalName] != null)
            videoMap[this.javaClass.canonicalName]!!.launch(uri)
        startPermission(android.Manifest.permission.CAMERA)
    }
//-------------------------------------------------------------------------------------------------//

//-------------------------------------------------------------------------------------------------//
    /**
     * @author Priscilla
     * 单一权限
     */
    fun FragmentActivity.readForPermission(result: (Boolean) -> Unit) {
        if (permissionMap[this.javaClass.canonicalName] == null) {
            permissionMap[this.javaClass.canonicalName] =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                    result.invoke(it)
                }
        }
    }

    /**
     * input:
     * @param permission 要请求的权限 示例:android.Manifest.permission.WRITE_EXTERNAL_STORAGE
     */
    fun FragmentActivity.startPermission(permission: String) {
        if (permissionMap[this.javaClass.canonicalName] != null) {
            permissionMap[this.javaClass.canonicalName]!!.launch(permission)
        }
    }

//-------------------------------------------------------------------------------------------------//

    //-------------------------------------------------------------------------------------------------//
    /**
     * @author Priscilla
     * 一组权限
     */
    fun FragmentActivity.readForPermissions(havePermission: (MutableMap<String, Boolean>) -> Unit) {
        if (permissionsMap[this.javaClass.canonicalName] == null) {
            permissionsMap[this.javaClass.canonicalName] =
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                    havePermission.invoke(it)
                }
        }
    }

    /**
     * input:
     * @param permission 要请求的权限数组
     */
    fun FragmentActivity.startPermissions(
        permission: Array<String>,
    ) {
        if (permissionsMap[this.javaClass.canonicalName] != null) {
            permissionsMap[this.javaClass.canonicalName]!!.launch(permission)
        }
    }
//-------------------------------------------------------------------------------------------------//

//-------------------------------------------------------------------------------------------------//
    /**
     *  @author Priscilla
     *  调用文件
     */
    fun FragmentActivity.readForDocument(result: (Uri) -> Unit) {
        if (documentMap[this.javaClass.canonicalName] == null) {
            documentMap[this.javaClass.canonicalName] =
                registerForActivityResult(ActivityResultContracts.OpenDocument()) {
                    if (it != null)
                        result.invoke(it)
                }
        }
    }

    /**
     * input:
     * @param type 筛选类型
     */
    fun FragmentActivity.startDocument(
        type: Array<String>, permissionError: (() -> Unit)? = null,
    ) {
        readForPermission {
            if (it) {
                if (documentMap[this.javaClass.canonicalName] == null) {
                    documentMap[this.javaClass.canonicalName]!!.launch(type)
                } else permissionError?.invoke()
            }
            startPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
//-------------------------------------------------------------------------------------------------//

//-------------------------------------------------------------------------------------------------//
    /**
     *  @author Priscilla
     *  调用文件组
     */
    private var resultDocumentsLauncher: ActivityResultLauncher<Array<String>>? = null
    fun FragmentActivity.readForDocuments(result: (MutableList<Uri>) -> Unit) {
        if (documentsMap[this.javaClass.canonicalName] == null) {
            documentsMap[this.javaClass.canonicalName] =
                registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) {
                    if (it != null)
                        result.invoke(it)
                }
        }
    }

    /**
     * input:
     * @param type 筛选类型
     */
    fun AppCompatActivity.startDocuments(
        type: Array<String>, permissionError: (() -> Unit)? = null,
        result: (MutableList<Uri>) -> Unit,
    ) {
        readForPermission {
            if (it) {
                if (documentsMap[this.javaClass.canonicalName] == null) {
                    documentsMap[this.javaClass.canonicalName]!!.launch(type)
                } else permissionError?.invoke()
            }
            startPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
}