package org.arindom.takenotes

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed interface TakeNoteScreenProvider : ScreenProvider {
  object Home : TakeNoteScreenProvider
  data class CreateNotes(val id: Long) : TakeNoteScreenProvider
}