package com.aritradas.pregnancyvitalstracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.aritradas.pregnancyvitalstracker.data.VitalDatabase
import com.aritradas.pregnancyvitalstracker.data.VitalEntry
import com.aritradas.pregnancyvitalstracker.repository.VitalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class VitalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VitalRepository
    val allVitalEntries: LiveData<List<VitalEntry>>
    val allVitalEntriesFlow: Flow<List<VitalEntry>>

    init {
        val vitalEntryDao = VitalDatabase.getDatabase(application).vitalEntryDao()
        repository = VitalRepository(vitalEntryDao)
        allVitalEntries = repository.getAllVitalEntriesLiveData()
        allVitalEntriesFlow = repository.getAllVitalEntries()
    }

    fun insertVitalEntry(entry: VitalEntry) = viewModelScope.launch {
        repository.insertVitalEntry(entry)
    }

    fun updateVitalEntry(entry: VitalEntry) = viewModelScope.launch {
        repository.updateVitalEntry(entry)
    }

    fun deleteVitalEntry(entry: VitalEntry) = viewModelScope.launch {
        repository.deleteVitalEntry(entry)
    }

    fun deleteAllVitalEntries() = viewModelScope.launch {
        repository.deleteAllVitalEntries()
    }
}