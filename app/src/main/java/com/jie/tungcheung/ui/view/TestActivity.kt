package com.jie.tungcheung.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jie.tungcheung.R

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        intent.putExtra("Test","12456")
        setResult(200,intent)
    }
}