package com.jie.tungcheung.ui.view

import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jie.tungcheung.vm.MainViewModel
import com.jie.tungcheung.R
import com.jie.tungcheung.base.BaseActivity
import com.jie.tungcheung.bean.Test
import com.jie.tungcheung.databinding.ActivityMainBinding
import com.jie.tungcheung.ui.adapter.TestAdapter
import com.jie.tungcheung.utils.enshrine.statusBar
import com.jie.tungcheung.utils.enshrine.toast
import com.jie.tungcheung.utils.process.read

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

    override fun initListener() {

    }

}