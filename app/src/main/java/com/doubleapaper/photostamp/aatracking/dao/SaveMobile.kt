package com.doubleapaper.photostamp.aatracking.dao


import com.google.gson.annotations.SerializedName


class SaveMobile {
    constructor(
        actionDateTime: String?,
        androidVersion: Int?,
        appVersion: String?,
        imei: String?,
        isAnyProviderAvailable: Boolean?,
        isNetworkAvailable: Boolean?,
        isPassiveAvailable: Boolean?,
        isisGpsAvailable: Boolean?,
        locationServicesEnabled: Boolean?,
        model: String?,
        token: String?,
        userAction: String?,
        userID: String?
    ) {
        this.actionDateTime = actionDateTime
        this.androidVersion = androidVersion
        this.appVersion = appVersion
        this.imei = imei
        this.isAnyProviderAvailable = isAnyProviderAvailable
        this.isNetworkAvailable = isNetworkAvailable
        this.isPassiveAvailable = isPassiveAvailable
        this.isisGpsAvailable = isisGpsAvailable
        this.locationServicesEnabled = locationServicesEnabled
        this.model = model
        this.token = token
        this.userAction = userAction
        this.userID = userID
    }

    @SerializedName("ActionDateTime")
    var actionDateTime: String? = null
    @SerializedName("AndroidVersion")
    var androidVersion: Int? = null
    @SerializedName("AppVersion")
    var appVersion: String? = null
    @SerializedName("IMEI")
    var imei: String? = null
    @SerializedName("IsAnyProviderAvailable")
    var isAnyProviderAvailable: Boolean? = null
    @SerializedName("IsNetworkAvailable")
    var isNetworkAvailable: Boolean? = null
    @SerializedName("IsPassiveAvailable")
    var isPassiveAvailable: Boolean? = null
    @SerializedName("IsisGpsAvailable")
    var isisGpsAvailable: Boolean? = null
    @SerializedName("LocationServicesEnabled")
    var locationServicesEnabled: Boolean? = null
    @SerializedName("Model")
    var model: String? = null
    @SerializedName("Token")
    var token: String? = null
    @SerializedName("UserAction")
    var userAction: String? = null
    @SerializedName("UserID")
    var userID: String? = null

}
