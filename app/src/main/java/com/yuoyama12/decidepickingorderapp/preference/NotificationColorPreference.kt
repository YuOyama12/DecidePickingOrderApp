package com.yuoyama12.decidepickingorderapp.preference

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.fragment.app.findFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceViewHolder
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.dialogs.ColorPickerDialog
import com.yuoyama12.decidepickingorderapp.dialogs.InformationDialog
import com.yuoyama12.decidepickingorderapp.fragments.removeBulletPoint

class NotificationColorPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): Preference(context, attrs, defStyleAttr) {

    init {
        layoutResource = R.layout.preference_notification_color_selector
    }

    private val sharedPreference = GeneralPreferencesFragment.getSharedPreference(context)

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        setViews(holder)
    }

    private fun setViews(holder: PreferenceViewHolder) {
        val notificationColorCircle = holder.findViewById(R.id.notification_color)
        val helpIcon = holder.findViewById(R.id.notification_color_help)

        helpIcon.setOnClickListener {
            val childFragmentManager = holder.itemView.findFragment<PreferenceFragmentCompat>().childFragmentManager
            val title = getString(R.string.what_notification_color_is_text).removeBulletPoint()

            InformationDialog
                .create(title, R.layout.dialog_about_notification_color)
                .show(childFragmentManager, null)
        }

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
        val keyForSharedPref = getString(R.string.notification_color_selector_preference_key)

        val dialog = ColorPickerDialog(context)
            .createDetermineColorDialog(imageView, keyForSharedPref)

        dialog.show()
    }

    private fun getString(id: Int, vararg: Any? = null): String{
        return if (vararg == null) {
            context.getString(id)
        }else{
            context.getString(id,vararg)
        }
    }

    fun getNotificationColor(): Int =
        getRestoredOrDefaultColor(getString(R.string.notification_color_selector_preference_key))

}