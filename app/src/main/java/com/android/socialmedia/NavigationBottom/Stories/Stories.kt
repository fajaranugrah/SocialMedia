package com.android.socialmedia.NavigationBottom.Stories

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.socialmedia.R
import kotlinx.android.synthetic.main.activity_stories.*

class Stories : AppCompatActivity() {

    val TAG : String = "StoriesOpen"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stories)

        stories?.setVideoPath(getIntent()?.getStringExtra("StoriesUser"))
        stories?.start()
        stories?.setOnTouchListener { v, event ->
            var isReleased = event.action === android.view.MotionEvent.ACTION_UP || event.action === android.view.MotionEvent.ACTION_CANCEL
            var isPressed = event.action === android.view.MotionEvent.ACTION_DOWN

            Log.d(TAG, "onTouch: $isReleased && $isPressed")
            if (isReleased) {
                Log.d(TAG, "onTouch: no touch")
                stories?.start()
            } else if (isPressed) {
                Log.d(TAG, "onTouch: yeah touch")
                stories?.pause()
            }
            true
        }
    }
}