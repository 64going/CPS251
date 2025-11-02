package com.example.test

// Core Android imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: ContactViewModel by viewModels {
            ContactViewModel.provideFactory(application)
        }
        setContent {
            MaterialTheme {
                Surface {
                    MainScreenManager(viewModel)
                }
            }
        }
    }
}