package com.gabriel.astronomypod.common

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun ImageView.loadUrl(url: String) {
    Glide.with(this)
        .load(Uri.parse(url))
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}