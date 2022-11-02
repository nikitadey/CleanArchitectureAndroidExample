package com.nikita.demoapplication.app.framework.datasource.api.http

import com.nikita.demoapplication.app.framework.datasource.api.model.CryptoCandleRaw
import com.nikita.demoapplication.app.framework.datasource.api.model.CryptoSymbol
import com.nikita.demoapplication.app.framework.datasource.api.model.CryptoSymbolResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {

    @GET("crypto/exchange/?token=cd1ee22ad3i7tm8rq110cd1ee22ad3i7tm8rq11g")
    suspend fun fetchExchangeList(): Response<List<String>>

    @GET("crypto/symbol/?token=cd1ee22ad3i7tm8rq110cd1ee22ad3i7tm8rq11g")
    suspend fun fetchCryptoList(@Query("exchange") symbol:String): Response<List<CryptoSymbol>>

    @GET("crypto/candle?resolution=M&token=cd1ee22ad3i7tm8rq110cd1ee22ad3i7tm8rq11g")
    suspend fun fetchCryptoCandle(@Query("symbol") symbol:String,
                                  @Query("from") from:String,
                                  @Query("to") to:String): Response<CryptoCandleRaw>

}