package com.nikita.demoapplication.core.usecases

import com.nikita.demoapplication.core.data.repositories.CryptoRepository

class GetCryptoExchange(private val cryptoRepository: CryptoRepository) {

    suspend operator fun invoke () = cryptoRepository.fetchCryptoExchange()
}