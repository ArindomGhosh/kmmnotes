package org.arindom.takenotes.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed interface TakeNoteScreenProvider : ScreenProvider {
  data object Home : TakeNoteScreenProvider
  data class CreateNotes(val id: Long=0L) : TakeNoteScreenProvider
}