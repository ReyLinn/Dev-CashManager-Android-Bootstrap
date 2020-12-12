package com.androidbootstrap.bootstrap.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
        val Id: String,
        val UserName: String,
        val Password: String,
)