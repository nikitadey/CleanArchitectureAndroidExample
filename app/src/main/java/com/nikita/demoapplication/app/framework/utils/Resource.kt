package com.nikita.demoapplication.app.framework.utils

import com.nikita.demoapplication.utils.Status


data class Resource<T> (val status: Status, val data:T?)