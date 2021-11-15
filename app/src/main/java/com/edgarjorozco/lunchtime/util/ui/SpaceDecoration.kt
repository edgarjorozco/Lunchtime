package com.edgarjorozco.lunchtime.util.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class SpaceDecoration(
    private val verticalSpaceHeight: Int,
    private val spaceOnFirstItem: Boolean = true,
    private val spaceOnLastItem: Boolean = true) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        if (position == 0 && spaceOnFirstItem) outRect.top = verticalSpaceHeight


        if (!spaceOnLastItem && parent.adapter != null && position == parent.adapter!!.itemCount - 1) outRect.bottom = 0
        else outRect.bottom = verticalSpaceHeight
    }
}