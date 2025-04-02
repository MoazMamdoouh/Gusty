package com.example.gusty.alarm

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gusty.ui.theme.blue
import com.example.gusty.ui.theme.gray
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun AlarmScreen() {
    val isBottomSheetOpened = remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isBottomSheetOpened.value = true
                }, containerColor = gray
            ) {
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = null
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (isBottomSheetOpened.value) OpenButtonSheet(
                onDismiss = {
                    isBottomSheetOpened.value = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenButtonSheet(
    onDismiss: () -> Unit,
) {
    val buttonSheetState = androidx.compose.material3.rememberModalBottomSheetState()
    val openCalenderState = remember { mutableStateOf(false) }
    val openTimePickerState = remember { mutableStateOf(false) }
    val timeInTimeFormat = remember { mutableLongStateOf(0) }
    val datePickerState = rememberDatePickerState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = buttonSheetState,
        modifier = Modifier
            .fillMaxSize(), containerColor = blue
    ) {
        Column {
            Row {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .width(150.dp)
                        .height(200.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Select Date",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(5.dp)
                    )
                    Spacer(Modifier.height(5.dp))
                    Button(
                        onClick = {
                            openCalenderState.value = true
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = "Open calender", color = Color.Black)
                    }
                    TextField(
                        value = if (datePickerState.selectedDateMillis != 0L) {
                            val formattedDate =
                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                    Date(datePickerState.selectedDateMillis ?: 0)
                                )
                            formattedDate
                        } else {
                            "Select a date"
                        },
                        onValueChange = { newValue ->
                        },
                        label = { Text("Selected Date") },
                        enabled = false
                    )
                }
                Spacer(Modifier.width(10.dp))
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .width(150.dp)
                        .height(200.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Select Time",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(5.dp)
                    )
                    Spacer(Modifier.height(5.dp))
                    Button(
                        onClick = {
                            openTimePickerState.value = true
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = "Open Timer", color = Color.Black)
                    }
                    TextField(
                        value = if (timeInTimeFormat.value != 0L) {
                            val hours = (timeInTimeFormat.value / 3600).toInt()
                            val minutes = ((timeInTimeFormat.value % 3600) / 60).toInt()
                            String.format("%02d:%02d", hours, minutes)
                        } else {
                            "Select Time"
                        },
                        onValueChange = { newValue ->
                        },
                        label = { Text("Selected Time") },
                        enabled = false
                    )
                }
            }
            Spacer(Modifier.height(15.dp))
            Button(
                onClick = {

                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
                , modifier = Modifier.fillMaxWidth()
                    .padding(start = 15.dp , end = 15.dp)
            ) {
                Text(text = " Save Alarm ", color = Color.Black)
            }

        }
        if (openCalenderState.value) DatePickerModal(
            onDateSelected = { datePickerState.selectedDateMillis },
            onDismiss = { openCalenderState.value = false }, datePickerState
        )
        if (openTimePickerState.value) TimePickerDialog(
            onDismiss = {
                openTimePickerState.value = false
            }, timeInTimeFormat
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    value: DatePickerState
) {

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(value.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = value)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    timeInTimeFormat: MutableLongState,
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
        (24 * 3600) - (currentTimeInSeconds - selectedSeconds)
    }
    // i want to pass this to the alarm manager
    val triggerTimeMillis = System.currentTimeMillis() + (timeDifference * 1000)
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val selectedHour = timePickerState.hour
                val selectedMinute = timePickerState.minute
                timeInTimeFormat.value = (selectedHour * 3600 + selectedMinute * 60).toLong()
                onDismiss.invoke()
            }) {
                Text("Confirm", color = Color.Black)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Black)
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

