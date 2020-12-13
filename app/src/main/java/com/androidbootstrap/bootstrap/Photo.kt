package com.androidbootstrap.bootstrap

import com.google.gson.annotations.SerializedName

data class Photo(
        @SerializedName("filename")
        var filename: String? = null
)