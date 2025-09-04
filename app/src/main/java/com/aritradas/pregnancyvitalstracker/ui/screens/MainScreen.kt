package com.aritradas.pregnancyvitalstracker.ui.screens

import android.content.*
import android.os.IBinder
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aritradas.pregnancyvitalstracker.data.VitalEntry
import com.aritradas.pregnancyvitalstracker.timer.TimerService
import com.aritradas.pregnancyvitalstracker.ui.components.AddVitalDialog
import com.aritradas.pregnancyvitalstracker.ui.components.VitalEntryCard
import com.aritradas.pregnancyvitalstracker.viewmodel.VitalViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    vitalViewModel: VitalViewModel = viewModel()
) {
    val context = LocalContext.current
    val vitalEntries by vitalViewModel.allVitalEntriesFlow.collectAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }
    var currentTime by remember { mutableStateOf("00:00:00") }
    var isTimerServiceRunning by remember { mutableStateOf(false) }
    var timerService: TimerService? by remember { mutableStateOf(null) }
    var isBound by remember { mutableStateOf(false) }
    val serviceConnection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as TimerService.TimerBinder
                timerService = binder.getService()
                isBound = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                timerService = null
                isBound = false
            }
        }
    }
    val timeReceiver = remember {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == TimerService.ACTION_TIME_UPDATE) {
                    val time = intent.getStringExtra(TimerService.EXTRA_TIME) ?: "00:00:00"
                    currentTime = time
                }
            }
        }
    }

    LaunchedEffect(timerService) {
        timerService?.currentTime?.collect { time ->
            currentTime = time
        }
    }

    LaunchedEffect(timerService) {
        timerService?.isRunning?.collect { running ->
            isTimerServiceRunning = running
        }
    }

    DisposableEffect(context) {
        val intent = Intent(context, TimerService::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        val filter = IntentFilter(TimerService.ACTION_TIME_UPDATE)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(timeReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            ContextCompat.registerReceiver(
                context,
                timeReceiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }

        onDispose {
            if (isBound) {
                context.unbindService(serviceConnection)
            }
            try {
                context.unregisterReceiver(timeReceiver)
            } catch (e: IllegalArgumentException) {
                // Receiver was not registered
            }
        }
    }

    fun startTimerService() {
        val intent = Intent(context, TimerService::class.java).apply {
            action = TimerService.ACTION_START_TIMER
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    fun stopTimerService() {
        val intent = Intent(context, TimerService::class.java).apply {
            action = TimerService.ACTION_STOP_TIMER
        }
        context.startService(intent)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE1BEE7),
                            Color(0xFFBA68C8)
                        )
                    )
                )
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Track My Pregnancy",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A148C),
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Timer - $currentTime",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )

                    Button(
                        onClick = {
                            if (isTimerServiceRunning) {
                                stopTimerService()
                            } else {
                                startTimerService()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isTimerServiceRunning) Color(0xFFD32F2F) else Color(
                                0xFF4CAF50
                            )
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .widthIn(min = 80.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (isTimerServiceRunning) "Stop" else "Start",
                            fontSize = 14.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            if (vitalEntries.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No vital entries yet.",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "Tap + to add your first entry!",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
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

            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF9C27B0),
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .navigationBarsPadding()
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Vitals")
            }
        }
    }

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