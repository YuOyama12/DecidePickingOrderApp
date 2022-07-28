package com.yuoyama12.decidepickingorderapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.yuoyama12.decidepickingorderapp.preference.datastore.SortingPreferencesDataStoreRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private const val SORTING_METHODS_BUNDLE_KEY = "sortMethods"
private const val SORT_OBJECT_BUNDLE_KEY = "sortObject"
const val GROUP_SORTING_PREF_KEY = "group_sorting"
const val MEMBER_SORTING_PREF_KEY = "member_sorting"

enum class SortObject {
    GROUP,
    MEMBER
}

@AndroidEntryPoint
class SortDialog : DialogFragment() {

    @Inject lateinit var sortingPreferencesDataStoreRepository: SortingPreferencesDataStoreRepository

    companion object {
        fun create(sortObject: SortObject, sortingMethods: Array<String>): SortDialog {
            return SortDialog().apply {
                val bundle = Bundle()
                bundle.putSerializable(SORT_OBJECT_BUNDLE_KEY, sortObject)
                bundle.putStringArray(SORTING_METHODS_BUNDLE_KEY, sortingMethods)

                arguments = bundle
            }
        }

        object Group {
            const val SORT_BY_CREATION_TIME_LATEST_FIRST = 1
            const val SORT_BY_ALPHABETICAL_ORDER = 2
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val sortObject = arguments?.getSerializable(SORT_OBJECT_BUNDLE_KEY) as SortObject
        val sortingMethodList = arguments?.getStringArray(SORTING_METHODS_BUNDLE_KEY) as Array<*>

        val defaultSelectedPosition = runBlocking {
            getDefaultSelectedPosition(sortObject)
        }

        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.select_dialog_singlechoice,
            sortingMethodList)

        val dialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)

            builder.setSingleChoiceItems(adapter, defaultSelectedPosition) { dialog, position ->
                lifecycleScope.launch{
                    restoreInDataStore(position, sortObject)
                    dialog.dismiss()
                }
            }

            builder.create()

        } ?: throw  IllegalStateException("Activity cannot be null")

        return dialog
    }

    private suspend fun getDefaultSelectedPosition(sortObject: SortObject): Int {
        return when (sortObject) {
            SortObject.GROUP -> sortingPreferencesDataStoreRepository.getGroupSortingId()
            SortObject.MEMBER -> sortingPreferencesDataStoreRepository.getMemberSortingId()
        }.first()
    }

    private suspend fun restoreInDataStore(id: Int, sortObject: SortObject) {
        when (sortObject) {
            SortObject.GROUP -> {
                sortingPreferencesDataStoreRepository.setGroupSortingId(id)
            }
            SortObject.MEMBER -> {
                sortingPreferencesDataStoreRepository.setMemberSortingId(id)
            }
        }
    }

}