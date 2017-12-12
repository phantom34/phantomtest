package test.phantom.com.p90.ui.main

import android.view.View

import test.phantom.com.p90.entity.BookShelfBean

interface OnItemClickListener {
    fun toSearch()

    fun onClick(bookShelfBean: BookShelfBean, index: Int)

    fun onLongClick(view: View, bookShelfBean: BookShelfBean, index: Int)
}