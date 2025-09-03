package com.aritradas.pregnancyvitalstracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aritradas.pregnancyvitalstracker.data.VitalEntry
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VitalEntryCard(
    vitalEntry: VitalEntry,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy hh:mm a", Locale.getDefault())

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
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
            Column {
                // First row - Heart Rate and Blood Pressure
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    VitalItem(
                        icon = Icons.Default.Favorite,
                        value = "${vitalEntry.heartRate} bpm",
                        modifier = Modifier.weight(1f)
                    )

                    VitalItem(
                        icon = Icons.Default.Settings,
                        value = "${vitalEntry.systolicBP}/${vitalEntry.diastolicBP} mmHg",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Second row - Weight and Baby Kicks
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    VitalItem(
                        icon = Icons.Default.Info,
                        value = "${vitalEntry.weight} kg",
                        modifier = Modifier.weight(1f)
                    )

                    VitalItem(
                        icon = Icons.Default.Star,
                        value = "${vitalEntry.babyKicks} kicks",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Date at the bottom with purple background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFF9C27B0), // Darker purple
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = dateFormat.format(vitalEntry.timestamp),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun VitalItem(
    icon: ImageVector,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF4A148C), // Dark purple
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            color = Color(0xFF4A148C), // Dark purple
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}