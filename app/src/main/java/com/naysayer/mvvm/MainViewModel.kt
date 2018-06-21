package com.naysayer.mvvm

import android.databinding.ObservableField

class MainViewModel {
    var repoModel: RepoModel = RepoModel()
    val text = ObservableField<String>("Old Data")

    val isLoading = ObservableField<Boolean>(false)

    fun refresh() {
        isLoading.set(true)
        repoModel.refreshData(object : OnDataReadyCallback {
            override fun onDataReady(data: String) {
                isLoading.set(false)
                text.set(data)
            }
        })
    }
}