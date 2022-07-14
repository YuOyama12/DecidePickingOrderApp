package com.yuoyama12.decidepickingorderapp.preference

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.yuoyama12.decidepickingorderapp.R

class GeneralPreferenceFragment : PreferenceFragmentCompat() {

    companion object {
        fun getSharedPreference(context: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.action_bar)
        actionBar?.title = getString(R.string.preference_action_bar_title)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_general_preference, rootKey)
    }

}