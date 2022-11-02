package com.nikita.demoapplication.app.framework.datasource.api.http

import com.nikita.demoapplication.app.framework.datasource.api.http.exception.BasicException


sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    abstract class Failure(val exception: BasicException) : ApiResponse<Nothing>()
}

