/*
 * *
 *  * Created by Kevin Gitonga on 5/7/20 9:01 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 5/7/20 9:01 AM
 *
 */

package ke.co.ipandasoft.futaasoccerlivestreams.data

import ke.co.ipandasoft.futaasoccerlivestreams.data.requestpayload.CountryDataPayload
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.TodayGamesResponse
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.UpcomingGamesResponse
import retrofit2.Response
import retrofit2.http.*

interface GamesApiService {


    @POST("/public/api/v1/games")
    suspend fun getTodayGames(@Body countryDataPayload: CountryDataPayload):Response<TodayGamesResponse>

    @POST("/public/api/v1/highlights")
    suspend fun getUpcomingGames(@Body countryDataPayload: CountryDataPayload):Response<UpcomingGamesResponse>
}