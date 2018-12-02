package com.manuelsagra.filmica.view.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.widget.ImageView

class FadeImageView: ImageView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setImageBitmap(bm: Bitmap?) {
        animate()
                .alpha(0.0f)
                .setDuration(120)
                .setListener(AnimatorEndListener() {
                    super@FadeImageView.setImageBitmap(bm)
                    animate()
                            .alpha(1f)
                            .setDuration(240)
                })
    }
}