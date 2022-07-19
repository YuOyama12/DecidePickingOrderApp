package com.yuoyama12.decidepickingorderapp.preference

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.yuoyama12.decidepickingorderapp.R

class NotificationColorPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): Preference(context, attrs, defStyleAttr) {

    init {
        layoutResource = R.layout.preference_notification_color_selector
    }

    private val sharedPreference = GeneralPreferenceFragment.getSharedPreference(context)

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        setViews(holder)
    }

    private fun setViews(holder: PreferenceViewHolder) {
        val notificationColorCircle = holder.findViewById(R.id.notification_color)

        notificationColorCircle.apply {
            background.setTint(getRestoredOrDefaultColor(getString(R.string.notification_color_selector_preference_key)))
            setOnClickListener { createColorPickerDialog(it) }
        }

    }

    private fun getRestoredOrDefaultColor(key: String): Int {
        @Suppress("DEPRECATION")
        val defaultColor =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.resources.getColor(getDefaultColor(), null)
            } else {
                context.resources.getColor(getDefaultColor())
            }
        return sharedPreference.getInt(key, defaultColor)
    }

    private fun getDefaultColor(): Int = R.color.white

    private fun createColorPickerDialog(imageView: View){
        var selectedColor: Int? = null

        ColorPickerDialogBuilder
            .with(context)
            .setTitle(getString(R.string.color_selector_dialog_title))
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
                    val keyForSharedPref = getString(R.string.notification_color_selector_preference_key)

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
        return orientation == Configuration.ORIENTATION_PORTRAIT
    }

    private fun setColorDataInSharedPreference(key: String, color: Int) {
        val editor = sharedPreference.edit()
        editor.putInt(key, color).apply()
    }

    private fun getString(id: Int, vararg: Any? = null): String{
        return if (vararg == null) {
            context.getString(id)
        }else{
            context.getString(id,vararg)
        }
    }




}