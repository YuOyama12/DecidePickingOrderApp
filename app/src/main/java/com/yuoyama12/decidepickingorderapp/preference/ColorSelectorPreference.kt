package com.yuoyama12.decidepickingorderapp.preference

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import com.yuoyama12.decidepickingorderapp.R

class ColorSelectorPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): Preference(context, attrs, defStyleAttr) {

    init {
        layoutResource = R.layout.preference_color_selector
    }

}