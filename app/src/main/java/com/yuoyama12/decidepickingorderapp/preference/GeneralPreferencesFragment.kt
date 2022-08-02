package com.yuoyama12.decidepickingorderapp.preference

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.navigation.fragment.findNavController
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.yuoyama12.decidepickingorderapp.MainActivity
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.dialogs.LicenseDialog

class GeneralPreferencesFragment : PreferenceFragmentCompat() {

    companion object {
        fun getSharedPreference(context: Context): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val actionBar = MainActivity.getActionBar(requireActivity())
        actionBar.title = getString(R.string.preference_action_bar_title)

        MainActivity.createNavigationIcon(actionBar) {
            findNavController().navigate(R.id.action_generalPreferenceFragment_to_mainFragment)
        }
    }

    private fun showLicenseDialog(preference: Preference?) {
        preference?.setOnPreferenceClickListener {
            val dialog = LicenseDialog()
            dialog.show(childFragmentManager, null)

            true
        }
    }


}