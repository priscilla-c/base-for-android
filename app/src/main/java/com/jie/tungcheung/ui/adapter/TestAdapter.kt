package com.jie.tungcheung.ui.adapter

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableArrayList
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.jie.tungcheung.R
import com.jie.tungcheung.base.BaseAdapter
import com.jie.tungcheung.bean.Test
import com.jie.tungcheung.databinding.ItemTestBinding
import com.jie.tungcheung.utils.enshrine.SCALE_CONFIG
import com.jie.tungcheung.utils.enshrine.dip2px
import com.jie.tungcheung.utils.glide.TGlide.Companion.load

//Created by Priscilla Cheung 2021年12月16日14:33:10.
class TestAdapter(context: Context, dataList: ObservableArrayList<Test>) :
    BaseAdapter<Test, ItemTestBinding>(context,dataList) {
    override fun getItemLayout(): Int = R.layout.item_test

    override fun bindItems(binding: ItemTestBinding?, item: Test, position: Int) {
        binding?.apply {
           test = item
            listImage.load(item.logo, SCALE_CONFIG)
            if (!item.imgs.isNullOrEmpty()) {
                card.layoutParams.height = dip2px(context, 154F)
                listIconA.load(item.imgs[0], SCALE_CONFIG)
                listIconA.visibility = View.VISIBLE
                if (item.imgs.size >= 2) {
                    listIconB.load(item.imgs[1], SCALE_CONFIG)
                    listIconB.visibility = View.VISIBLE
                } else listIconB.visibility = View.GONE
                if (item.imgs.size >= 3) {
                    listIconC.load(item.imgs[2], SCALE_CONFIG)
                    listIconC.visibility = View.VISIBLE
                } else listIconC.visibility = View.GONE
                if (item.imgs.size >= 4) {
                    listIconD.load(item.imgs[3], SCALE_CONFIG)
                    listIconD.visibility = View.VISIBLE
                } else listIconD.visibility = View.GONE
            } else {
                listIconA.visibility = View.GONE
                listIconB.visibility = View.GONE
                listIconC.visibility = View.GONE
                listIconD.visibility = View.GONE
                card.layoutParams.height = dip2px(context, 65F)
            }
            when {
                item.servicetype == 3 -> listTag.text = "置顶"
                position == 2 -> listTag.text =
                    "刷新"
                else -> listTag.visibility = View.GONE
            }
            executePendingBindings()
        }
    }

    fun notifyData(list: List<Test>) {
        data.apply {
            clear()
            addAll(list)
            notifyDataSetChanged()
        }
    }


}