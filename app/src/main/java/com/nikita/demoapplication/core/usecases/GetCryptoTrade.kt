package com.nikita.demoapplication.core.usecases

import com.nikita.demoapplication.core.data.repositories.CryptoRepository

class GetCryptoTrade(private val cryptoRepository: CryptoRepository) {

    suspend operator fun invoke (symbol:String) = cryptoRepository.fetchTrade(symbol)
}