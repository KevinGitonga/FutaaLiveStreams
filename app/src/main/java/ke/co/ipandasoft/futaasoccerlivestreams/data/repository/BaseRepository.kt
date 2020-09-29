package ke.co.ipandasoft.futaasoccerlivestreams.data.repository

import ke.co.ipandasoft.futaasoccerlivestreams.data.response.LocationInfo
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.TodayGamesResponse
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.UpcomingGamesResponse
import ke.co.ipandasoft.futaasoccerlivestreams.utils.State
import retrofit2.Response

interface BaseRepository {

    suspend fun getTodayGames(): State<TodayGamesResponse>

    suspend fun getUpcomingGames():State<UpcomingGamesResponse>

}