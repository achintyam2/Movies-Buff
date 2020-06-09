package com.example.movies.network

import com.google.gson.annotations.SerializedName

data class ErrorResponse(@field:SerializedName("status") val status: Int,
                         @field:SerializedName("message") val message: String,
                         @field:SerializedName("timestamp") val timeStamp: Long)