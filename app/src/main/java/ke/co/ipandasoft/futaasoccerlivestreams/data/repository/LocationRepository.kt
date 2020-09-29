package ke.co.ipandasoft.futaasoccerlivestreams.data.repository

import com.google.gson.Gson
import ke.co.ipandasoft.futaasoccerlivestreams.data.LocationApiService
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.LocationInfo
import ke.co.ipandasoft.futaasoccerlivestreams.utils.State
import retrofit2.Response
import timber.log.Timber
import java.lang.Exception

class LocationRepository(private val locationApiService: LocationApiService){

    suspend fun checkLocalLocation():State<LocationInfo>{
        try {
            val locationResp=locationApiService.getLocationInfo()
            if(locationResp.isSuccessful) {
                Timber.e("DATA RESP"+Gson().toJson(locationResp.body()))
            }
            return State.success(locationResp.body()!!)

        }catch (ex:Exception){
            Timber.e("EXCEPTION"+ex.localizedMessage)
            return State.error(ex.localizedMessage)
        }

    }

}