package org.arindom.takenotes.homeScreen.presentation

import org.arindom.takenotes.homeScreen.domain.entities.Notes

internal data class NotesViewModel(
  val notes: Notes,
  val deleteMode:Boolean = false,
  var isMarkedForDelete: Boolean = false
)
