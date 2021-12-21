package com.jie.tungcheung.ui.view

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jie.tungcheung.BuildConfig.APPLICATION_ID
import com.jie.tungcheung.vm.MainViewModel
import com.jie.tungcheung.R
import com.jie.tungcheung.base.BaseActivity
import com.jie.tungcheung.bean.Test
import com.jie.tungcheung.databinding.ActivityMainBinding
import com.jie.tungcheung.ui.adapter.TestAdapter
import com.jie.tungcheung.utils.enshrine.*
import com.jie.tungcheung.utils.enshrine.ResultsApi.readForPermissions
import com.jie.tungcheung.utils.enshrine.ResultsApi.readForStart
import com.jie.tungcheung.utils.enshrine.ResultsApi.readForVideo
import com.jie.tungcheung.utils.enshrine.ResultsApi.startActivityForResults
import com.jie.tungcheung.utils.enshrine.ResultsApi.startPermissions
import com.jie.tungcheung.utils.enshrine.ResultsApi.startVideo
import com.jie.tungcheung.utils.glide.TGlide.Companion.load
import com.jie.tungcheung.utils.process.read
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    val adapter by lazy {
        TestAdapter(this, ObservableArrayList<Test>())
    }
    val layout by lazy {
        LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun getViewId(): Int = R.layout.activity_main

    override fun onBundle(bundle: Bundle) {

    }

    override fun observerUI() {
        viewModel.test.read(this, loading = {
        }, timeOut = {
        }, error = { exception, t ->
        }) {
            it?.data?.let { list ->
                adapter.notifyData(list)
            }
        }
        readForStart {
            toast("${it.data!!.getStringExtra("Test")}")
        }
        readForPermissions {
            Toast.makeText(
                this,
                "文件读取权限：${if (it[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] != null) it[android.Manifest.permission.WRITE_EXTERNAL_STORAGE]!! else true}\n" +
                        "日历访问权限：${if (it[android.Manifest.permission.WRITE_CALENDAR] != null) it[android.Manifest.permission.WRITE_CALENDAR]!! else true}\n" +
                        "相机权限：${if (it[android.Manifest.permission.CAMERA] != null) it[android.Manifest.permission.CAMERA]!! else true}\n" +
                        "联系人访问权限：${if (it[android.Manifest.permission.GET_ACCOUNTS] != null) it[android.Manifest.permission.GET_ACCOUNTS]!! else true}\n" +
                        "位置访问权限：${if (it[android.Manifest.permission.ACCESS_COARSE_LOCATION] != null) it[android.Manifest.permission.ACCESS_COARSE_LOCATION]!! else true}",
                Toast.LENGTH_SHORT
            ).show()
        }
        readForVideo {

        }
    }


    override fun init(savedInstanceState: Bundle?) {
        binding {
            main = this@MainActivity
            executePendingBindings()
        }
        statusBar {
            setScreen(false)
        }
        viewModel.test()
    }

    fun startAct() {
        binding {
            startActivityForResults<TestActivity>()
        }
    }

    fun startPms() {
        binding {
            startPermissions(
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_CALENDAR,
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.GET_ACCOUNTS,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    fun startVd() {
        binding {
            val file = File(
                "/storage/emulated/0",
                "Dong Yijie Waste Recycling Co. LTD"
            )
            if (!file!!.exists()) {
                file!!.mkdirs()
            }
            val fileUrl = File("$file/${System.currentTimeMillis()}.mp4")
            startVideo(this@MainActivity, "$APPLICATION_ID.FileProvider", fileUrl)
        }
    }

    override fun initListener() {

    }

}