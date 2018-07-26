package com.naysayer.iseeclinic.dialogs

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.InputType
import com.afollestad.materialdialogs.MaterialDialog
import com.naysayer.iseeclinic.R

class Dialogs(_context: Context) {
    private var context = _context

    fun resetPasswordDialog(onDialogButtonsClick: OnDialogButtonsClick): MaterialDialog {
        return MaterialDialog.Builder(context)
                .title(R.string.title_forgot_password_dialog)
                .content(R.string.content_forgot_password_dialog)
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .input(R.string.hint_edit_text_forgot_password_dialog, R.string.prefill_edit_text_forgot_password_dialog, false) { _, c -> onDialogButtonsClick.onInput(c) }
                .widgetColor(ContextCompat.getColor(context, R.color.primaryColor))
                .positiveText(R.string.positive_forgot_password_dialog)
                .negativeText(R.string.negative_forgot_password_dialog)
                .positiveColorRes(R.color.primaryColor)
                .onNegative { dialog, _ -> onDialogButtonsClick.onNegativeClick(dialog) }
                .negativeColorRes(R.color.primaryColor)
                .show()
    }
}