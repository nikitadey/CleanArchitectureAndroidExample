package com.nikita.demoapplication.core.usecases

import com.nikita.demoapplication.core.data.repositories.CryptoRepository

class GetCryptoCandle(private val cryptoRepository: CryptoRepository)  {

    suspend operator fun invoke(symbol:String, from:String, to:String) = cryptoRepository.fetchCryptoCandle(symbol, from, to)
}