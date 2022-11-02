package com.nikita.demoapplication.core.domain

import com.google.gson.annotations.SerializedName

data class CryptoTrade(
    val lastPrice: Float,
    val timestamp: Long,
    val symbol: String,
    val volume: Float,
)
