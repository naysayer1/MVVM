package com.naysayer.iseeclinic

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog

class Dialogs(_context: Context) {
    private var context = _context


    fun resetPasswordDialog(onDialogButtonsClick: OnDialogButtonsClick): MaterialDialog {
        //TODO input dialog or show current email in this dialog
        return MaterialDialog.Builder(context)
                .content("Сбросить пароль")
                .positiveText("Да")
                .negativeText("Нет")
                .onPositive({ _, _ -> onDialogButtonsClick.onPositiveClick() })
                .onNegative({ dialog, _ -> onDialogButtonsClick.onNegativeClick(dialog) })
                .show()
    }
}

interface OnDialogButtonsClick {
    fun onPositiveClick() {}
    fun onNegativeClick(dialog: MaterialDialog) {
        dialog.dismiss()
    }
}


