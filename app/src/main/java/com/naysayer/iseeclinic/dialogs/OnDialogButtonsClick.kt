package com.naysayer.iseeclinic.dialogs

import com.afollestad.materialdialogs.MaterialDialog

interface OnDialogButtonsClick {
    fun onPositiveClick() {}

    fun onNegativeClick(dialog: MaterialDialog) {
        dialog.dismiss()
    }

    fun onInput(c: CharSequence) {}
}