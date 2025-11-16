package com.example.test

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/*
1. How does the app handle dark and light themes? What is the purpose of defining separate color schemes for each theme?
2. The app animates card colors and elevations when notes are marked as important. Explain the purpose of using animationSpec with tween() in these animations?
3. When the floating action button is clicked, the app requests focus on a text field. Explain why this focus management is important for user experience?
4. The app defines a custom shapes system with different corner radius values for different sizes (extraSmall, small, medium, large, extraLarge). Explain how this hierarchical shape system contributes to visual consistency in the Material Design system?
5. The app uses AnimatedVisibility to show and hide note content when cards are clicked. Explain the difference between using AnimatedVisibility versus simply using an if statement to conditionally show content. What user experience benefit does the animation provide?
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(viewModel: NoteViewModel) {
    val notes by viewModel.notes.collectAsState()

    val titleFocus = remember { FocusRequester() }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    var isTitleEmpty by remember { mutableStateOf(false) }
    var isContentEmpty by remember { mutableStateOf(false) }

    var isEditing by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<Note?>(null) }
    var showDeleteDialog by remember { mutableStateOf<Note?>(null) }

    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }

    Scaffold(
        topBar = {TopAppBar(
            title = {Text("Material Notes")},
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ))},
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {FloatingActionButton(onClick = {editingNote = null;isEditing = false; title = ""; content = "";titleFocus.requestFocus()},
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            shape = MaterialTheme.shapes.medium
        ){ Icon(Icons.Filled.Add, contentDescription = "Add Note")} }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(16.dp)
            .fillMaxSize()
            ,horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = MaterialTheme.shapes.large,
                ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()){
                    Text(if(isEditing){"Edit Note"}else{"Create New Note"},
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp),
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it;isTitleEmpty = title.isEmpty() },
                    label = { Text("Title") },
                    isError = isTitleEmpty,
                    supportingText = {if(isTitleEmpty){Text("Title cannot be empty")} },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .focusRequester(titleFocus)
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it;isContentEmpty = content.isEmpty() },
                    label = { Text("Content") },
                    isError = isContentEmpty,
                    supportingText = {if(isContentEmpty){Text("Content cannot be empty")} },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    minLines = 3
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)) {
                    Button(onClick = {
                        if (title.isNotBlank() && content.isNotBlank()) {
                            if (editingNote == null) {
                                viewModel.addNote(title, content, dateFormat.format(Date()))
                            } else {
                                viewModel.deleteNote(editingNote!!)
                                viewModel.addNote(title, content, dateFormat.format(Date()))
                                editingNote = null
                                isEditing = false
                            }
                            title = ""
                            content = ""
                        }
                    }, enabled = title.isNotBlank() && content.isNotBlank()) {
                        Text(if (editingNote == null) "Add Note" else "Update Note")
                    }
                    if (editingNote != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedButton(onClick = {
                            isEditing = false
                            editingNote = null
                            title = ""
                            content = ""
                        }) {
                            Text("Cancel Edit")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(contentAlignment = Alignment.TopStart, modifier = Modifier.fillMaxWidth()){
                Text("Your Notes", style = MaterialTheme.typography.titleLarge)
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp)
            ) {
                items(notes.size) { idx ->
                    val note = notes[idx]

                    var isFavorited  by remember { mutableStateOf(false) }
                    var anamate  by remember { mutableStateOf(false) }

                    val cardElevation by animateDpAsState(
                        targetValue = if (isFavorited) 8.dp else 2.dp,
                        animationSpec = tween(
                            durationMillis = 300,
                        ),
                        label = "cardElevation"
                    )
                    val cardColor by animateColorAsState(
                        targetValue = if (isFavorited) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        animationSpec = tween(durationMillis = 300),
                        label = "cardColor"
                    )
                    val cardHeight by animateDpAsState(
                        targetValue = if (anamate) 140.dp else 150.dp,
                        animationSpec = tween(
                            durationMillis = 300,
                            ),
                            label = "cardHeight"
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(cardHeight)
                            .padding(vertical = 8.dp)
                            .clickable {isEditing = true; title = note.title; content = note.content;editingNote = note; viewModel.viewModelScope.launch { try{anamate = true; delay(300); anamate = false} catch (e: Exception){} }},
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(cardElevation),
                        colors = CardDefaults.cardColors(cardColor)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(note.title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(start = 16.dp, top = 16.dp),color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(note.content, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 16.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)

                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "Last updated: ${note.date}",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(end = 16.dp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Row(horizontalArrangement = Arrangement.Center) {
                                        IconButton(onClick = {
                                            isFavorited = !isFavorited
                                        }) {
                                            Icon(
                                                imageVector = if(isFavorited){Icons.Default.Star}else{Icons.Outlined.StarBorder},
                                                contentDescription = "Search button",
                                                tint = if(isFavorited){MaterialTheme.colorScheme.secondary}else{
                                                    MaterialTheme.colorScheme.onSurfaceVariant}
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        IconButton(onClick = { showDeleteDialog = note }) {
                                            Icon(
                                                imageVector = Icons.Filled.Delete,
                                                contentDescription = "Delete button",
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (showDeleteDialog != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text("Delete Note") },
                text = { Text("Are you sure you want to delete this note: ${showDeleteDialog?.title}?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteNote(showDeleteDialog!!)
                        showDeleteDialog = null
                        if (editingNote == showDeleteDialog) {
                            editingNote = null
                            title = ""
                            content = ""
                        }
                    }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}