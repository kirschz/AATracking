package com.doubleapaper.photostamp.aatracking.dao


import com.google.gson.annotations.SerializedName


class SaveTracking {
    constructor(
        addressName: String?,
        appVersion: String?,
        dateTimeStamp: String?,
        fromService: String?,
        imei: String?,
        latitude: Double?,
        longitude: Double?,
        ticket: String?,
        unixTimeStamp: Long?,
        userID: String?
    ) {
        this.addressName = addressName
        this.appVersion = appVersion
        this.dateTimeStamp = dateTimeStamp
        this.fromService = fromService
        this.imei = imei
        this.latitude = latitude
        this.longitude = longitude
        this.ticket = ticket
        this.unixTimeStamp = unixTimeStamp
        this.userID = userID
    }

    @SerializedName("AddressName")
    var addressName: String? = null
    @SerializedName("AppVersion")
    var appVersion: String? = null
    @SerializedName("DateTimeStamp")
    var dateTimeStamp: String? = null
    @SerializedName("FromService")
    var fromService: String? = null
    @SerializedName("IMEI")
    var imei: String? = null
    @SerializedName("Latitude")
    var latitude: Double? = null
    @SerializedName("Longitude")
    var longitude: Double? = null
    @SerializedName("Ticket")
    var ticket: String? = null
    @SerializedName("UnixTimeStamp")
    var unixTimeStamp: Long? = null
    @SerializedName("UserID")
    var userID: String? = null

    override fun toString(): String {
        return "SaveTracking(addressName=$addressName, appVersion=$appVersion, dateTimeStamp=$dateTimeStamp, fromService=$fromService, imei=$imei, latitude=$latitude, longitude=$longitude, ticket=$ticket, unixTimeStamp=$unixTimeStamp, userID=$userID)"
    }

}
