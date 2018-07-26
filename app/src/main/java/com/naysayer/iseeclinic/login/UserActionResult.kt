package com.naysayer.iseeclinic.login

interface UserActionResult {
    fun successfullyAuth()

    fun unsuccessfullyAuth()

    fun unsuccessfullyAuth(exception: Exception)
}