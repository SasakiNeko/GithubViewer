package com.sasaki.githubviewer.presentation.scroll

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


abstract class RecyclerViewScroller(private val linearLayoutManager: LinearLayoutManager): RecyclerView.OnScrollListener() {

    private var isListBottom = true
    var previousTotalCount = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
        val itemCount = linearLayoutManager.itemCount
        val childCount = recyclerView.childCount

        if (isListBottom && (itemCount > previousTotalCount)) {
            isListBottom = false
            previousTotalCount = itemCount
        }

        if (!isListBottom && (itemCount == childCount + firstVisibleItemPosition)) {
            reachBottomOfList()
            isListBottom = true
        }
    }

    abstract fun reachBottomOfList()
}