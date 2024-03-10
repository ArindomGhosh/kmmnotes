package org.arindom.takenotes.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkColorPalette = darkColorScheme(
  primary = Color.White,
  primaryContainer = Color.Gray,
  secondary = Color.Gray,
  onPrimary = Color.Black,
  onSecondary = Color.Black,
  background = Color.Black,
  surface = Color.Black
)

private val LightColorPalette = lightColorScheme(
  primary = Color.Black,
  primaryContainer = Color.Gray,
  secondary = Color.Gray,
  onPrimary = Color.White,
  onSecondary = Color.White,
  background = Color.White,
  surface = Color.White
)

@Composable
fun TakeNotesTheme(
  isDark: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = if (isDark) DarkColorPalette else LightColorPalette
  MaterialTheme(
    colorScheme = colorScheme,
    content = content
  )
}