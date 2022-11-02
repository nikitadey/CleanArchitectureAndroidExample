package com.nikita.demoapplication.app.framework.datasource.api.http.exception

import com.nikita.demoapplication.app.framework.datasource.api.http.ApiResponse

class ApiResponseException(basicException: BasicException): ApiResponse.Failure(basicException) {
}