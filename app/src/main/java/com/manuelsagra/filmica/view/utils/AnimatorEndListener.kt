package com.manuelsagra.filmica.view.utils

import android.animation.Animator

class AnimatorEndListener(
    val callback: ((Animator) -> Unit)
): Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator) {
        callback.invoke(animation)
    }

    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationStart(animation: Animator?) {
    }

}