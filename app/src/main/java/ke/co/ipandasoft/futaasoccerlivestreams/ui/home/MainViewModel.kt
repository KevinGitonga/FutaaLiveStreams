package ke.co.ipandasoft.futaasoccerlivestreams.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import ke.co.ipandasoft.futaasoccerlivestreams.data.repository.LocationRepository
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.LocationInfo
import ke.co.ipandasoft.futaasoccerlivestreams.utils.State
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(private val locationRepository: LocationRepository):ViewModel() {

    private val _locationLiveData= MutableLiveData<LocationInfo>()
    val locationLiveData:LiveData<LocationInfo> = _locationLiveData

    fun getLocationInfo(){
        viewModelScope.launch {
            when(val locationResp=locationRepository.checkLocalLocation()){
                is  State.Success->{
                   Timber.e("SUCCESS RESP"+Gson().toJson(locationResp.data))
                    _locationLiveData.postValue(locationResp.data)
                }
                is State.Error->{
                    Timber.e("ERROR RESP"+Gson().toJson(locationResp.message))
                }
            }
        }
    }

}