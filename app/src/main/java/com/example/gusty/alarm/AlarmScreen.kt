package com.example.gusty.alarm

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.gusty.ui.theme.gray
import java.util.Calendar


@Composable
fun AlarmScreen() {
    val isTimePickerDialog = remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isTimePickerDialog.value = true
                }, containerColor = gray
            ) {
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = null
                )
            }
        }
    ){ padding ->
        Column(
            modifier = Modifier.padding(padding)
                .fillMaxSize()
        ) {
            if(isTimePickerDialog.value) TimePickerDialog(
                onDismiss = {
                    isTimePickerDialog.value = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
) {

    val currentTime = Calendar.getInstance()
    val currentTimeInSeconds = (currentTime.get(Calendar.HOUR_OF_DAY) * 3600) +
            (currentTime.get(Calendar.MINUTE) * 60) +
            currentTime.get(Calendar.SECOND)

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    val selectedSeconds = (timePickerState.hour.toLong() * 3600) + (timePickerState.minute * 60)

    val timeDifference = if (selectedSeconds >= currentTimeInSeconds) {
        selectedSeconds - currentTimeInSeconds
    } else {
        (24 * 3600) - (currentTimeInSeconds - selectedSeconds) // Time for the next day
    }
    val triggerTimeMillis = System.currentTimeMillis() + (timeDifference * 1000)
    Log.i("TAG", "time = $triggerTimeMillis ")
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDismiss.invoke()
            }) {
                Text("Confirm", color = Color.Black)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel" , color = Color.Black)
            }
        },
        title = { Text(text = "Select Time") },
        text = {
            DialExample(timePickerState)
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialExample(timePickerState: TimePickerState) {

    Column {
        TimePicker(
            state = timePickerState,
        )
    }
}

