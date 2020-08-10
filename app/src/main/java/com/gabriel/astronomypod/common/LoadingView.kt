package com.gabriel.astronomypod.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.gabriel.astronomypod.R
import kotlinx.android.synthetic.main.layout_loading.view.*

class LoadingView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val anim1 by lazy {
        ivShape1.animate().rotationBy(360f).setDuration(1000)
    }
    private val anim2 by lazy {
        ivShape2.animate().rotationBy(360f).setDuration(1000)
    }
    private val anim3 by lazy {
        ivShape3.animate().rotationBy(360f).setDuration(1000)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_loading, this)
    }

    fun startLoadAnimation() {
        anim1.onComplete {
            anim2.start()
            anim2.onComplete {
                anim3.start()
                anim3.onComplete {
                    anim1.start()
                }
            }
        }
        anim1.start()
    }

    fun setLoadingText(loadingMessage: String) {
        loadText.text = loadingMessage
    }

    fun stopLoadAnimation() {
        anim1.cancel()
        anim2.cancel()
        anim3.cancel()
    }

}