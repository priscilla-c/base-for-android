package com.jie.tungcheung.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


open class DataBindingViewHolder<T : ViewBinding>(var binding: T) : RecyclerView.ViewHolder(binding.root)