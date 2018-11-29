package com.manuelsagra.filmica.view.utils

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.manuelsagra.filmica.R

abstract class SwipeToDeleteCallback: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return false
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val itemView = viewHolder.itemView

        // Background
        setupBackground(recyclerView, itemView, dX, c)

        // Icon
        setupIcon(recyclerView, itemView, c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun setupIcon(recyclerView: RecyclerView, itemView: View, c: Canvas) {
        val icon = ContextCompat.getDrawable(recyclerView.context, R.drawable.ic_check)!!
        val iconMargin = (itemView.height - icon.intrinsicHeight) / 3
        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconLeft = itemView.left + iconMargin
        val iconRight = iconLeft + icon.intrinsicWidth
        val iconBottom = iconTop + icon.intrinsicHeight
        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        icon.draw(c)
    }

    private fun setupBackground(recyclerView: RecyclerView, itemView: View, dX: Float, c: Canvas) {
        val color = ContextCompat.getColor(recyclerView.context, R.color.colorPrimaryDark)
        val background = ColorDrawable(color)
        background.setBounds(itemView.left, itemView.top, (itemView.left + dX).toInt(), itemView.bottom)
        background.draw(c)
    }
}