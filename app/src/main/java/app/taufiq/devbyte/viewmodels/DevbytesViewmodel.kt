package app.taufiq.devbyte.viewmodels

import android.app.Application
import androidx.lifecycle.*
import app.taufiq.devbyte.database.getDatabase
import app.taufiq.devbyte.network.DevByteNetwork
import app.taufiq.devbyte.network.asDomainModel
import app.taufiq.devbyte.repository.VideoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

/**
 * Created By Taufiq on 8/18/2020.
 *
 */


/**
 * DevByteViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 *
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during actiivty
 * or fragment lifecycle events.
 */
class DevbytesViewmodel(application: Application) : AndroidViewModel(application) {

    /**
     * The data source this ViewModel will fetch results from.
     */
    private val videoRepository = VideoRepository(getDatabase(application))

    /**
     * A playlist of videos displayed on the screen.
     */
    val playlist = videoRepository.videos

    /**
     * This is the Job for all coroutines started by this viewmodel
     *
     * canceling this job will cancel all coroutines started by this.
     * */
    private val viewModelJob = SupervisorJob()


    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     *
     * Since we pass viewModelJob, you can cancel all coroutines launched by uiScope by calling
     * viewModelJob.cancel()
     */
    private val viewmodelScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    /**
     * Event triggered for network error. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    /**
     * Event triggered for network error. Views should use this to get access
     * to the data.
     */
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError


    /**
     * Flag to display the error message. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    /**
     * Flag to display the error message. Views should use this to get access
     * to the data.
     */
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown


    /**
     * init{} is called immediately when this viewmodel is created
     * */

    init {

        refreshDataFromRepository()
    }


    private fun refreshDataFromRepository() = viewmodelScope.launch {
        try {
            videoRepository.refreshVideo()
            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false
        } catch (networkError: IOException) {
            if (playlist.value.isNullOrEmpty()) {
                Timber.d("Network error")
                _eventNetworkError.value = true
            }

        }
    }

    /**
     * Resets the network error log
     * */
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }


    /**
     * Cancel all coroutines when the ViewModel is cleared
     */

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DevbytesViewmodel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DevbytesViewmodel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }


}