package com.example.gusty.alarm

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gusty.R
import com.example.gusty.data.local.alarm.AlarmEntity
import com.example.gusty.setting.UnitPreference
import com.example.gusty.ui.theme.blue
import com.example.gusty.ui.theme.gray
import com.example.gusty.utilities.LocationPermission
import com.example.gusty.utilities.UiStateResult
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun AlarmScreen(alarmViewModel: AlarmViewModel) {
    val isLoading = remember { mutableStateOf(false) }
    val isBottomSheetOpened = remember { mutableStateOf(false) }
    val alarmUiState = alarmViewModel.listOfAlarms.collectAsStateWithLifecycle().value
    alarmViewModel.getAllAlarmsFromDataBase()
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
            when (alarmUiState) {
                is UiStateResult.Failure -> {
                    Log.i("TAG", "")
                }

                is UiStateResult.Success -> {
                    isLoading.value = false
                    LazyColumn {
                        itemsIndexed(alarmUiState.response) { _, alarm ->
                            AlarmItemCard(alarm, alarmViewModel)
                        }
                    }
                }

                UiStateResult.Loading -> isLoading.value = true
            }
            if (isBottomSheetOpened.value) OpenButtonSheet(
                onDismiss = {
                    isBottomSheetOpened.value = false
                }, alarmViewModel
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenButtonSheet(
    onDismiss: () -> Unit,
    alarmViewModel: AlarmViewModel,
) {
    val context = LocalContext.current
    val buttonSheetState = androidx.compose.material3.rememberModalBottomSheetState()
    val openCalenderState = remember { mutableStateOf(false) }
    val openTimePickerState = remember { mutableStateOf(false) }
    val timeInTimeFormat = remember { mutableLongStateOf(0) }
    val currentWeather = alarmViewModel.currentWeather.collectAsStateWithLifecycle().value
    var formattedDate = " "
    val isLoading = remember { mutableStateOf(false) }
    val currentDateMillis = System.currentTimeMillis()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentDateMillis
    )
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )
    var timeStamp by remember { mutableLongStateOf(0) }

    LaunchedEffect(currentWeather) {

    }

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
                            formattedDate =
                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                    Date(datePickerState.selectedDateMillis ?: 0)
                                )
                            formattedDate
                        } else {
                            "Select a date"
                        },
                        onValueChange = {
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
                        value = if (timeInTimeFormat.longValue != 0L) {
                            val hours = (timeInTimeFormat.longValue / 3600).toInt()
                            val minutes = ((timeInTimeFormat.longValue % 3600) / 60).toInt()
                            String.format("%02d:%02d", hours, minutes)
                        } else {
                            "Select Time"
                        },
                        onValueChange = {
                        },
                        label = { Text("Selected Time") },
                        enabled = false
                    )
                }
            }
            Spacer(Modifier.height(15.dp))
            Button(
                onClick = {
                    alarmViewModel.getCurrentWeather(
                        LocationPermission.locationState.value.latitude,
                        LocationPermission.locationState.value.longitude ,
                        UnitPreference.getUnitSharedPreference(context) ?: "metric"
                    )
                    when (currentWeather) {
                        is UiStateResult.Loading -> {
                            isLoading.value = true
                        }
                        is UiStateResult.Failure -> Log.i("TAG", "Failed to insert")
                        is UiStateResult.Success -> {
                            isLoading.value = false
                            val countryName = currentWeather.response.sys.country
                            val duration = alarmViewModel.calculateAlarmDuration(
                                min = timePickerState.minute,
                                hour = timePickerState.hour,
                                stamp = timeStamp
                            )
                            alarmViewModel.insertAlarm(
                                context,
                                id = duration.toInt() / 1000,
                                day = formattedDate,
                                place = countryName,
                                duration = duration,
                                time = "3,3"
                            )
                        }
                    }
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ), modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp)
            ) {
                Text(text = if(isLoading.value)" Loading " else " Save Alarm ", color = Color.Black)
            }

        }
        if (openCalenderState.value) DatePickerModal(
            onDateSelected = {
                openCalenderState.value = false
                timeStamp = it ?: 0
                Log.i("TAG", "date time stamp $it ")
            },
            onDismiss = { openCalenderState.value = false }, datePickerState
        )
        if (openTimePickerState.value) TimePickerDialog(
            onDismiss = {
                openTimePickerState.value = false
                Log.i("TAG", "OpenButtonSheet: ${timePickerState.hour} , ${timePickerState.minute}")
            }, timePickerState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    datePickerState: DatePickerState
) {

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
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
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    timePickerState: TimePickerState,
) {

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

@Composable
fun AlarmItemCard(alarm: AlarmEntity, alarmViewModel: AlarmViewModel) {
    val openDeleteDialogState = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4E4F4F).copy(alpha = 0.2f))
    ) {
        Row {
            Text(
                text = "${alarm.datePicked} , ${alarm.timePicked}",
                Modifier
                    .padding(start = 5.dp)
                    .padding(start = 3.dp, top = 20.dp, bottom = 15.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(Modifier.width(10.dp))
            Image(
                painter = painterResource(R.drawable.arrow),
                contentDescription = null,
                modifier = Modifier.padding(top = 18.dp)
            )
            Spacer(Modifier.width(5.dp))
            Text(
                text = alarm.place,
                Modifier
                    .padding(start = 5.dp)
                    .padding(start = 3.dp, top = 20.dp, bottom = 15.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(painter = painterResource(R.drawable.delete),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 10.dp, top = 15.dp)
                    .size(24.dp)
                    .clickable {
                        openDeleteDialogState.value = true
                    }
            )
            if (openDeleteDialogState.value) {
                OpenDeleteFromAlarmDialog(onDismiss = {
                    openDeleteDialogState.value = false
                }, alarm, alarmViewModel)
            }
        }
    }
}

@Composable
fun OpenDeleteFromAlarmDialog(
    onDismiss: () -> Unit,
    alarm: AlarmEntity,
    alarmViewModel: AlarmViewModel,
) {
    androidx.compose.material.AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            androidx.compose.material.TextButton(onClick = {
                alarmViewModel.deleteAlarmFromDataBase(alarm)
                onDismiss.invoke()
            }) {
                Text("Delete ", color = Color.Red)
            }
        },
        dismissButton = {
            androidx.compose.material.TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text(
                text = "Delete Alarm",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
        text = {
            Text(
                "Are you sure u want to delete this Alarm ?",
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    )
}
