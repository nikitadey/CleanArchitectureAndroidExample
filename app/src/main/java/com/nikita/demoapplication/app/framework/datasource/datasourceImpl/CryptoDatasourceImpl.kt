package com.nikita.demoapplication.app.framework.datasource.datasourceImpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikita.demoapplication.app.framework.datasource.api.http.ApiClient
import com.nikita.demoapplication.app.framework.datasource.api.http.ApiResponse
import com.nikita.demoapplication.app.framework.datasource.api.http.ResponseCode
import com.nikita.demoapplication.app.framework.datasource.api.http.exception.BasicException
import com.nikita.demoapplication.app.framework.datasource.api.http.exception.NoDataException
import com.nikita.demoapplication.app.framework.datasource.api.http.exception.SourCastException
import com.nikita.demoapplication.app.framework.datasource.api.model.CryptoCandleRaw
import com.nikita.demoapplication.app.framework.datasource.api.model.CryptoSymbol
import com.nikita.demoapplication.app.framework.datasource.api.model.CryptoSymbolResponse
import com.nikita.demoapplication.app.framework.datasource.api.websocket.MessageListener
import com.nikita.demoapplication.app.framework.datasource.api.websocket.WebSocketManager
import com.nikita.demoapplication.core.data.datasource.CryptoDatasource
import com.nikita.demoapplication.core.domain.CryptoCandleValue
import com.nikita.demoapplication.core.domain.CryptoExchange
import com.nikita.demoapplication.core.domain.CryptoName
import com.nikita.demoapplication.core.domain.CryptoTrade
import com.nikita.demoapplication.utils.GsonUtils
import retrofit2.Response

class CryptoDatasourceImpl(
    private val apiService: ApiClient,
    private val webSocketManager: WebSocketManager
) : CryptoDatasource {
    override suspend fun fetchCryptoTrades(symbol: String): LiveData<CryptoTrade> {

        val response: MutableLiveData<CryptoTrade> = MutableLiveData<CryptoTrade>()
        webSocketManager.setListener(object : MessageListener {
            override fun onConnectSuccess() {
                kotlin.run {
                    webSocketManager.sendMessage("{\"type\":\"subscribe\",\"symbol\":\"$symbol\"}")
                }
            }

            override fun onConnectFailed() {

            }

            override fun onClose() {

            }

            override fun onMessage(text: String?) {

                text?.let {
                    val cryptoTradeRawResponse: com.nikita.demoapplication.app.framework.datasource.api.model.CryptoTradeRawResponse? =
                        GsonUtils.fromJson(
                            it,
                            com.nikita.demoapplication.app.framework.datasource.api.model.CryptoTradeRawResponse::class.java
                        )

                    cryptoTradeRawResponse?.let { v ->
                        v.cryptoTradeList?.let {
                            response.postValue(v.cryptoTradeList[0].toDomain())
                        }
                        if (!v.type.equals("trade")) {
                            kotlin.run {
                                webSocketManager.reconnect()
                            }
                        }
                    }
                }


            }

        }

        )
        webSocketManager.init()
        webSocketManager.setUrl("wss://ws.finnhub.io?token=cd1ee22ad3i7tm8rq110cd1ee22ad3i7tm8rq11g")

        kotlin.run {
            webSocketManager.connect()
        }

        return response
    }

    override suspend fun fetchCryptoList(symbol: String): ApiResponse<List<CryptoName>> {

        val retrofitResponse = apiService.fetchCryptoList(symbol);

        var response: Response<List<CryptoSymbol>> = retrofitResponse
        if (response.code() == ResponseCode.SUCCESS) {

            var responseBody: List<CryptoSymbol> = response.body() as List<CryptoSymbol>
            val responseList: List<CryptoName> = responseBody?.map {
                it.toDomain()
            } ?: emptyList()
            return ApiResponse.Success(responseList)
        } else {
            return SourCastException()
        }
    }

    override suspend fun fetchCryptoExchangeList(): ApiResponse<List<CryptoExchange>> {
        val retrofitResponse = apiService.fetchExchangeList();

        var response: Response<List<String>> = retrofitResponse
        if (response.code() == ResponseCode.SUCCESS) {

            var responseBody: List<String> = response.body() as List<String>
            val responseList: List<CryptoExchange> = responseBody?.map {
                CryptoExchange(it)
            } ?: emptyList()
            return ApiResponse.Success(responseList)
        } else {
            return SourCastException()
        }
    }

    override suspend fun fetchCryptoCandle(
        symbol: String,
        from: String,
        to: String
    ): ApiResponse<List<CryptoCandleValue>> {
        val retrofitResponse = apiService.fetchCryptoCandle(symbol, from, to);

        var response: Response<CryptoCandleRaw> = retrofitResponse
        if (response.code() == ResponseCode.SUCCESS) {
            var responseBody: CryptoCandleRaw = response.body() as CryptoCandleRaw

            val responseList: ArrayList<CryptoCandleValue> = ArrayList<CryptoCandleValue>()
            if (responseBody.status.equals("ok")) {
                responseBody.time?.let {
                    for ((index, value) in it.withIndex()) {
                        val cryptoCandleValue = CryptoCandleValue(
                            high = responseBody.high.get(index),
                            low = responseBody.low.get(index),
                            open = responseBody.open.get(index),
                            close = responseBody.closing.get(index),
                            volume = responseBody.volume.get(index),
                            time = value
                        )
                        responseList.add(cryptoCandleValue)
                    }
                    return ApiResponse.Success(responseList)
                } ?: kotlin.run {
                    return NoDataException()

                }


            } else {
                return NoDataException()
            }

        } else {
            return SourCastException()
        }
    }
}