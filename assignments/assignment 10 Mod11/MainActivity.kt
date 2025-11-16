package com.example.test

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.example.test.ui.theme.DarkColorScheme
import com.example.test.ui.theme.LightColorScheme
import com.example.test.ui.theme.Shapes
import com.example.test.ui.theme.Typography


//import com.example.test.ui.theme.RoomDatabaseDemoTheme

// MainActivity is the entry point of the Android application.
class MainActivity : ComponentActivity() {
    // onCreate is called when the activity is first created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configure the window to use light status bar icons for a modern look.
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        // Initialize the ViewModel using by viewModels.
        // The provideFactory method ensures the ViewModel has access to the application context.
        val viewModel: NoteViewModel by viewModels {
            NoteViewModel.provideFactory(application)
        }

        // Set up the Compose UI content for this activity.
        setContent {
            var isDark by remember { mutableStateOf(false) }
            isDark = isSystemInDarkTheme()
            // Apply the custom theme for the application.
            MaterialTheme(
                colorScheme = if(isDark){DarkColorScheme}else{LightColorScheme},
                shapes = Shapes
            ) {
                // Surface is a fundamental composable that applies background color and fills the screen.
                Surface(
                ) {
                    // Display the NoteScreen, passing the initialized ViewModel to it.

                    NoteScreen(viewModel)
                }
            }
        }
    }
}