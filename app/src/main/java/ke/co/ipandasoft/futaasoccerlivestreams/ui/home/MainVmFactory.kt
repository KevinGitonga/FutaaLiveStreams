package ke.co.ipandasoft.futaasoccerlivestreams.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ke.co.ipandasoft.futaasoccerlivestreams.data.repository.LocationRepository

class MainVmFactory(private val locationRepository: LocationRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(locationRepository) as T
    }

}