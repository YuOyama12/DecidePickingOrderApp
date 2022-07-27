package com.yuoyama12.decidepickingorderapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.yuoyama12.decidepickingorderapp.preference.GeneralPreferenceFragment

private const val SORTING_METHODS_BUNDLE_KEY = "sortMethods"
private const val SORT_OBJECT_BUNDLE_KEY = "sortObject"
const val GROUP_SORTING_PREF_KEY = "group_sorting"
const val MEMBER_SORTING_PREF_KEY = "member_sorting"

enum class SortObject {
    GROUP,
    MEMBER
}
class SortDialog : DialogFragment() {

    companion object {
        fun create(sortObject: SortObject, sortingMethods: Array<String>): SortDialog {
            return SortDialog().apply {
                val bundle = Bundle()
                bundle.putSerializable(SORT_OBJECT_BUNDLE_KEY, sortObject)
                bundle.putStringArray(SORTING_METHODS_BUNDLE_KEY, sortingMethods)

                arguments = bundle
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val sortObject = arguments?.getSerializable(SORT_OBJECT_BUNDLE_KEY) as SortObject
        val sortingMethodList = arguments?.getStringArray(SORTING_METHODS_BUNDLE_KEY) as Array<*>

        val defaultSelectedPosition = getDefaultSelectedPosition(sortObject)

        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.select_dialog_singlechoice,
            sortingMethodList)

        val dialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)

            builder.setSingleChoiceItems(adapter, defaultSelectedPosition) { dialog, position ->
                restoreInSharedPref(position, sortObject)
                dialog.dismiss()
                }

            builder.create()

        } ?: throw  IllegalStateException("Activity cannot be null")

        return dialog
    }

    private fun getDefaultSelectedPosition(sortObject: SortObject): Int {
        val sharedPref = GeneralPreferenceFragment.getSharedPreference(requireContext())

        return when (sortObject) {
            SortObject.GROUP -> sharedPref.getInt(GROUP_SORTING_PREF_KEY, 0)
            SortObject.MEMBER -> sharedPref.getInt(MEMBER_SORTING_PREF_KEY, 0)
        }
    }

    private fun restoreInSharedPref(id: Int, sortObject: SortObject) {
        val sharedPref = GeneralPreferenceFragment.getSharedPreference(requireContext())

        sharedPref.edit().apply {
            when (sortObject) {
                SortObject.GROUP -> putInt(GROUP_SORTING_PREF_KEY, id)
                SortObject.MEMBER -> putInt(MEMBER_SORTING_PREF_KEY, id)
            }
        }.apply()
    }

}