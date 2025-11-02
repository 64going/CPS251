package com.example.test


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


/*
1. Having the list update as you type the letters in the search box was not discussed in class.  Go through the code and explain how you implimented that functionality?
2. Explain the purpose of a Data Access Object (DAO) in this type of application. What specific operations would you expect a Contact DAO to perform?
3. Describe the primary responsibilities of the ContactViewModel. How does it facilitate communication between the UI and the data layer, and what problem does it solve in the context of Android lifecycles?
4. How does the application handle and observe changes in the list of contacts or the search query to keep the UI updated? What pattern or mechanism is typically used for this in modern Android UI development?
5. Briefly describe the overall architecture of this application, mentioning the key layers (UI, ViewModel, Repository, Database) and how they interact. Why is this architectural pattern beneficial?
*/
@Composable
fun MainScreenManager (viewModel: ContactViewModel){

    val contacts by viewModel.allContacts.collectAsState(listOf())
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var searchName by remember { mutableStateOf("") }
    var nameEmpty by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<Contact?>(null) }
    var isPhoneValid by remember { mutableStateOf(true) }
    val contactsList = contacts
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
    ) {
       item{
           MainScreen(
               name = name,
               phoneNumber = phoneNumber,
               searchName = searchName,
               isPhoneValid = isPhoneValid,
               onSortOrderChange = {viewModel.onSortOrderChange(it)},
               addOnClick = {
                   if(viewModel.isValidPhoneNumber(phoneNumber) && !nameEmpty) {
                       viewModel.insert(contact = Contact(name = name, phoneNumber = phoneNumber))
                       isPhoneValid = true
                       nameEmpty = name.isEmpty()
                   } else {
                       isPhoneValid = viewModel.isValidPhoneNumber(phoneNumber)
                       nameEmpty = name.isEmpty()
                   } },
               onValueChangeNameSearchName = {searchName = it; viewModel.onSearchQueryChange(searchName)},
               onValueChangePhoneNumber = {phoneNumber = it},
               onValueChangeName= {name = it; nameEmpty = name.isEmpty()},
               nameEmpty = nameEmpty
           )
       }
        item {
            ContactView(contactsList, onRemove = { showDeleteDialog=it})
        }
    }
    if (showDeleteDialog != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Delete Contact") },
            text = { Text("Are you sure you want to delete this contact?") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog?.let { viewModel.delete(it) }
                    showDeleteDialog = null
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


@Composable
fun MainScreen(
    name: String,
    phoneNumber: String,
    searchName: String,
    onValueChangeName: (String) -> Unit,
    onValueChangePhoneNumber: (String) -> Unit,
    onValueChangeNameSearchName: (String) -> Unit,
    isPhoneValid: Boolean,
    addOnClick:()-> Unit,
    onSortOrderChange:(SortOrder)-> Unit,
    nameEmpty: Boolean,
){

    Column (
        modifier = Modifier.fillMaxSize().padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ){
        OutlinedTextField(
            value = name,
            onValueChange = {onValueChangeName(it)},
            label = { Text("Name") },
            isError = nameEmpty,
            supportingText = {if(nameEmpty){Text("Name cannot be empty")}},
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        )
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {onValueChangePhoneNumber(it)},
            label = { Text("Phone Number(10 digits)") },
            isError = !isPhoneValid,
            supportingText = {if(!isPhoneValid){Text("Invalid phone number format should be like 999.999.9999")}},
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            Arrangement.SpaceEvenly
        ) {
            Button(onClick = {addOnClick()}) {
                Text("Add")
            }
            Button(onClick = {onSortOrderChange(SortOrder.DESC)}) {
                Text("Sort Desc")
            }
            Button(onClick = {onSortOrderChange(SortOrder.ASC)}) {
                Text("Sort Asc")
            }
        }
        OutlinedTextField(
            value = searchName,
            onValueChange = {onValueChangeNameSearchName(it)},
            label = { Text("Search Name") },
            modifier = Modifier.fillMaxWidth()
        )
        HorizontalDivider()
        Text("Contacts:", style = MaterialTheme.typography.headlineLarge)
    }

}
@Composable
fun ContactView(
    contactsList: List<Contact>,
    onRemove: (Contact) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .heightIn(max = 500.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            if (contactsList.isEmpty()) {
            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 200.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items (contactsList){ contact ->
                        ContactRow(contact, {onRemove(contact)})
                            HorizontalDivider()

                    }
                }
            }
        }
    }
}
    @Composable
    fun ContactRow(
        contact: Contact,
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
                Text(contact.name, style = MaterialTheme.typography.bodyLarge)
                Text(contact.phoneNumber, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = {onRemove()}) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove contact"
                )
            }
        }
    }