package net.natsucamellia.lifeclock.ui.screens

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import net.natsucamellia.lifeclock.R
import net.natsucamellia.lifeclock.ui.model.LifeClockModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    lifeClockModel: LifeClockModel,
    navigateUp: () -> Unit
) {
    val datePickerState = rememberDatePickerState(lifeClockModel.birthdayEpoch)
    val age = lifeClockModel.age
    var openDatePickerDialog by remember { mutableStateOf(false) }
    var openYearChip by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat.getDateInstance()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(stringResource(R.string.setting))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        datePickerState.selectedDateMillis?.let { lifeClockModel.updateBirthdayEpoch(it) }
                        lifeClockModel.updateAge(age)
                        lifeClockModel.updateClock()
                        navigateUp()
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            IconPreference(
                title = "Birthday",
                summary = dateFormat.format(datePickerState.selectedDateMillis),
                imageVector = Icons.Outlined.Cake,
                onClick = { openDatePickerDialog = true }
            )
//            Divider()
            IconPreference(
                title = "Expected Age",
                summary = "$age years old",
                imageVector = Icons.Outlined.Timer,
                onClick = { openYearChip = true }
            )
        }

        if (openDatePickerDialog) {
            DatePickerDialog(
                onDismissRequest = { openDatePickerDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = { openDatePickerDialog = false }
                    ) {
                        Text(text = "OK")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = true
                )
            }
        }

        if (openYearChip) {
            ModalBottomSheet(
                onDismissRequest = { openYearChip = false }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Expected Age $age")
                }
                Slider(
                    value = age.toFloat(),
                    onValueChange = { value -> lifeClockModel.updateAge(value.roundToInt()) },
                    valueRange = 0f..100f,
                    modifier = Modifier.padding(24.dp)
                )
            }
        }
    }
}

@Composable
fun SettingsCategory(
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun IconPreference(
    title: String,
    summary: String? = null,
    imageVector: ImageVector,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick.invoke()
            }
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null
        )
        Spacer(Modifier.width(16.dp))
        PreferenceItem(
            title = title,
            summary = summary
        )
    }
}

@Composable
fun PreferenceItem(
    modifier: Modifier = Modifier,
    title: String,
    summary: String? = null
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            title,
            overflow = TextOverflow.Ellipsis
        )
        if (summary != null) {
            Spacer(Modifier.height(2.dp))
            Text(summary, style = MaterialTheme.typography.bodySmall)
        }
    }
}