package com.itis.android2.rv.itemDecorators

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecorator(
    context: Context,
    spacing: Float = 16f
) : RecyclerView.ItemDecoration() {

    private val spacingPx: Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        spacing,
        context.resources.displayMetrics
    ).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spacingMiddle = (spacingPx * 0.25).toInt()
        val spacingTopBottom = (spacingPx * 0.5).toInt()
        val viewHolder = parent.getChildViewHolder(view)

        val currentPosition = parent.getChildAdapterPosition(view).takeIf {
            it != RecyclerView.NO_POSITION
        } ?: viewHolder.oldPosition

        when (currentPosition) {
            0 -> {
                outRect.top = spacingTopBottom
                outRect.bottom = spacingMiddle
            }
            state.itemCount - 1 -> {
                outRect.top = spacingMiddle
                outRect.bottom = spacingTopBottom
            }
            else -> {
                outRect.top = spacingMiddle
                outRect.bottom = spacingMiddle
            }
        }
        outRect.left = spacingPx
        outRect.right = spacingPx
    }
}
