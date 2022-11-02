package com.nikita.demoapplication.app.framework.datasource.api.http.exception


import com.nikita.demoapplication.app.framework.datasource.api.http.ApiResponse
import com.nikita.demoapplication.app.framework.datasource.api.http.ResponseCode

class SourCastException : ApiResponse.Failure(BasicException(ResponseCode.INVALID_DATA, "Make sure that the specified generic type and retrofit response correspond"))