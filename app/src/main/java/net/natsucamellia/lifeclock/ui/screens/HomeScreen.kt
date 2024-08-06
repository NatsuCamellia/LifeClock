package net.natsucamellia.lifeclock.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.natsucamellia.lifeclock.R
import net.natsucamellia.lifeclock.ui.model.LifeClockModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    lifeClockModel: LifeClockModel,
    onClickSettings: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(onClick = onClickSettings) {
                        Icon(imageVector = Icons.Outlined.Settings, contentDescription = null)
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                Text(
                    text = "How will you spend?",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                TimeDisplay(remainMillis = lifeClockModel.remainMillis)
            }
                TimerController(
                    isClockRunning = lifeClockModel.remainMillis > 0,
                    onPlayClicked = onClickSettings,
                    onPauseClicked = {
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar("Life goes on!")
                        }
                    },
                    onStopClicked = {
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar("Don't give up!")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                )
        }
    }
}

@Composable
fun TimeDisplay(
    remainMillis: Long,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
    ) {
        val hours = remainMillis / 3600000
        val minutes = remainMillis % 3600000 / 60000
        val seconds = remainMillis % 60000 / 1000
        val hundreds = remainMillis % 1000 / 10

        Text(text = hours.toString(), style = MaterialTheme.typography.displayMedium)
        Text(text = ":", style = MaterialTheme.typography.displayMedium)
        Text(text = minutes.addZero(), style = MaterialTheme.typography.displayMedium)
        Text(text = ":", style = MaterialTheme.typography.displayMedium)
        Text(text = seconds.addZero(), style = MaterialTheme.typography.displayMedium)
        Text(text = ".", style = MaterialTheme.typography.displaySmall)
        Text(text = hundreds.addZero(), style = MaterialTheme.typography.displaySmall)
    }
}

fun Long.addZero(): String {
    if (this >= 10) return this.toString()
    return "0$this"
}

@Composable
fun TimerController(
    isClockRunning: Boolean,
    onPlayClicked: () -> Unit,
    onPauseClicked: () -> Unit,
    onStopClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isClockRunning) {
            LargeFloatingActionButton(
                shape = CircleShape,
                onClick = onPauseClicked
            ) {
                Icon(
                    Icons.Default.Pause,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                onClick = onStopClicked
            ) {
                Icon(Icons.Default.Stop, null)
            }
        } else {
            LargeFloatingActionButton(
                shape = CircleShape,
                onClick = onPlayClicked
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null
                )
            }
        }
    }
}