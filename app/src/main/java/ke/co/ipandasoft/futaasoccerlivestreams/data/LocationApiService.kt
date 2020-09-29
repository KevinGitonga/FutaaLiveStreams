package ke.co.ipandasoft.futaasoccerlivestreams.data

import ke.co.ipandasoft.futaasoccerlivestreams.data.response.LocationInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface LocationApiService {

    @GET("/json")
    @Headers("Cache-Control: max-age=0", "Data-Agent: The Stream")
    suspend fun getLocationInfo():Response<LocationInfo>
}