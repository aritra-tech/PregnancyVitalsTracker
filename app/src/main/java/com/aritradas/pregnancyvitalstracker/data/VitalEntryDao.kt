package com.aritradas.pregnancyvitalstracker.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VitalEntryDao {
    @Query("SELECT * FROM vital_entries ORDER BY timestamp DESC")
    fun getAllVitalEntries(): Flow<List<VitalEntry>>

    @Query("SELECT * FROM vital_entries ORDER BY timestamp DESC")
    fun getAllVitalEntriesLiveData(): LiveData<List<VitalEntry>>

    @Insert
    suspend fun insertVitalEntry(entry: VitalEntry)

    @Update
    suspend fun updateVitalEntry(entry: VitalEntry)

    @Delete
    suspend fun deleteVitalEntry(entry: VitalEntry)

    @Query("DELETE FROM vital_entries")
    suspend fun deleteAllVitalEntries()
}