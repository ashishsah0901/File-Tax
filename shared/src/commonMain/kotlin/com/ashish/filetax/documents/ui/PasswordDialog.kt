package com.ashish.filetax.documents.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.ashish.filetax.documents.PasswordPrompt

@Composable
internal fun PasswordDialog(
    prompt: PasswordPrompt,
    onSubmit: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var password by remember(prompt.fileName) { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("This PDF is locked") },
        text = {
            Column {
                Text(
                    text = "${prompt.fileName} needs a password. AIS and Form 16 open with your PAN in lower case followed by your date of birth as DDMMYYYY, so abcde1234f01011990.",
                    style = MaterialTheme.typography.bodyMedium
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    isError = prompt.wrongPassword,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.padding(top = 16.dp)
                )
                if (prompt.wrongPassword) {
                    Text(
                        text = "That password did not open the file.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onSubmit(password) }, enabled = password.isNotBlank()) {
                Text("Open")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}