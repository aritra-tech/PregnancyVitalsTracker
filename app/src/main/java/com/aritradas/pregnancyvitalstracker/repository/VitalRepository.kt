package com.aritradas.pregnancyvitalstracker.repository

import androidx.lifecycle.LiveData
import com.aritradas.pregnancyvitalstracker.data.VitalEntry
import com.aritradas.pregnancyvitalstracker.data.VitalEntryDao
import kotlinx.coroutines.flow.Flow

class VitalRepository(private val vitalEntryDao: VitalEntryDao) {

    fun getAllVitalEntries(): Flow<List<VitalEntry>> = vitalEntryDao.getAllVitalEntries()

    fun getAllVitalEntriesLiveData(): LiveData<List<VitalEntry>> =
        vitalEntryDao.getAllVitalEntriesLiveData()

    suspend fun insertVitalEntry(entry: VitalEntry) {
        vitalEntryDao.insertVitalEntry(entry)
    }

    suspend fun updateVitalEntry(entry: VitalEntry) {
        vitalEntryDao.updateVitalEntry(entry)
    }

    suspend fun deleteVitalEntry(entry: VitalEntry) {
        vitalEntryDao.deleteVitalEntry(entry)
    }

    suspend fun deleteAllVitalEntries() {
        vitalEntryDao.deleteAllVitalEntries()
    }
}