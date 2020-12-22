package com.simcik.branchassessment.custom.bindingAdapters

import android.animation.ValueAnimator
import android.view.View
import androidx.databinding.BindingAdapter


@BindingAdapter("bind:noData")
fun toggleVisibility(view: View, list: List<Any>?) {

    val show = ValueAnimator.ofFloat(view.alpha, 1.0f).apply {
        duration = 750L
        addUpdateListener {
            val float = it.animatedValue as Float
            view.alpha = float
        }
    }

    val hide = ValueAnimator.ofFloat(view.alpha, 0.0f).apply {
        duration = 750L
        addUpdateListener {
            val float = it.animatedValue as Float
            view.alpha = float
        }
    }

    when {
        list.isNullOrEmpty() -> show.start()
        else -> hide.start()
    }
}