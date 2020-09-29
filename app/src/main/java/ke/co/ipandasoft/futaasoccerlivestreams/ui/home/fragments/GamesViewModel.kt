package ke.co.ipandasoft.futaasoccerlivestreams.ui.home.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import ke.co.ipandasoft.futaasoccerlivestreams.data.repository.LiveStreamsRepository
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.LocationInfo
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.TodayGamesResponse
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.UpcomingGamesResponse
import ke.co.ipandasoft.futaasoccerlivestreams.utils.State
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber

class GamesViewModel(private val repository: LiveStreamsRepository) :ViewModel(){

    private val _todayGamesData = MutableLiveData<TodayGamesResponse>()
    val todayGamesData: LiveData<TodayGamesResponse> =  _todayGamesData

    private val _upcomingGamesData = MutableLiveData<UpcomingGamesResponse>()
    val upcomingGamesData: LiveData<UpcomingGamesResponse> =  _upcomingGamesData


    fun loadTodayGames(){
     viewModelScope.launch {
         when(val dataResponse=repository.getTodayGames()) {
             is State.Success
             ->{
                 Timber.e("DATA FROM REPO"+Gson().toJson(dataResponse.data))
                 _todayGamesData.postValue(dataResponse.data)
             }
             is State.Error->{
                 Timber.e("Error in response ${dataResponse.message}")
             }
             is State.Loading ->{
                 Timber.e("IS LOADING DATA LOADING DATA")
             }
         }
     }
    }

    fun loadUpcomingGames(){
        viewModelScope.launch {
            when(val dataResponse=repository.getUpcomingGames()) {
                is State.Success
                ->{
                    Timber.e("DATA FROM REPO"+Gson().toJson(dataResponse.data))
                    _upcomingGamesData.postValue(dataResponse.data)
                }
                is State.Error->{
                    Timber.e("Error in response ${dataResponse.message}")
                }
                is State.Loading ->{
                    Timber.e("IS LOADING DATA LOADING DATA")
                }
            }
    } }

}