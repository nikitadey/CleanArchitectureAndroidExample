package com.nikita.demoapplication.app.framework.datasource.api.http

import com.nikita.demoapplication.app.framework.datasource.api.http.exception.BasicException


sealed class Status {
    object Uninitialized : Status()
    object Loading : Status()

    object Success : Status()
    data class Error(val error: BasicException) : Status()
}