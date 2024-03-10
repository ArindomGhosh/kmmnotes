package org.arindom.takenotes

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import featureTakeNotesScreenModule
import org.arindom.takenotes.homeScreen.HomeScreen
import org.arindom.takenotes.ui.TakeNotesTheme
import org.koin.compose.KoinContext

@Composable
fun App() {
  KoinContext {
    ScreenRegistry {
      featureTakeNotesScreenModule()
    }
    TakeNotesTheme {
      Navigator(
        screen = HomeScreen(),
      )
    }
  }
}