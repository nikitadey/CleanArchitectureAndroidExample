package com.nikita.demoapplication.app.framework.datasource.api.model

import com.google.gson.annotations.SerializedName

data class CryptoCandleRaw(@SerializedName ("c")val closing:ArrayList<Float>,
                           @SerializedName ("h")val high:ArrayList<Float>,
                           @SerializedName ("l")val low:ArrayList<Float>,
                           @SerializedName ("o")val open:ArrayList<Float>,
                           @SerializedName ("t")val time:ArrayList<Long>?,
                           @SerializedName ("v")val volume:ArrayList<Float>,
                           @SerializedName ("s")val status:String,
                            )
