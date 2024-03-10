package org.arindom.takenotes.ui

import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IndeterminateCircularIndicator(
  modifier: Modifier = Modifier,
  isLoading: Boolean
) {
  val loading by remember { mutableStateOf(isLoading) }
  if (!loading) return
  CircularProgressIndicator(
    modifier = modifier.width(64.dp),
    color = MaterialTheme.colorScheme.secondary,
    trackColor = MaterialTheme.colorScheme.surfaceVariant,
  )
}