package com.yuoyama12.decidepickingorderapp.preference

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.dialogs.LicenseDialog

class GeneralPreferencesFragment : PreferenceFragmentCompat() {

    companion object {
        fun getSharedPreference(context: Context): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.action_bar)
        actionBar?.title = getString(R.string.preference_action_bar_title)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_general_preference, rootKey)

        val notificationColorCheckBox = findPreference<CheckBoxPreference>(getString(R.string.use_of_notification_color_key))
        val notificationColor = findPreference<NotificationColorPreference>(getString(R.string.notification_color_selector_preference_key))

        notificationColorCheckBox?.apply {
            notificationColor?.isVisible = this.isChecked

            setOnPreferenceChangeListener { _, newValue ->
                notificationColor?.isVisible = newValue as Boolean
                true
            }
        }

        val licensePreference = findPreference<Preference>(getString(R.string.license_key))
        showLicenseDialog(licensePreference)

    }


    private fun showLicenseDialog(preference: Preference?) {
        preference?.setOnPreferenceClickListener {
            val dialog = LicenseDialog()
            dialog.show(childFragmentManager, null)

            true
        }
    }


}