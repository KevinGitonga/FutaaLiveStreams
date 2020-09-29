package ke.co.ipandasoft.futaasoccerlivestreams.data.response


import com.google.gson.annotations.SerializedName

data class LocationInfo(
    @SerializedName("as")
    var asX: String?,
    @SerializedName("city")
    var city: String?,
    @SerializedName("country")
    var country: String?,
    @SerializedName("countryCode")
    var countryCode: String?,
    @SerializedName("isp")
    var isp: String?,
    @SerializedName("lat")
    var lat: Double?,
    @SerializedName("lon")
    var lon: Double?,
    @SerializedName("org")
    var org: String?,
    @SerializedName("query")
    var query: String?,
    @SerializedName("region")
    var region: String?,
    @SerializedName("regionName")
    var regionName: String?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("timezone")
    var timezone: String?,
    @SerializedName("zip")
    var zip: String?
)