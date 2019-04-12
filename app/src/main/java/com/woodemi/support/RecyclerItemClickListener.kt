package com.woodemi.support

import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

interface RecyclerItemClickListener {
    fun onItemClick(rv: RecyclerView, view: View, position: Int) {
        // A convenience empty implementation
    }

    fun onItemLongClick(rv: RecyclerView, view: View, position: Int) {
        // A convenience empty implementation
    }
}

fun RecyclerView.setRecyclerItemListener(listener: RecyclerItemClickListener) {
    addOnItemTouchListener(RecyclerItemGestureListener(this, listener))
}

fun RecyclerView.onItemClick(handleOnItemClick: (RecyclerView, View, Int) -> Unit) {
    setRecyclerItemListener(object : RecyclerItemClickListener {
        override fun onItemClick(rv: RecyclerView, view: View, position: Int) {
            handleOnItemClick(rv, view, position)
        }
    })
}

fun RecyclerView.onItemLongClick(handleOnItemLongClick: (RecyclerView, View, Int) -> Unit) {
    setRecyclerItemListener(object : RecyclerItemClickListener {
        override fun onItemLongClick(rv: RecyclerView, view: View, position: Int) {
            handleOnItemLongClick(rv, view, position)
        }
    })
}

private class RecyclerItemGestureListener(
        val recyclerView: RecyclerView,
        private val clickListener: RecyclerItemClickListener
) : GestureDetector.SimpleOnGestureListener(), RecyclerView.OnItemTouchListener {
    private var gestureDetector = GestureDetector(recyclerView.context, this)

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        rv.findChildViewUnder(e.x, e.y)?.let { gestureDetector.onTouchEvent(e) }
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return true
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        recyclerView.findChildViewUnder(e.x, e.y)?.let {
            clickListener.onItemClick(recyclerView, it, recyclerView.layoutManager.getPosition(it))
        }
        return true
    }

    override fun onLongPress(e: MotionEvent) {
        recyclerView.findChildViewUnder(e.x, e.y)?.let {
            clickListener.onItemLongClick(recyclerView, it, recyclerView.layoutManager.getPosition(it))
        }
    }
}