package ke.co.ipandasoft.futaasoccerlivestreams.data.repository

import com.google.gson.Gson
import ke.co.ipandasoft.futaasoccerlivestreams.data.GamesApiService
import ke.co.ipandasoft.futaasoccerlivestreams.data.LocationApiService
import ke.co.ipandasoft.futaasoccerlivestreams.data.requestpayload.CountryDataPayload
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.LocationInfo
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.TodayGamesResponse
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.UpcomingGamesResponse
import ke.co.ipandasoft.futaasoccerlivestreams.utils.State
import retrofit2.Response
import timber.log.Timber

class LiveStreamsRepository(private val gamesApiService: GamesApiService) :BaseRepository{
    override suspend fun getTodayGames(): State<TodayGamesResponse> {

        return try {
            val dataResponse=gamesApiService.getTodayGames(CountryDataPayload("null"))
            var gamesResponse:TodayGamesResponse?=null
            when {
                dataResponse.isSuccessful -> {
                    Timber.e("SUCCESS RESP ${Gson().toJson(dataResponse.body())}")
                    gamesResponse= dataResponse.body()!!
                }
                else -> {
                    Timber.e("ERROR RESP ${Gson().toJson(dataResponse.errorBody())}")
                }
            }
            return State.Success(gamesResponse!!)

        }catch (exception:Exception){

            Timber.e("ERROR RESP ${Gson().toJson(exception.localizedMessage)}")
            return State.Error(exception.message!!)
        }
    }

    override suspend fun getUpcomingGames():State<UpcomingGamesResponse>{
        return try {
            val dataResponse=gamesApiService.getUpcomingGames(CountryDataPayload("null"))
            var gamesResponse:UpcomingGamesResponse?=null
            when {
                dataResponse.isSuccessful -> {
                    Timber.e("SUCCESS RESP ${Gson().toJson(dataResponse.body())}")
                    gamesResponse= dataResponse.body()!!
                }
                else -> {
                    Timber.e("ERROR RESP ${Gson().toJson(dataResponse.errorBody())}")
                }
            }
            return State.Success(gamesResponse!!)

        }catch (exception:Exception){

            Timber.e("ERROR RESP ${Gson().toJson(exception.localizedMessage)}")
            return State.Error(exception.message!!)
        }
    }

}