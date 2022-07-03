package com.yuoyama12.decidepickingorderapp

import android.view.MotionEvent
import android.view.View

class FlickListener(
    private val listener: Listener,
    private val play: Float = flickJudge
) : View.OnTouchListener {

    companion object {
        const val flickJudge: Float = 100f
    }

    private var lastX: Float = 0f

    interface Listener {
        fun onFlickToLeft()
        fun onFlickToRight()
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        view.performClick()

        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchDown(event)
                true
            }
            MotionEvent.ACTION_UP -> {
                touchOff(event)
                true
            }
            else -> false
        }

    }

    private fun touchDown(event: MotionEvent) {
        lastX = event.x
    }

    private fun touchOff(event: MotionEvent) {
        val currentX = event.x

        if (currentX + play < lastX) {
            listener.onFlickToLeft()
        } else if (lastX < currentX - play) {
            listener.onFlickToRight()
        }
    }

}