package com.naysayer.iseeclinic


class Validation {

    fun isEmailValid(email: String): Boolean {
        val indexOfDogSymbol: Int
        val indexOfDotSymbol: Int
        val name: String
        val code: String


        if (email.contains(" ")) {
            return false
        }

        if (!email.contains("@")) {
            return false
        } else {
            indexOfDogSymbol = email.indexOf("@")
            val substring = email.substring(0, indexOfDogSymbol)
            if (substring.length < 2) {
                return false
            }
        }

        name = email.substring(indexOfDogSymbol, email.length)
        if (name.length < 3) {
            return false
        } else {
            if (!name.contains(".")) {
                return false
            } else {
                indexOfDotSymbol = name.indexOf(".")
            }
        }

        code = name.substring(indexOfDotSymbol, name.length)
        return code.length >= 3
    }

    fun isPasswordValid(password: String): Boolean {
        var charCounter = 0
        var digitCounter = 0

        if ((password.contains(" ")) or (password.length < 6)) {
            return false
        }

        for (i in 0 until password.length) {
            val c = password[i]
            when {
                isLetter(c) -> charCounter++
                isDigit(c) -> digitCounter++
                else -> return false
            }
        }
        return charCounter >= 2 && digitCounter >= 1
    }

    private fun isLetter(c: Char): Boolean = Character.toUpperCase(c) in 'A'..'Z'

    private fun isDigit(c: Char): Boolean = c in '0'..'9'
}