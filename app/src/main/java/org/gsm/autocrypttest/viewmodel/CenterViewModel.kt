package org.gsm.autocrypttest.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import org.gsm.autocrypttest.BuildConfig
import org.gsm.autocrypttest.data.model.Data
import org.gsm.autocrypttest.data.model.DbMarker
import org.gsm.autocrypttest.repository.CenterDatabaseRepository
import org.gsm.autocrypttest.repository.CenterRepository
import javax.inject.Inject


@HiltViewModel
class CenterViewModel @Inject constructor(
    private val repository: CenterRepository,
    private val dataRepository: CenterDatabaseRepository
) : ViewModel() {

    var job: Job? = null

    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int> get() = _progress

    private val _lineCnt = MutableLiveData<Int>()
    val lineCnt: LiveData<Int> get() = _lineCnt

    private val _data = MutableLiveData<List<Data>>()
    val data: LiveData<List<Data>> get() = _data

    private val _lat = MutableLiveData<String>()
    val lat: LiveData<String> get() = _lat

    private val _lng = MutableLiveData<String>()
    val lng: LiveData<String> get() = _lng

    private val _addr = MutableLiveData<String>()
    val addr: LiveData<String> get() = _addr

    private val _phoneNum = MutableLiveData<String>()
    val phoneNum: LiveData<String> get() = _phoneNum

    private val _updateAt = MutableLiveData<String>()
    val updateAt: LiveData<String> get() = _updateAt

    private val _centerName = MutableLiveData<String>()
    val centerName: LiveData<String> get() = _centerName

    private val _facilityName = MutableLiveData<String>()
    val facilityName: LiveData<String> get() = _facilityName

    private val _centerType = MutableLiveData<String>()
    val centerType: LiveData<String> get() = _centerType

    private val list = mutableListOf<DbMarker>()

    private val _latLng = MutableLiveData<List<DbMarker>>()
    val latLng: LiveData<List<DbMarker>> get() = _latLng


    init {
        _latLng.value = list
    }

    suspend fun getCenter() {
        job = CoroutineScope(Dispatchers.IO).launch {
            for (page in 1..10) {
                delay(100L)
                val response = try {
                    repository.getCenter(page, BuildConfig.API_KEY)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
                val result = response?.body()!!

                result.apply {
                    if (this != null) {
                        _data.postValue(this.data)
                        data.forEach {
                            dataRepository.insertCenter(it)
                        }
                    }
                }
            }

        }

    }

    suspend fun getLineCnt() {
        _lineCnt.value = dataRepository.selectLineCnt()
        Log.e(TAG, "getLineCnt: ${lineCnt.value}")
    }

    suspend fun setProcess() = CoroutineScope(Dispatchers.Main).launch {
        delay(1000L)
        _progress.value = 50
        if (lineCnt.value != 100) {
            _progress.value = 80
            launch {
                getCenter()
            }.join()
            delay(2000L)
            for (i in 0..3) {
                delay(100L)
                _progress.value = _progress.value!! + 5
            }

        } else {
            delay(1000)
            _progress.value = 100
        }

    }

    fun getCenterInfo(id: Int) = CoroutineScope(Dispatchers.Main).launch {
        val dbResult = dataRepository.selectCenter(id)
        withContext(Dispatchers.IO) {
            _addr.postValue(dbResult.address)
            _centerName.postValue(dbResult.centerName)
            _facilityName.postValue(dbResult.facilityName)
            _phoneNum.postValue(dbResult.phoneNumber)
            _updateAt.postValue(dbResult.updatedAt)
        }
        Log.d(TAG, "getCenterInfo: ${addr.value}")
    }


    fun getLatLng() {
        job = CoroutineScope(Dispatchers.Main).launch {
            getLineCnt()
            for (i in 1..lineCnt.value!!) {
                val dbResult = dataRepository.selectCenter(i)
                _lat.value = (dbResult.lat)
                _lng.value = (dbResult.lng)
                _centerType.value = (dbResult.centerType)
                list.add(
                    DbMarker(
                        LatLng(lat.value!!.toDouble(), lng.value!!.toDouble()),
                        i,
                        centerType.value!!
                    )
                )
            }
            _latLng.value = list
        }

    }

}