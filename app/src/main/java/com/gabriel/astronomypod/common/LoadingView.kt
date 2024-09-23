package com.gabriel.astronomypod.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.gabriel.astronomypod.R
import com.gabriel.astronomypod.databinding.LayoutLoadingBinding

class LoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: LayoutLoadingBinding = LayoutLoadingBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
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

    fun startLoadAnimation() {
        with(binding) {
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

    }

    fun setLoadingText(loadingMessage: String) {
        binding.loadText.text = loadingMessage
    }

    fun stopLoadAnimation() {
        with(binding) {
            anim1.setAnimationListener(null)
            ivShape3.clearAnimation()
            anim2.setAnimationListener(null)
            ivShape1.clearAnimation()
            anim3.setAnimationListener(null)
            ivShape2.clearAnimation()
        }
    }

}