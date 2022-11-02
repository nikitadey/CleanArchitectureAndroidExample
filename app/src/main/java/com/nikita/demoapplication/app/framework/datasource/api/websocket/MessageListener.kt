package com.nikita.demoapplication.app.framework.datasource.api.websocket

interface MessageListener {
    fun  onConnectSuccess () // successfully connected
    fun  onConnectFailed () // connection failed
    fun  onClose () // close
    fun onMessage(text: String?)
}