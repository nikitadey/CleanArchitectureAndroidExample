package com.nikita.demoapplication.core.data.repositories

import com.nikita.demoapplication.core.data.datasource.CryptoDatasource

class CryptoRepository(private val cryptoDatasource: CryptoDatasource) {

    suspend fun fetchAllCrypto(symbol:String) = cryptoDatasource.fetchCryptoList(symbol)
    suspend fun fetchCryptoExchange() = cryptoDatasource.fetchCryptoExchangeList()
    suspend fun fetchCryptoCandle(symbol:String,from:String,to:String ) =
        cryptoDatasource.fetchCryptoCandle(symbol, from, to)

    suspend fun fetchTrade(symbol:String)=cryptoDatasource.fetchCryptoTrades(symbol = symbol)
}