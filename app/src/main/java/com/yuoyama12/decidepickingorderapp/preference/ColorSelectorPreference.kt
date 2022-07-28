package com.yuoyama12.decidepickingorderapp.preference

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.dialogs.ColorPickerDialog

private const val COLOR_CIRCLE_ID_HEADER = "color_"
private const val COLOR_CIRCLE_SIZE = 5
class ColorSelectorPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
): Preference(context, attrs, defStyleAttr) {

    init {
        layoutResource = R.layout.preference_color_selector
    }

    private val sharedPreference = GeneralPreferencesFragment.getSharedPreference(context)

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        setViews(holder)
    }

    private fun setViews(holder: PreferenceViewHolder) {
        val colorCircleSize = holder.itemView.findViewById<ConstraintLayout>(holder.itemView.id).childCount
        val colorCircleList: ArrayList<View> = getColorCircleList(holder, colorCircleSize)

        colorCircleList.forEach { colorCircle ->
            val circleNumber = (colorCircleList.indexOf(colorCircle)) + 1
            val keyForSharedPref = COLOR_CIRCLE_ID_HEADER + circleNumber.toString()
            colorCircle.background.setTint(getRestoredOrDefaultColor(keyForSharedPref))

            colorCircle.setOnClickListener {
                createColorSelectorDialog(it, keyForSharedPref)
            }
        }

    }

    private fun getColorCircleList(holder: PreferenceViewHolder, maxCount: Int): ArrayList<View> {
        val list = arrayListOf<View>()
        for (number in 1..maxCount){
            list.add(getImageViewByNumber(holder, number))
        }
        return list
    }

    private fun getImageViewByNumber(holder: PreferenceViewHolder, number: Int): View {
        val viewId = context.resources.getIdentifier(
            COLOR_CIRCLE_ID_HEADER + number.toString(),
            "id",
            context.packageName
        )
        return holder.findViewById(viewId)
    }


    private fun getRestoredOrDefaultColor(key: String): Int {
        @Suppress("DEPRECATION")
        val defaultColor =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.resources.getColor(getDefaultColorByKey(key), null)
            } else {
                context.resources.getColor(getDefaultColorByKey(key))
            }

        return sharedPreference.getInt(key, defaultColor)
    }

    private fun getDefaultColorByKey(key: String): Int{
        return when(key){
            COLOR_CIRCLE_ID_HEADER + "1" -> R.color.light_blue
            COLOR_CIRCLE_ID_HEADER + "2" -> R.color.light_green
            COLOR_CIRCLE_ID_HEADER + "3" -> R.color.light_yellow
            COLOR_CIRCLE_ID_HEADER + "4" -> R.color.light_pink
            COLOR_CIRCLE_ID_HEADER + "5" -> R.color.light_purple
            else -> R.color.white
        }
    }

    private fun createColorSelectorDialog(imageView: View, keyForSharedPref: String){
        val dialog = ColorPickerDialog(context)
            .createDetermineColorDialog(imageView, keyForSharedPref)

        dialog.show()
    }

    fun getColorList(): ArrayList<Int>{
        val list = arrayListOf<Int>()
        for (number in 1..COLOR_CIRCLE_SIZE){
            val key = COLOR_CIRCLE_ID_HEADER + number.toString()
            val color = getRestoredOrDefaultColor(key)
            list.add(color)
        }
        return list
    }

}