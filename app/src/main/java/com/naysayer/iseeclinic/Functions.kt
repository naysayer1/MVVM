package com.naysayer.iseeclinic

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast

fun toast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}