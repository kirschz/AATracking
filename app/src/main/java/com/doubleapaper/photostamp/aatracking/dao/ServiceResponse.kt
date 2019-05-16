package com.doubleapaper.photostamp.aatracking.dao

import com.google.gson.annotations.SerializedName


class ServiceResponse {

    @SerializedName("Message")
    var message: String? = null
    @SerializedName("Status")
    var status: String? = null

}
