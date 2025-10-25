package com.example.test

// Core Android imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

// Compose UI imports
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// ViewModel imports
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.test.MainViewModel
/*
1. How does the MainViewModel effectively manage the various pieces of data (e.g., student list, loading status, input fields) that need to be observed and updated by the UI?
2. Explain how the interaction between the StudentGradeManager Composable and the MainViewModel demonstrates a unidirectional data flow. Why is this pattern beneficial for UI development?
3. Describe the lifecycle of the MainViewModel in relation to MainActivity. Why is it important for the ViewModel to outlive configuration changes, and how does viewModel() assist with this?
4. Explain the key advantages of using LazyColumn for displaying the list of students instead of a regular Column with a scroll modifier. When would you choose one over the other?
5. How does the design of this application (with MainActivity, MainViewModel, and various @Composable functions) exemplify the principle of separation of concerns? What are the benefits of this approach?
*/

/**
 * MainActivity is the entry point of the application.
 * It sets up the basic Compose UI.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the Compose UI
        setContent {
            MaterialTheme {
                Surface {
                    StudentGradeManager()
                }
            }
        }
    }
}

@Composable
fun StudentGradeManager(
    viewModel: MainViewModel = viewModel()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 50.dp)
    ) {
        item{
            Text("Student Grade Manager", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))
        }
        item {
            GPADisplay(viewModel.calculateGPA())
        }
        item {
            AddStudentForm(
                name = viewModel.newStudentName,
                grade = viewModel.newStudentGrade,
                onNameChange = {viewModel.updateNewStudentName(it)},
                onGradeChange = {viewModel.updateNewStudentGrade(it)},
                onAddStudent = {viewModel.addStudent(viewModel.newStudentName, viewModel.newStudentGrade)}
            )
        }
        item {
            Button(onClick = {viewModel.loadSampleData()}, modifier = Modifier.padding(vertical = 8.dp)) {
                Text("Load Sample Data")
            }
        }
        item {
            StudentsList(viewModel.students,{viewModel.removeStudent(it)})
        }
        item {
            if(viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun GPADisplay(gpa: Float) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Class GPA", style = MaterialTheme.typography.titleMedium)
            Text("%.2f".format(gpa), style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun AddStudentForm(
    name: String,
    grade: String,
    onNameChange: (String) -> Unit,
    onGradeChange: (String) -> Unit,
    onAddStudent: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Add New Student", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))

            OutlinedTextField(
                value = name,
                onValueChange = {onNameChange(it)},
                label = {Text("Student Name")},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = grade,
                onValueChange = {onGradeChange(it)},
                label = {Text("Grade (0-100)")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {onAddStudent()},
                enabled = name.isNotBlank() && grade.isNotBlank(),
                modifier = Modifier.fillMaxWidth()) {
                    Text("Add Student")
            }
        }
    }
}

@Composable
fun StudentsList(
    students: List<Student>,
    onRemoveStudent: (Student) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .heightIn(max = 300.dp) // Limit height to prevent overflow
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Students (${students.size})", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))

            if (students.isEmpty()) {
                Text("No students added yet", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 200.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items (students){ student ->
                        StudentRow(student, {onRemoveStudent(student)})
                        if(students.indexOf(student) != students.lastIndex){
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}
// Inside items, call StudentRow for each student and a Divider if it's not the last student
@Composable
fun StudentRow(
    student: Student,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(student.name, style = MaterialTheme.typography.bodyLarge)
            Text("Grade: ${student.grade}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        IconButton(onClick = {onRemove()}) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove student"
            )
        }
    }
}

/**
 * Preview function for the StudentGradeManager screen
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StudentGradeManagerPreview() {
    MaterialTheme {
        StudentGradeManager()
    }
}
