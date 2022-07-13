package com.yuoyama12.decidepickingorderapp.preference

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.yuoyama12.decidepickingorderapp.R

class GeneralPreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.action_bar)
        actionBar?.title = getString(R.string.preference_action_bar_title)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_general_preference, rootKey)
    }

}