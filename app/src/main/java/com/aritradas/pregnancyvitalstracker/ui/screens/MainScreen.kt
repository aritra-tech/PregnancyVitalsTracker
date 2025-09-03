package com.aritradas.pregnancyvitalstracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aritradas.pregnancyvitalstracker.data.VitalEntry
import com.aritradas.pregnancyvitalstracker.ui.components.AddVitalDialog
import com.aritradas.pregnancyvitalstracker.ui.components.VitalEntryCard
import com.aritradas.pregnancyvitalstracker.viewmodel.VitalViewModel
import kotlinx.coroutines.delay
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    vitalViewModel: VitalViewModel = viewModel()
) {
    val vitalEntries by vitalViewModel.allVitalEntriesFlow.collectAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }
    var timerSeconds by remember { mutableStateOf(0) }
    var isTimerRunning by remember { mutableStateOf(false) }

    // Timer effect
    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning) {
            delay(1000)
            timerSeconds++
        }
    }

    Scaffold(
        topBar = {
            // Timer Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFE1BEE7), // Light purple
                                Color(0xFFBA68C8)  // Medium purple
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Track My Pregnancy",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A148C)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Timmer - ${formatTime(timerSeconds)}",
                            fontSize = 16.sp,
                            color = Color(0xFF4A148C)
                        )

                        Button(
                            onClick = {
                                if (isTimerRunning) {
                                    isTimerRunning = false
                                } else {
                                    isTimerRunning = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isTimerRunning) Color(0xFFD32F2F) else Color(
                                    0xFF4CAF50
                                )
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(
                                text = if (isTimerRunning) "Stop" else "Start",
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF9C27B0),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Vitals")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (vitalEntries.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No vital entries yet.\nTap + to add your first entry!",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        lineHeight = 24.sp
                    )
                }
            } else {
                // Vitals list
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(vitalEntries) { vitalEntry ->
                        VitalEntryCard(vitalEntry = vitalEntry)
                    }
                }
            }
        }
    }

    // Show Add Dialog
    if (showAddDialog) {
        AddVitalDialog(
            onDismiss = { showAddDialog = false },
            onSubmit = { systolic, diastolic, heartRate, weight, babyKicks ->
                val newEntry = VitalEntry(
                    systolicBP = systolic,
                    diastolicBP = diastolic,
                    heartRate = heartRate,
                    weight = weight,
                    babyKicks = babyKicks,
                    timestamp = Date()
                )
                vitalViewModel.insertVitalEntry(newEntry)
            }
        )
    }
}

private fun formatTime(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", minutes, secs)
}