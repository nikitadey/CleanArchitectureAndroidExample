package com.nikita.demoapplication.app.framework.datasource.api.model

import com.google.gson.annotations.SerializedName
import com.nikita.demoapplication.app.framework.mappers.DomainModel
import com.nikita.demoapplication.core.domain.CryptoName
import com.nikita.demoapplication.core.domain.CryptoTrade

data class CryptoTrade(
    @SerializedName("p")val price: String,
    @SerializedName("t")val time: String,
    @SerializedName("v")val volume: Float,
    @SerializedName("s")val symbol: String
) : DomainModel<com.nikita.demoapplication.core.domain.CryptoTrade> {
    override fun toDomain() = CryptoTrade(price.toFloat(),time.toLong(),symbol,volume)
}
