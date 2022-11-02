package com.nikita.demoapplication.app.framework.datasource.api.model

import com.nikita.demoapplication.app.framework.mappers.DomainModel
import com.nikita.demoapplication.core.domain.CryptoName

data class CryptoSymbol(
    val description: String,
    val displaySymbol: String,
    val symbol: String
) : DomainModel<CryptoName> {
    override fun toDomain() = CryptoName(description, symbol)
}
