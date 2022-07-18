package com.yuoyama12.decidepickingorderapp.preference

import android.content.Context
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.yuoyama12.decidepickingorderapp.R

private const val COLOR_CIRCLE_ID_HEADER = "color_"
class ColorSelectorPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
): Preference(context, attrs, defStyleAttr) {

    init {
        layoutResource = R.layout.preference_color_selector
    }

    private val sharedPreference = GeneralPreferenceFragment.getSharedPreference(context)

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        setViews(holder)
    }

    private fun setViews(holder: PreferenceViewHolder) {
        val colorCircleCount = holder.itemView.findViewById<ConstraintLayout>(holder.itemView.id).childCount
        val colorCircleList: ArrayList<View> = getColorCircleList(holder,colorCircleCount)

        colorCircleList.forEach { colorCircle ->
            val circleNumber = (colorCircleList.indexOf(colorCircle)) + 1
            val keyForSharedPref = COLOR_CIRCLE_ID_HEADER + circleNumber.toString()
            colorCircle.background.setTint(getRestoredOrDefaultColor(keyForSharedPref))

            colorCircle.setOnClickListener {
                createColorSelectorDialog(it, circleNumber)
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

    private fun createColorSelectorDialog(imageView: View, number: Int){
        var selectedColor: Int? = null

        ColorPickerDialogBuilder
            .with(context)
            .setTitle(context.getString(R.string.color_selector_dialog_title))
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(10)
            .setOnColorSelectedListener {
                selectedColor = it
            }
            .setOnColorChangedListener {
                selectedColor = it
            }
            .setPositiveButton(android.R.string.ok){ dialog,_,_ ->
                if (selectedColor != null){
                    imageView.background.setTint(selectedColor!!)
                    val keyForSharedPref = COLOR_CIRCLE_ID_HEADER + number.toString()

                    setColorDataInSharedPreference(keyForSharedPref, selectedColor!!)
                }

                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel){ dialog,_ ->
                dialog.dismiss()
            }
            .showColorPreview(isOrientationPortrait())
            .showAlphaSlider(false)
            .build()
            .show()
    }

    private fun isOrientationPortrait(): Boolean {
        val orientation = context.resources.configuration.orientation
        return orientation == ORIENTATION_PORTRAIT
    }

    private fun setColorDataInSharedPreference(key: String, color: Int) {
            val editor = sharedPreference.edit()
            editor.putInt(key, color).apply()
    }

}