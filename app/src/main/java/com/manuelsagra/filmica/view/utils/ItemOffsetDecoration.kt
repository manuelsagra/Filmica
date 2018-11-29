package com.manuelsagra.filmica.view.utils

import android.graphics.Rect
import android.support.annotation.DimenRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

class ItemOffsetDecoration(@DimenRes val offsetId: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val offset = view.context.resources.getDimensionPixelSize(offsetId)
        val position = parent.getChildAdapterPosition(view)

        if (parent.layoutManager is GridLayoutManager) {
            val columns = (parent.layoutManager as GridLayoutManager).spanCount

            val column = position % columns
            val row = (position / columns).toInt()

            val topOffset = if (row == 0) offset else 0
            val leftOffset = if (column == 0) offset else 0

            outRect.set(leftOffset, topOffset, offset, offset)
        } else if (parent.layoutManager is LinearLayoutManager) {
            val topOffset = if (position == 0) offset else 0

            outRect.set(offset, topOffset, offset, offset)
        }
    }
}