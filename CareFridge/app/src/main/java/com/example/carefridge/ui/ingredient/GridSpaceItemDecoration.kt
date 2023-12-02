package com.example.carefridge.ui.ingredient

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpaceItemDecoration(private val spanCount: Int, private val space: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount + 1      // 1부터 시작

//        // 첫 행 제외하고 상단 여백 추가
//        if (position >= spanCount) {
//            outRect.top = space
//        }
//        outRect.bottom = space
        // 첫번째 열이면 우측 여백 추가, 두번째 열이면 좌측 여백 추가
        if (column == 1) {
            outRect.right = space
        } else {
            outRect.left = space
        }
    }
}