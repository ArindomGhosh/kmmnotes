package org.arindom.takenotes.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ErrorHandler(
  header: String,
  content: String,
  onDismissListener: () -> Unit = {},
  onConfirmationListener: () -> Unit = {}
) {
  AlertDialog(
    icon = {
      Icon(Icons.Default.Warning, contentDescription = null)
    },
    title = {
      Text(
        header,
        style = MaterialTheme.typography.headlineMedium
      )
    },
    text = {
      Text(
        content,
        style = MaterialTheme.typography.bodyMedium
      )
    },
    confirmButton = {
      TextButton(onClick = onConfirmationListener) {
        Text(
          "Ok",
          style = MaterialTheme.typography.displayMedium
        )
      }
    },
    dismissButton = {
      TextButton(onClick = {
        onDismissListener()
      }) {
        Text(
          "Cancel",
          style = MaterialTheme.typography.displayMedium
        )
      }
    },
    onDismissRequest = onDismissListener,
  )
}