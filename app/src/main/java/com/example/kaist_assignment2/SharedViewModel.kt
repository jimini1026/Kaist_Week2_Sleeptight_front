package com.example.kaist_assignment2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _updateSongsEvent = MutableLiveData<Boolean>()
    val updateSongsEvent: LiveData<Boolean> get() = _updateSongsEvent

    fun notifySongsUpdated() {
        _updateSongsEvent.value = true
    }
}