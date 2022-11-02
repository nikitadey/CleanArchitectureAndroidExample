package com.nikita.demoapplication.core.data.datasource


import androidx.lifecycle.LiveData
import com.nikita.demoapplication.app.framework.datasource.api.http.ApiResponse
import com.nikita.demoapplication.core.domain.CryptoCandleValue
import com.nikita.demoapplication.core.domain.CryptoExchange
import com.nikita.demoapplication.core.domain.CryptoName
import com.nikita.demoapplication.core.domain.CryptoTrade

interface CryptoDatasource {

    suspend fun fetchCryptoTrades(symbol: String): LiveData<CryptoTrade>
    suspend fun fetchCryptoList(symbol:String):ApiResponse<List<CryptoName>>
    suspend fun fetchCryptoExchangeList():ApiResponse<List<CryptoExchange>>
    suspend fun fetchCryptoCandle(symbol:String,from:String,to:String ):ApiResponse<List<CryptoCandleValue>>

}