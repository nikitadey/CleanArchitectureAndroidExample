package com.nikita.demoapplication.app.framework.di

import com.nikita.demoapplication.app.framework.datasource.api.http.ApiClient
import com.nikita.demoapplication.app.framework.datasource.api.http.ApiClientImpl
import com.nikita.demoapplication.app.framework.datasource.api.websocket.WebSocketManager
import com.nikita.demoapplication.app.framework.datasource.datasourceImpl.CryptoDatasourceImpl
import com.nikita.demoapplication.core.data.datasource.CryptoDatasource
import com.nikita.demoapplication.core.data.repositories.CryptoRepository
import com.nikita.demoapplication.core.usecases.GetCryptoCandle
import com.nikita.demoapplication.core.usecases.GetCryptoExchange
import com.nikita.demoapplication.core.usecases.GetCryptoList
import com.nikita.demoapplication.core.usecases.GetCryptoTrade
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {


    single {
        CryptoDatasourceImpl(get(),get())

    } bind CryptoDatasource::class


    single {
       WebSocketManager()

    }
    single {
        ApiClientImpl.api
    } bind ApiClient::class

    single {
        CryptoRepository(get())

    }

    single {
        GetCryptoExchange(get())

    }

    single {
        GetCryptoList(get())

    }

    single{
        GetCryptoCandle(get())
    }

    single{
        GetCryptoTrade(get())
    }

}