package com.nikita.demoapplication.app.framework.datasource.api.http.exception


import com.nikita.demoapplication.app.framework.datasource.api.http.ApiResponse
import com.nikita.demoapplication.app.framework.datasource.api.http.ResponseCode

class NoDataException : ApiResponse.Failure(BasicException(ResponseCode.INVALID_DATA, "No Data"))