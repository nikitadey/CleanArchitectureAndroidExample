package com.nikita.demoapplication.app.framework.datasource.api.model

import com.google.gson.annotations.SerializedName

data class CryptoTradeRawResponse(@SerializedName("data") val cryptoTradeList:List<CryptoTrade> ,@SerializedName("type") val type:String)
