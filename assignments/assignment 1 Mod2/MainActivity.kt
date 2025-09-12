package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                WeatherDashboard()
            }
        }
    }
}

@Composable
fun WeatherDashboard() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CurrentWeatherSection()
    }
}

@Composable
fun CurrentWeatherSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                RoundedCornerShape(16.dp)
            )
            .padding(20.dp)

    ) {
        WeatherAlertOverlay()
        Column(
            horizontalAlignment=Alignment.CenterHorizontally,
        ){
            Text(text = "72 F", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(text = "Partly Cloudy", fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(text = "Ann Arbor, MI", fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
            
            Row(
                modifier = Modifier.padding(top = 16.dp) ,
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Text(text = "H: 78 F", fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text(text = "L: 65 F", fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    }
}
@Composable
fun WeatherAlertOverlay() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopEnd
    ) {
        Card(
            modifier = Modifier.padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Red
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Severe Weather",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
/**
 * Preview for Android Studio's design view.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WeatherDashboardPreview(){
    WeatherDashboard()
}