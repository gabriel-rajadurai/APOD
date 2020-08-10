package com.gabriel.astronomypod.common

import android.animation.Animator
import android.animation.ObjectAnimator
import android.net.Uri
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun ImageView.loadUrl(url: String, scaleType: ScaleType = ScaleType.NONE) {

    Glide.with(this)
        .load(Uri.parse(url))
        .apply {
            when (scaleType) {
                ScaleType.CENTER_CROP -> centerCrop()
                ScaleType.FIT_CENTER -> fitCenter()
                ScaleType.CENTER_INSIDE -> centerInside()
                ScaleType.NONE -> {
                    // Do nothing
                }
            }
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

enum class ScaleType {
    CENTER_CROP,
    FIT_CENTER,
    NONE,
    CENTER_INSIDE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun ViewPropertyAnimator.onComplete(complete:()->Unit){
    setListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {}

        override fun onAnimationEnd(animation: Animator?) {
            complete()
        }

        override fun onAnimationCancel(animation: Animator?) {}

        override fun onAnimationStart(animation: Animator?) {}
    })
}