package com.pankti.webservicewithretrofit.domain.entities

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pankti.webservicewithretrofit.domain.usecase.NetworkUseCase
import com.pankti.webservicewithretrofit.utils.NetworkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class NetworkDataViewModel @Inject constructor(val useCase: NetworkUseCase) : ViewModel(){

    private var _postList = MutableLiveData<List<GetNum>>()
    private var _GetOneNumber = MutableLiveData<GetNum>()

//    for audio
private val _soundData = MutableLiveData<AudioFile>()
    val soundData: LiveData<AudioFile> = _soundData
    private var refreshJob: Job? = null
    var previousPartOfInterest: String? = null


    //    to take a number witOut list
    private val _numberLiveData = MutableLiveData<Result<GetNum>>()
    val numberLiveData: LiveData<Result<GetNum>> = _numberLiveData

    var postList: LiveData<List<GetNum>> = _postList
    var getOneNum: LiveData<GetNum> = _GetOneNumber

    var errorMessage = MutableLiveData("")
    var isLoading = MutableLiveData(false)


//    region get Turn
    fun getPosts() {
        viewModelScope.launch {

//            isLoading.value = false
            isLoading.value = true

            useCase.getNumber().catch { error ->
                errorMessage.value = error.message?:""
//                isLoading.value = true
                isLoading.value = false
            }.collect {
                _postList.postValue((it))
//                isLoading.value = true
                isLoading.value = false
            }
        }
    }
//    endregion

//    region Refresh voice after call new voice
    @RequiresApi(Build.VERSION_CODES.M)
    fun isNetworkConnected(context: Context): Boolean {
        return NetworkManager.isConnected(context)
    }

   fun startRefreshing() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            while (true) {
                fetchSound()
                delay(5000) // Adjust the refresh interval as needed (5000 milliseconds = 5 seconds)
            }
        }
    }

    fun stopRefreshing() {
        refreshJob?.cancel()
    }

    private suspend fun fetchSound() {
        try {
            val sound = useCase.downloadAudio()
            _soundData.postValue(sound)
        } catch (e: Exception) {
            // Handle errors (e.g., network errors)
            e.printStackTrace()
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopRefreshing()
    }
// endregion


//    region commented & not used

/*    fun fetchSound() {
        viewModelScope.launch {
            val response = useCase.downloadAudio()
            if (response.isSuccessful) {
                _soundData.value = response.body()
            } else {
                // Handle error
            }
        }
    }*/

/*
    fun fetchSound() {
        viewModelScope.launch {
            val sound = useCase.downloadAudio()
            _soundData.postValue(sound)
        }
    }
*/

/*    fun getOneNumber() {
        viewModelScope.launch {
            isLoading.value = true
            useCase.getOneNumber().catch { error ->
                errorMessage.value = error.message?:""
                isLoading.value = false
            }.collect {
                _GetOneNumber.postValue((it))
                isLoading.value = false
            }
        }
    }*/


    //    to take a number witOut list
    fun getOneNumber() {
        viewModelScope.launch {
            val result = useCase.getOneNumber()
            _numberLiveData.postValue(result)
        }
    }

//endregion
}