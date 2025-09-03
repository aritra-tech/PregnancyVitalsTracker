package com.aritradas.pregnancyvitalstracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "vital_entries")
data class VitalEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val systolicBP: Int,
    val diastolicBP: Int,
    val heartRate: Int,
    val weight: Double,
    val babyKicks: Int,
    val timestamp: Date = Date()
)