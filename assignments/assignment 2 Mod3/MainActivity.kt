package com.example.test

/*
1. Why do we need to use remember and mutableStateOf for the selectedButtons variable, and what would happen if we used a normal variable instead?
2. What advantages does FlowRow provide compared to a simple Row or Column when arranging the buttons?
3. Why is it a good practice to separate the InteractiveButton into its own composable instead of creating the button logic directly inside the FlowRow?
4. What is the difference between background() and border() modifiers, and how do they work together to style the InteractiveButton?
5. Why is it better to group related information about a button into a data class, rather than keeping that information in multiple separate lists?
*/
import androidx.compose.ui.tooling.preview.Preview
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.ExperimentalLayoutApi

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                InteractiveButtonGrid()
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InteractiveButtonGrid() {
    var selectedButtons by remember { mutableStateOf(setOf<Int>()) }

    // List of button data (color, number) - already provided
    val buttonData = listOf(
        ButtonData(Color(0xFFE57373), "1"), // Red
        ButtonData(Color(0xFF81C784), "2"), // Green
        ButtonData(Color(0xFF64B5F6), "3"), // Blue
        ButtonData(Color(0xFFFFB74D), "4"), // Orange
        ButtonData(Color(0xFFBA68C8), "5"), // Purple
        ButtonData(Color(0xFF4DB6AC), "6"), // Teal
        ButtonData(Color(0xFFFF8A65), "7"), // Deep Orange
        ButtonData(Color(0xFF90A4AE), "8"), // Blue Grey
        ButtonData(Color(0xFFF06292), "9"), // Pink
        ButtonData(Color(0xFF7986CB), "10"), // Indigo
        ButtonData(Color(0xFF4DD0E1), "11"), // Cyan
        ButtonData(Color(0xFFFFD54F), "12"), // Yellow
        ButtonData(Color(0xFF8D6E63), "13"), // Brown
        ButtonData(Color(0xFF9575CD), "14"), // Deep Purple
        ButtonData(Color(0xFF4FC3F7), "15"), // Light Blue
        ButtonData(Color(0xFF66BB6A), "16"), // Light Green
        ButtonData(Color(0xFFFFCC02), "17"), // Amber
        ButtonData(Color(0xFFEC407A), "18"), // Pink
        ButtonData(Color(0xFF42A5F5), "19"), // Blue
        ButtonData(Color(0xFF26A69A), "20"), // Teal
        ButtonData(Color(0xFFFF7043), "21"), // Deep Orange
        ButtonData(Color(0xFF9CCC65), "22"), // Light Green
        ButtonData(Color(0xFF26C6DA), "23"), // Cyan
        ButtonData(Color(0xFFD4E157), "24")  // Lime
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).padding(top = 20.dp)
        ,horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Interactive Button Grid",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp))

        Text(text = "Selected: ${selectedButtons.size} of ${buttonData.size}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 24.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            buttonData.forEachIndexed { index, button ->
                InteractiveButton(
                    buttonData = button,
                    isSelected = selectedButtons.contains(index),
                    onClick = {selectedButtons = selectedButtons.plusElement(index) }
                    )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { selectedButtons = setOf() },
            enabled = selectedButtons.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear Selection")
        }
    }
}

@Composable
fun InteractiveButton(
    buttonData: ButtonData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .background(if (isSelected){MaterialTheme.colorScheme.primaryContainer}
                else {buttonData.color},shape = MaterialTheme.shapes.medium)
            .border(if(isSelected) {BorderStroke(width = 3.dp, color = MaterialTheme.colorScheme.primary)}
                else {BorderStroke(width = 1.dp, color = Color.Black.copy(alpha = 0.3f))},shape = MaterialTheme.shapes.medium)
            .clickable {onClick()}
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = buttonData.number,
            color = if(isSelected){MaterialTheme.colorScheme.onPrimaryContainer}else{Color.White}
            ,fontSize = 18.sp, fontWeight = FontWeight.Bold
        )
    }
}

// Data class to hold button information
data class ButtonData(
    val color: Color,
    val number: String
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InteractiveButtonGridPreview() {
    InteractiveButtonGrid()
}