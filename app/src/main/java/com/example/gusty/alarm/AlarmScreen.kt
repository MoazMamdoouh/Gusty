package com.example.gusty.alarm

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gusty.R
import com.example.gusty.data.local.alarm.AlarmEntity
import com.example.gusty.setting.LanguagePreference
import com.example.gusty.setting.UnitPreference
import com.example.gusty.ui.theme.gray
import com.example.gusty.ui.theme.transparentBlack
import com.example.gusty.utilities.BackGrounds
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

        when (alarmUiState) {
            is UiStateResult.Failure -> {
                Log.i("TAG", "")
            }

            is UiStateResult.Success -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    BackGrounds.getFirstBackGround(),
                                    BackGrounds.getSecondBackGround()
                                )
                            )
                        )
                ) {
                    isLoading.value = false
                    LazyColumn {
                        itemsIndexed(alarmUiState.response) { _, alarm ->
                            AlarmItemCard(alarm, alarmViewModel)
                        }
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
    val currentWeather = alarmViewModel.currentWeather.collectAsStateWithLifecycle().value
    val isLoading = remember { mutableStateOf(false) }
    val currentDateMillis = System.currentTimeMillis()
    val formatedTimeToDisplay = remember { mutableStateOf("HH/MM Am") }
    val isTimeValid = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentDateMillis,
        initialDisplayedMonthMillis = currentDateMillis ,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= currentDateMillis
            }
        }
    )
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )
    var timeStamp by remember { mutableLongStateOf(0) }

    val formattedDateForDisplay = remember { mutableStateOf("dd/mm/yyyy") }
    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            if (millis != 0L) {
                formattedDateForDisplay.value = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(Date(millis))
            }
        }
    }

    LaunchedEffect(timePickerState.hour, timePickerState.minute) {
        val hour = timePickerState.hour
        val minute = timePickerState.minute
        val amPm = if (hour in 0..11) "AM" else "PM"
        val hourFormatted = if (hour % 12 == 0) 12 else hour % 12
        val minuteFormatted = String.format("%02d", minute)
        formatedTimeToDisplay.value = "$hourFormatted:$minuteFormatted $amPm"
    }

    LaunchedEffect(timePickerState.hour, timePickerState.minute) {
        if (timePickerState.hour <= currentTime.get(Calendar.HOUR_OF_DAY)
            && timePickerState.minute <= currentTime.get(Calendar.MINUTE)
        ) {
            isTimeValid.value = false
        } else {
            isTimeValid.value = true
        }
    }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = buttonSheetState,
        modifier = Modifier
            .fillMaxSize(), containerColor = transparentBlack
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
                        text = stringResource(R.string.select_date),
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
                        Text(text = stringResource(R.string.open_calender), color = Color.Black)
                    }

                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.selected_date),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = formattedDateForDisplay.value,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
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
                        text = stringResource(R.string.select_time),
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
                        Text(text = stringResource(R.string.open_timer), color = Color.Black)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = if (isTimeValid.value) stringResource(R.string.time_picked)
                        else stringResource(R.string.time_is_not_valid),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = formatedTimeToDisplay.value,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
            Spacer(Modifier.height(15.dp))
            Button(
                enabled = if (isTimeValid.value) true else false,
                onClick = {
                    alarmViewModel.getCurrentWeather(
                        LocationPermission.locationState.value.latitude,
                        LocationPermission.locationState.value.longitude,
                        UnitPreference.getUnitSharedPreference(context) ?: "metric" ,
                        LanguagePreference.getLanguagePref(context) ?: "en"
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
                            val androidAlarmManager = AndroidAlarmManager(context)
                            alarmViewModel.insertAlarm(
                                androidAlarmManager,
                                id = duration.toInt() / 1000,
                                day = formattedDateForDisplay.value,
                                place = countryName,
                                duration = duration,
                                time = formatedTimeToDisplay.value
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
                Text(
                    text = if (isLoading.value) stringResource(R.string.loading) else stringResource(
                        R.string.save_alarm
                    ),
                    color = Color.Black
                )
            }

        }
        if (openCalenderState.value) DatePickerModal(
            onDateSelected = {
                openCalenderState.value = false
                timeStamp = it ?: 0
            },
            onDismiss = { openCalenderState.value = false }, datePickerState
        )
        if (openTimePickerState.value) TimePickerDialog(
            onDismiss = {
                openTimePickerState.value = false
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
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
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
                Text(stringResource(R.string.confirm), color = Color.Black)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel), color = Color.Black)
            }
        },
        title = { Text(text = stringResource(R.string.select_time)) },
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
                Text(stringResource(R.string.delete), color = Color.Red)
            }
        },
        dismissButton = {
            androidx.compose.material.TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        title = {
            Text(
                text = stringResource(R.string.delete_alarm),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
        text = {
            Text(
                stringResource(R.string.are_you_sure_u_want_to_delete_this_alarm),
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    )
}
