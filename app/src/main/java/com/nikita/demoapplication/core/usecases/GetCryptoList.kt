package com.nikita.demoapplication.core.usecases

import com.nikita.demoapplication.core.data.repositories.CryptoRepository

class GetCryptoList(private val cryptoRepository: CryptoRepository) {

    suspend operator fun invoke (symbol:String) = cryptoRepository.fetchAllCrypto(symbol)
}