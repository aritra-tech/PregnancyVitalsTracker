package com.aritradas.pregnancyvitalstracker.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aritradas.pregnancyvitalstracker.R
import com.aritradas.pregnancyvitalstracker.data.VitalEntry
import java.text.SimpleDateFormat
import java.util.Locale

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
                            Color(0xFFE1BEE7),
                            Color(0xFFBA68C8)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    VitalItem(
                        imageRes = R.drawable.heart_rate_1,
                        value = "${vitalEntry.heartRate} bpm",
                        modifier = Modifier.weight(1f)
                    )

                    VitalItem(
                        imageRes = R.drawable.blood_pressure_1,
                        value = "${vitalEntry.systolicBP}/${vitalEntry.diastolicBP} mmHg",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    VitalItem(
                        imageRes = R.drawable.scale_1,
                        value = "${vitalEntry.weight} kg",
                        modifier = Modifier.weight(1f)
                    )

                    VitalItem(
                        imageRes = R.drawable.newborn_2,
                        value = "${vitalEntry.babyKicks} kicks",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFF9C27B0),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(all = 8.dp)
                        .align(Alignment.End)
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
    @DrawableRes imageRes: Int,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            color = Color(0xFF4A148C),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}