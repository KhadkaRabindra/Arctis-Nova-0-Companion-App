package com.steelseries.arctisnova0companionapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steelseries.arctisnova0companionapp.devices.ArctisNova0
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel: ViewModel() {

    private val arctisNova0 = ArctisNova0()

    private val _firmwareVersion = MutableLiveData<String>()
    val firmwareVersion: LiveData<String> get() = _firmwareVersion

    private val _micVolume = MutableLiveData<Int>()
    val micVolume: LiveData<Int> get() = _micVolume

    init {
        initializeConnection()
    }

    private fun initializeConnection() {
        viewModelScope.launch {
            arctisNova0.initializeConnection()
            getFirmwareVersion()
            getMicVolume()
        }
    }

    private suspend fun getFirmwareVersion() {
        withContext(Dispatchers.IO) {
            val fwVersionCommand = byteArrayOf(ArctisNova0.GET_FW_VERSION.toByte())
            val fwVersion = arctisNova0.deviceCommunication(fwVersionCommand)
            _firmwareVersion.postValue(String(fwVersion))
        }
    }

    private suspend fun getMicVolume() {
        withContext(Dispatchers.IO) {
            val getMicVolumeCommand = byteArrayOf(ArctisNova0.GET_MIC_VOLUME.toByte())
            val getMicVolume = arctisNova0.deviceCommunication(getMicVolumeCommand)
            val micVolumeLevel = getMicVolume.firstOrNull()?.toInt() ?: 0
            _micVolume.postValue(micVolumeLevel)
        }
    }

    fun setMicVolume(volume: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val setMicVolumeCommand = byteArrayOf(ArctisNova0.SET_MIC_VOLUME.toByte(), volume.toByte())
            arctisNova0.deviceCommunication(setMicVolumeCommand)
            _micVolume.postValue(volume)
        }
    }
}