package com.nikita.demoapplication.app.framework.datasource.api.http.exception

/**
 * handles exception message based on exception type
 */
class BasicException {

    //    var responseCode = 0
//    private var msg: String? = null
    private val serverError = "Could not connect to server"
    var listOfError = mutableListOf<ApiException>()
        private set


    constructor(code: Int, message: String) {
//        msg = message
//        responseCode=code
        listOfError.add(ApiException(code, message))
    }

    constructor(code: Int) {
        listOfError.add(ApiException(code, serverError))
    }


    fun getError(): ApiException {
        return listOfError[0]
    }
}