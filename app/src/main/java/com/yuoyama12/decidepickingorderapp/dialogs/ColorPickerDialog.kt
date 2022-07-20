package com.yuoyama12.decidepickingorderapp.dialogs

import android.content.Context
import android.content.res.Configuration
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.preference.GeneralPreferenceFragment

class ColorPickerDialog(private val context: Context) {

    private val resources = context.resources

    fun createDetermineColorDialog(imageView: View, keyForSharedPref: String): AlertDialog {
        var selectedColor: Int? = null
        val title = resources.getString(R.string.color_selector_dialog_title)

        val dialog =
        ColorPickerDialogBuilder
            .with(context)
            .setTitle(title)
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .density(10)
            .setOnColorSelectedListener {
                selectedColor = it
            }
            .setOnColorChangedListener {
                selectedColor = it
            }
            .setPositiveButton(android.R.string.ok) { dialog, _, _ ->
                if (selectedColor != null){
                    imageView.background.setTint(selectedColor!!)
                    setColorDataInSharedPreference(keyForSharedPref, selectedColor!!)
                }

                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { dialog,_ ->
                dialog.dismiss()
            }
            .showColorPreview(isOrientationPortrait())
            .showAlphaSlider(false)
            .build()

        return dialog
    }

    private fun isOrientationPortrait(): Boolean {
        val orientation = resources.configuration.orientation
        return orientation == Configuration.ORIENTATION_PORTRAIT
    }

    private fun setColorDataInSharedPreference(key: String, color: Int) {
        val sharedPreference = GeneralPreferenceFragment.getSharedPreference(context)
        val editor = sharedPreference.edit()
        editor.putInt(key, color).apply()
    }

}