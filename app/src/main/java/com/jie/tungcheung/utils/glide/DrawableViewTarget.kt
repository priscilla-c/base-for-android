package com.jie.tungcheung.utils.glide

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition


abstract class DrawableViewTarget(view: ImageView) : CustomViewTarget<ImageView, Drawable>(view) {

    override fun onLoadFailed(errorDrawable: Drawable?) {
        view.background = errorDrawable
    }

    override fun onResourceCleared(placeholder: Drawable?) {
        view.background = placeholder
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        resourceReady(resource,transition)
    }


    abstract fun resourceReady(resource: Drawable,transition: Transition<in Drawable>?)
}