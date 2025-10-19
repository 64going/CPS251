package com.example.test

// Core Android imports
import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat

// Compose UI imports
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.TextButton
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Navigation imports
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
/*
1. The LoginApp composable uses NavHost and rememberNavController(). Explain the primary purpose of NavHost and NavController in defining the overall navigation structure of an Android Compose application.
2. The LoginApp defines routes like "welcome/{userName}" and "profile/{userName}".Explain the mechanism used to pass the userName data from the LoginScreen to WelcomeScreen and ProfileScreen. Specifically, reference how arguments are defined in the route pattern and retrieved in the target composable as discussed in the "Passing Arguments" lesson.
3. The solution you are to create breaks down the UI into LoginScreen, WelcomeScreen, ProfileScreen.  Discuss the advantages of organizing UI into distinct composable functions and potentially separate files for improved maintainability and readability, even if this specific project keeps them in one file.
4. For the email field either the email is of the wrong format, the incorrect email, or is fine.  Show the code where you programed the logic so it knows what error message to display based upon what is wrong.
5. Although Top App Bar and Bottom Navigation Bar are discussed in navigation_ui.html, this solution primarily uses buttons for navigation within the content area. Discuss a scenario in a larger application where you might choose to implement a Bottom Navigation Bar (as described in navigation_ui.html) as the primary navigation instead of simple buttons, and what benefits it would offer.*/
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        setContent {
            MaterialTheme {
                Surface {
                    LoginApp()
                }
            }
        }
    }
}
/**
 * Main navigation app that handles the login flow
 */
@Composable
fun LoginApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login"){
            LoginScreen(onLoginSuccess = {navController.navigate("welcome/$it")})
        }
        composable(route = "welcome/{userName}"){ backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userName")
            WelcomeScreen(userId.toString(), onViewProfile = {navController.navigate("profile/$userId")},
                onLogout = {navController.navigate("login")})
        }
        composable("profile/{userName}"){  backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userName").toString()
            ProfileScreen(userId, onBackToWelcome = {navController.navigate("welcome/$userId")})
        }
    }
}
@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var validEmailBool by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val validPassword = "password123"
    val validEmail = "student@wccnet.edu"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Student Login", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)

        Card(elevation = CardDefaults.cardElevation(.8.dp), modifier = Modifier.padding(8.dp)) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; nameError = name.isEmpty() },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") },
                    isError = nameError,
                    placeholder = { Text("Full Name", color = if(nameError){ MaterialTheme.colorScheme.error}else{Color.Unspecified}) },
                    supportingText = { if(nameError){Text("Please enter your name")} },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; emailError = !isValidEmail(email) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = "email") },
                    isError = emailError,
                    placeholder = { Text("Email", color = if(nameError){ MaterialTheme.colorScheme.error}else{Color.Unspecified}) },
                    supportingText = {
                        if (!isValidEmail(email)&& emailError) {
                            Text("Please enter a valid email")
                        }
                        else if (!validEmailBool && emailError){
                            Text("please enter the correct email")
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "password") },
                    placeholder = { Text("Password", color = if(nameError){ MaterialTheme.colorScheme.error}else{Color.Unspecified}) },
                    trailingIcon = {
                        TextButton(onClick = {
                            passwordVisible = !passwordVisible
                        }) { Text("Show", color = MaterialTheme.colorScheme.primary) }
                    },
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    isError = passwordError,
                    supportingText = {
                        if (passwordError) {
                            Text("Please enter the correct password")
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button( modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (name.isEmpty()){ nameError = true }
                        if (!isValidEmail(email)){
                            emailError = true
                        }else if(email != validEmail){
                            validEmailBool = false
                            emailError = true
                        }else{
                            validEmailBool = true
                        }
                        passwordError = password != validPassword
                        if(!nameError && validEmailBool && !passwordError) { onLoginSuccess(name.trim()) }
                    }
                ) {
                    Text("Login")
                }
                Text("Demo /email: $validEmail /password: $validPassword", fontWeight = FontWeight.Light, fontSize = 14.sp)
            }
        }
    }
}
/**
 * Welcome screen that displays after successful login
 */
@Composable
fun WelcomeScreen(
    userName: String,
    onViewProfile: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Text("Hello $userName", style = MaterialTheme.typography.headlineMedium)

        Button(onClick = { onViewProfile() }) {
            Text("View Profile")
        }
        Button(onClick = { onLogout() }) {
            Text("Logout")
        }
    }
}
/**
 * Profile screen showing user information
 */
@Composable
fun ProfileScreen(
    userName: String,
    onBackToWelcome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("User Profile", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(32.dp))

        Card (modifier = Modifier.fillMaxWidth().padding(16.dp)){
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProfileRow("Name ",userName)
                ProfileRow("Email ","student@wccnet.edu")
                ProfileRow("Student ID ","2024001")
                ProfileRow("Major ","Computer Science")
                ProfileRow("Year ","Freshman")
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = { onBackToWelcome() }) {
            Text("Back to Welcome")
        }
    }
}
@Composable
fun ProfileRow(label: String, value: String) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
fun isValidEmail(email: String): Boolean {
    return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex())
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(onLoginSuccess = {})
    }
}
/**
 * Preview function for the welcome screen
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    MaterialTheme {
        WelcomeScreen(
            userName = "John Doe",
            onViewProfile = {},
            onLogout = {}
        )
    }
}
/**
 * Preview function for the profile screen
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen(
            userName = "John Doe",
            onBackToWelcome = {}
        )
    }
}
