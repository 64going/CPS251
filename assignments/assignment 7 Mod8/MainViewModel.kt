package com.example.test

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    
    var students by mutableStateOf<List<Student>>(emptyList())
        private set // 'private set' means the 'students' list can be read externally but only modified within this ViewModel.

    var isLoading by mutableStateOf(false)
        private set

    var newStudentName by mutableStateOf("")
        private set

    var newStudentGrade by mutableStateOf("")
        private set

    fun addStudent(name: String, grade: String) {
        students += Student(name, grade = grade.toFloat())
        newStudentName = ""
        newStudentGrade = ""
    }

    fun removeStudent(student: Student) {
        if(students.contains(student)){
            students = students - student
        }
    }

    fun calculateGPA(): Float {
        var studentGPA = 0.0
        if(students.isNotEmpty()){
            for (student in students){
                studentGPA += student.grade
            }
            return (studentGPA / students.size).toFloat()
        }else{
            return 0f
        }
    }

    fun loadSampleData() {
        viewModelScope.launch {
            try {
                isLoading = true
                delay(1500)
                students += listOf(
                    Student("Carol Davis", 92f),
                    Student("Bob Smith", 87f),
                    Student("Alice Johnson", 95f)
                )
            } catch (e: Exception) {
                //show Toast loading failed
            } finally {
                isLoading = false
            }
        }
    }

    fun updateNewStudentName(name: String) {
        newStudentName = name
    }

    fun updateNewStudentGrade(grade: String) {
        newStudentGrade = grade
    }
}

data class Student(
    val name: String,
    val grade: Float
)