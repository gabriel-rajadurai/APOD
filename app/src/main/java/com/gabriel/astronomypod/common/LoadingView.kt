package com.gabriel.astronomypod.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
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
        AnimationUtils.loadAnimation(context, R.anim.rotate_animation)
            .apply { fillAfter = true }
    }
    private val anim2 by lazy {
        AnimationUtils.loadAnimation(context, R.anim.rotate_animation)
            .apply { fillAfter = true }
    }
    private val anim3 by lazy {
        AnimationUtils.loadAnimation(context, R.anim.rotate_animation)
            .apply { fillAfter = true }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_loading, this)
    }

    fun startLoadAnimation() {

        ivShape3.startAnimation(anim1)

        anim1.onComplete {
            ivShape3.clearAnimation()
            ivShape1.startAnimation(anim2)
            anim2.onComplete {
                ivShape1.clearAnimation()
                ivShape2.startAnimation(anim3)
                anim3.onComplete {
                    ivShape2.clearAnimation()
                    ivShape3.startAnimation(anim1)
                }
            }
        }
    }

    fun setLoadingText(loadingMessage: String) {
        loadText.text = loadingMessage
    }

    fun stopLoadAnimation() {
        anim1.setAnimationListener(null)
        ivShape3.clearAnimation()
        anim2.setAnimationListener(null)
        ivShape1.clearAnimation()
        anim3.setAnimationListener(null)
        ivShape2.clearAnimation()
    }

}