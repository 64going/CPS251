package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview

/**
 1. Why should you use LazyColumn instead of Column for the contact list, and what runtime benefits does it provide for long lists?
 2. When should you use rememberSaveable instead of remember for tracking a selected contact, and what kinds of configuration changes or process deaths does it protect against?
 3. Explain how Modifier order (e.g., padding before vs after background) changes layout and appearance; give one practical reason this matters for the contact item card.
 4. How would you implement multi‑selection (data structure, toggle behavior, and UI affordances such as a selection count and bulk actions)?
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ContactListApp()
                }
            }
        }
    }
}
@Composable
fun ContactListApp() {
    val contacts = listOf(
        Contact("John Doe", "john@example1.com", "123-555-0101",false),
        Contact("Bob Doe", "Bob@example2.com", "124-555-0101",false),
        Contact("John Smeth", "john@example3.com", "125-555-0101",false),
        Contact("Will Doe", "Will@example4.com", "126-555-0101",false),
        Contact("John Doe2", "john@example5.com", "127-555-0101",false),
        Contact("John Brown", "brown@example6.com", "128-555-0101",false),
        Contact("John Doe3", "john@example7.com", "129-555-0101",false),
        Contact("John Doe4", "john@example8.com", "130-555-0101",false),
        Contact("Bill White", "Bill@example9.com", "131-555-0101",false),
        Contact("John Doe5", "john@example10.com", "132-555-0101",false),
        Contact("John Doe6", "john@example11.com", "133-555-0101",false),
        Contact("Jill Big", "JillBig@example12.com", "134-555-0101",false),
        Contact("John Doe7", "john@example13.com", "135-555-0101",false),
        Contact("John Wells", "johnWells@example14.com", "136-555-0101",false),
        Contact("John Doe8", "johnDoe8@example15.com", "137-555-0101",false),
        Contact("Larry Doe", "Larry@example16.com", "138-555-0101",false),
        Contact("John Doe9", "john@example17.com", "139-555-0101",false),
        Contact("Jake Calories", "JakeCalories@example18.com", "140-555-0101",false),
        Contact("John Doe10", "john@example19.com", "141-555-0101",false),
        Contact("Johnny John", "johnny@example20.com", "142-555-0101",false),
        Contact("John Doe", "john@example21.com", "143-555-0101",false),
        Contact("John Doe", "john@example22.com", "144-555-0101",false),
        Contact("Three Left", "Left@example23.com", "145-555-0101",false),
        Contact("John Doe", "john@example24.com", "146-555-0101",false),
        Contact("This Bottom", "john@example25.com", "147-555-0101",false),
    )
    ContactList(contacts)
}

@Composable
fun ContactList(contacts: List<Contact>) {
    var selectedContact by rememberSaveable { mutableStateOf<Contact?>(null) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 50.dp)
    ) {
        Text("Contact List", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))
        Text(text = if(selectedContact!= null){"Selected: ${selectedContact!!.name}"} else { "No contact selected"}, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(bottom = 24.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(contacts) { contact ->
                ContactItem(contact,contact.isSelectedContact,{ selectedContact?.isSelectedContact = false; selectedContact = contact; contact.isSelectedContact = true})
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            enabled = selectedContact != null,
            onClick = {selectedContact?.isSelectedContact = false ;selectedContact = null}
        ) {
            Text("Clear Selection")
        }
    }
}

@Composable
fun ContactItem(
    contact: Contact,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
                .padding(16.dp)
                ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        shape = CircleShape, color = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.secondary
                        }
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text("${contact.name[0]}${contact.name.substringAfter(" ")[0]}", color = Color.White)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column (
                modifier = Modifier.weight(1f)
            ) {
               Text(contact.name,fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = if(isSelected){MaterialTheme.colorScheme.primary}else{MaterialTheme.colorScheme.secondary})
               Text(contact.email,fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, color = if(isSelected){MaterialTheme.colorScheme.primary}else{MaterialTheme.colorScheme.secondary})
               Text(contact.phone,fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall, color = if(isSelected){MaterialTheme.colorScheme.primary}else{MaterialTheme.colorScheme.secondary})

            }
            if (isSelected){
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

        }
    }
}

// Data class for contact information
data class Contact(
    val name: String,
    val email: String,
    val phone: String,
    var isSelectedContact: Boolean
)

/**
 * Preview for Android Studio's design view.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ContactListAppPreview() {
    ContactListApp()
}
