package com.example.countdowntimerapp
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CountdownTimerApp()
        }
    }
}
@Composable
fun CountdownTimerApp() {
    var remainingTime by remember { mutableStateOf(60) }
    var timerActive by remember { mutableStateOf(false) }
    var timerJob by remember { mutableStateOf<Job?>(null) }
    var customTime by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    // Start/Stop timer logic
    fun startStopTimer() {
        if (timerActive) {
            timerJob?.cancel()
        } else {
            timerActive = true 
            timerJob = scope.launch {
                while (remainingTime > 0) {
                    delay(1000)
                    remainingTime -= 1
                }
                timerActive = false
                Toast.makeText(context, "Timer Finished", Toast.LENGTH_SHORT).show()
            }
        }
        timerActive = !timerActive
    }

    // Reset the timer
    fun resetTimer() {
        timerJob?.cancel()
        remainingTime = 60
        timerActive = false
        customTime = ""
    }

    // Set custom time if input is valid
    fun setCustomTime() {
        val customTimeValue = customTime.toIntOrNull()
        if (customTimeValue != null && customTimeValue > 0) {
            remainingTime = customTimeValue
        } else {
            Toast.makeText(context, "Please enter a valid number", Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Countdown Timer",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Display the remaining time
        Text(
            text = "$remainingTime seconds remaining",
            fontSize = 36.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Input field for custom countdown time
        TextField(
            value = customTime,
            onValueChange = { customTime = it },
            label = { Text("Enter custom time") },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Button to set custom time
        Button(
            onClick = { setCustomTime() },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = "Set Custom Time")
        }

        // Start/Stop button
        Button(
            onClick = { startStopTimer() },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = if (timerActive) "Stop" else "Start")
        }

        // Reset button
        Button(
            onClick = { resetTimer() },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = "Reset")
        }
    }
}
