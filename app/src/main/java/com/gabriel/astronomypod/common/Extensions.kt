package com.gabriel.astronomypod.common

import android.app.Application
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

fun ImageView.loadUrl(
    url: String,
    scaleType: ScaleType = ScaleType.NONE,
    onFinish: (String?) -> Unit
) {

    Glide.with(this)
        .load(Uri.parse(url))
        .apply {
            when (scaleType) {
                ScaleType.CENTER_CROP -> centerCrop()
                ScaleType.FIT_CENTER -> fitCenter()
                ScaleType.CENTER_INSIDE -> centerInside()
                ScaleType.NONE -> {
                    //Do nothing
                }
            }
        }
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                onFinish(e?.localizedMessage)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onFinish(null)
                return false
            }
        })
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

fun Animation.onComplete(complete: () -> Unit) {
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {

        }

        override fun onAnimationEnd(animation: Animation?) {
            complete()
        }

        override fun onAnimationStart(animation: Animation?) {

        }
    })
}

fun AndroidViewModel.getString(@StringRes res: Int) = getApplication<Application>().getString(res)