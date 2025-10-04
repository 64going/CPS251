package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/*
1.Show me your ContactForm function and point to where you declare your state variables for form fields. Explain why you need separate variables for the input values and validation states. Of if you did not have seperate variables why not?
2.Show me one of your input fields (like NameField or EmailField) and point to the error part. Explain how it creates visual feedback for users
3.Point to your validateEmail function and explain the regex pattern you used. What would happen if you changed the pattern to something simpler like just checking for '@' and '.' characters? Why is the current pattern more robust?
4.Point to your Submit button and explain the enabled condition. What would happen if you simplified the condition to just check if all fields are not empty without validation?
5.Point to your Submit button and explain how it gets the text to appear below it when clicked.?
*/
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ContactValidatorApp()
            }
        }
    }
}

@Composable
fun ContactValidatorApp() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Contact Information",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        ContactForm()
    }
}

@Composable
fun ContactForm() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }

    var isNameValid by remember { mutableStateOf(true) }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPhoneValid by remember { mutableStateOf(true) }
    var isZipCodeValid by remember { mutableStateOf(true) }

    var submittedInfo by remember { mutableStateOf("") }
    val nameRegex = "^[A-Za-z\\s]{2,}$".toRegex()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(
                horizontal = 12.dp,
                vertical = 12.dp
    ),
    verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NameField(name,isNameValid) {
            name = it
            isNameValid = it.isEmpty() || it.matches(nameRegex)
        }
        EmailField(email,isEmailValid) {
           email = it
           isEmailValid = it.isEmpty() || validateEmail(it)
        }
        PhoneField(phone,isPhoneValid) {
            phone = it
            isPhoneValid = it.isEmpty() || validatePhone(it)
        }
        ZipCodeField(zipCode,isZipCodeValid) {
            zipCode = it
            isZipCodeValid = it.isEmpty() || validateZipCode(it)
        }
        Button(
            onClick = {
                submittedInfo = "Name: $name \nEmail: $email \nPhone: $phone \nZIP Code: $zipCode"
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isNameValid && isEmailValid && isPhoneValid &&
                    isZipCodeValid && name.isNotEmpty() &&
                    email.isNotEmpty() && phone.isNotEmpty() &&
                    zipCode.isNotEmpty()
        ) {
            Text("Submit")
        }

        Text(submittedInfo)
    }
}

@Composable
fun NameField(
    name: String,
    isNameValid: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = name,
        onValueChange = onValueChange,
        label = { Text("Name") },
        isError = !isNameValid && name.isNotEmpty(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
        ),
        supportingText = {
            if (!isNameValid && name.isNotEmpty()) {
                Text("Name must be at least 2 characters and contain only letters and spaces")
            }
        }
    )
}

@Composable
fun EmailField(
    email: String,
    isEmailValid: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = email,
        onValueChange = onValueChange,
        label = { Text("Email") },
        isError = !isEmailValid && email.isNotEmpty(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
        ),
        supportingText = {
            if (!isEmailValid && email.isNotEmpty()) {
                Text("Please enter a valid email address")
            }
        }
    )
}

@Composable
fun PhoneField(
    phone: String,
    isPhoneValid: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = phone,
        onValueChange = onValueChange,
        label = { Text("Phone Number") },
        isError = !isPhoneValid && phone.isNotEmpty(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
        ),
        supportingText = {
            if (!isPhoneValid && phone.isNotEmpty()) {
                Text("Please enter a valid format using [/ -]\n000-000-0000")
            }
        }
    )}

@Composable
fun ZipCodeField(
    zipCode: String,
    isZipValid: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = zipCode,
        onValueChange = onValueChange,
        label = { Text("ZIP Code") },
        isError = !isZipValid && zipCode.isNotEmpty(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        supportingText = {
            if (!isZipValid && zipCode.isNotEmpty()) {
                Text("Please enter a valid zipCode")
            }
        }
    )
}

fun validateEmail(email :String): Boolean {
    return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex())
}
fun validatePhone(phone :String): Boolean {
    return phone.matches("^\\d{3}[-/\\s]\\d{3}[-/\\s]\\d{4}$".toRegex())
}
fun validateZipCode(zipCode :String): Boolean {
    return zipCode.matches("^\\d{5}(-\\d{4})?$".toRegex())
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ContactValidatorAppPreview() {
    ContactValidatorApp()
}