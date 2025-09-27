package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
/*
1. What are the advantages of using remember and mutableStateOf for managing state in a Compose application, and how would you apply them to manage the isRunning and timeRemaining states in this project?
2. What is the difference between stateless and stateful composables, and why is it important to keep TimerDisplay stateless in this project?
3. How would you implement a countdown timer using LaunchedEffect in Compose, and what considerations should you make to ensure the timer stops when it reaches zero?
4. Why is it beneficial to separate the TimerControls and SessionSettings into their own composables, and how does this improve the reusability and readability of the code?
5. What is the role of LaunchedEffect in managing side effects in Compose, and how would you use it to ensure the timer updates every second only when isRunning is true?
*/

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                StudyTimerApp()
            }
        }
    }
}

@Composable
fun StudyTimerApp() {
    var isRunning by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableIntStateOf(900) }
    var sessionLength by remember { mutableIntStateOf(15) }
    var completedSessions by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimerDisplay(timeRemaining,sessionLength)
        Spacer(modifier = Modifier.height(32.dp))

        TimerControls(isRunning, onToggleTimer = { isRunning = !isRunning })

        Spacer(modifier = Modifier.height(32.dp))

        SessionSettings(sessionLength) { sessionLength = it; timeRemaining = it*60; isRunning = false}
        Spacer(modifier = Modifier.height(16.dp))

        Text("Completed Sessions $completedSessions", fontWeight = FontWeight.Bold)
    }

    LaunchedEffect(isRunning){
        while (isRunning){
            delay(1000)
            timeRemaining = timeRemaining - 1
            if(timeRemaining <= 0){
                isRunning = false
                completedSessions++
                timeRemaining = 0
            }
        }
    }
}

@Composable
fun TimerDisplay(
    timeRemaining: Int,
    sessionLength: Int
) {
    val minutes = timeRemaining / 60
    val seconds = timeRemaining % 60
    val progress = 1f - (timeRemaining.toFloat() / (sessionLength * 60))
    Text("Study Timer", fontSize = 40.sp, fontWeight = FontWeight.Bold)
    Text(String.format("%02d:%02d", minutes, seconds), fontSize = 50.sp, fontWeight = FontWeight.Bold)
    Text("${(progress *100).toInt()}% Complete")
}

@Composable
fun TimerControls(
    isRunning: Boolean,
    onToggleTimer: () -> Unit
) {
    Button(onClick = {onToggleTimer()}) {
        if(!isRunning){
            Text("Start")
        }else{
            Text("Reset")
        }
    }
}

@Composable
fun SessionSettings(
    sessionLength: Int,
    onSessionLengthChange: (Int) -> Unit,
) {

    Text("Session Length: $sessionLength minutes", fontWeight = FontWeight.Bold)
    Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        var button1 by remember { mutableStateOf(false)}
        var button2 by remember { mutableStateOf(true)}
        var button3 by remember { mutableStateOf(false)}
        var button4 by remember { mutableStateOf(false)}
        Button(
            onClick = { onSessionLengthChange(5); button1 = true;button2 = false;button3 = false;button4 = false},
            colors = (if(button1){ButtonDefaults.buttonColors(containerColor = Color.Unspecified)}else{ButtonDefaults.buttonColors(containerColor = Color.LightGray)})
        ) {
            Text("5m")
        }
        Button(
            onClick = { onSessionLengthChange(15); button1 = false;button2 = true;button3 = false;button4 = false },
            colors = (if(button2){ButtonDefaults.buttonColors(containerColor = Color.Unspecified)}else{ButtonDefaults.buttonColors(containerColor = Color.LightGray)})
        ) {
            Text("15m")
        }
        Button(
            onClick = { onSessionLengthChange(25); button1 = false;button2 = false;button3 = true;button4 = false},
            colors = (if(button3){ButtonDefaults.buttonColors(containerColor = Color.Unspecified)}else{ButtonDefaults.buttonColors(containerColor = Color.LightGray)})
        ) {
            Text("25m")
        }
        Button(
            onClick = { onSessionLengthChange(45); button1 = false;button2 = false;button3 = false;button4 = true},
            colors = (if(button4){ButtonDefaults.buttonColors(containerColor = Color.Unspecified)}else{ButtonDefaults.buttonColors(containerColor = Color.LightGray)})
        ) {
            Text("45m")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudyTimerPreview() {
    StudyTimerApp()
}