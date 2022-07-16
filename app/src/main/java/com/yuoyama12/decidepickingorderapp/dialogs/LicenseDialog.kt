package com.yuoyama12.decidepickingorderapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.databinding.DialogLicenseBinding

class LicenseDialog : DialogFragment() {

    private var _binding: DialogLicenseBinding? = null
    private val binding: DialogLicenseBinding
        get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogLicenseBinding
            .inflate(requireActivity().layoutInflater)

        binding.licenseView.setLicenses(R.xml.licenses)

        val dialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle(R.string.license_title)
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    dialog.cancel()
                }

            builder.create()

        } ?: throw  IllegalStateException("Activity cannot be null")

        return dialog
    }
}