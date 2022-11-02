package com.nikita.demoapplication.utils

import com.google.gson.*
import java.lang.Exception
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


object GsonUtils {

    val gson by lazy {
        GsonBuilder().create()
    }
    fun <T> fromJson(json: String, cls: Class<T>): T? {
        try {
            return gson.fromJson(json, cls)
        }
        catch (ex:JsonParseException){
            return null
        }
    }

    fun toJson(cas: Any?): String {
        return gson.toJson(cas)
    }

    fun <T> getListFromJsonArray(jsonArray: JsonArray, tClass: Class<T>): List<T> {
        val list: MutableList<T> = ArrayList()
        for (i in 0 until jsonArray.size()) {
            try {
                val obj = fromJson(jsonArray.get(i).toString(), tClass)
                if (obj != null) {
                    list.add(obj)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return list
    }

    fun getJsonFromString(data: String): JsonObject {
        return JsonParser.parseString(data) as JsonObject
    }

    fun filterNullFromString(jsonElement: JsonElement): String {
        return if (jsonElement.isJsonNull()) {
            ""
        } else {
            jsonElement.getAsString()
        }
    }
}