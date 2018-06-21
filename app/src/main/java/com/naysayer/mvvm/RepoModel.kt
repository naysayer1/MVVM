package com.naysayer.mvvm

import android.os.Handler

class RepoModel {
    fun refreshData(onDataReadyCallback: OnDataReadyCallback) {
        Handler().postDelayed({ onDataReadyCallback.onDataReady("new data") }, 2000)
    }
}

interface OnDataReadyCallback {
    fun onDataReady(data: String)
}