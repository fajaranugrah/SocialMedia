package com.android.socialmedia.UtilitiesManager

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

class TouchableRelativeLayout : RelativeLayout {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun performClick(): Boolean {
        return super.performClick()
    }
}