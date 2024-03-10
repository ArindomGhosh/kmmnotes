package org.arindom.takenotes.createNotes

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.arindom.takenotes.createNotes.domain.entities.MutableNote
import org.arindom.takenotes.createNotes.domain.usecases.CreateNotes
import org.arindom.takenotes.createNotes.domain.usecases.OpenNoteForId
import org.arindom.takenotes.createNotes.domain.usecases.UpdateNotes
import org.arindom.takenotes.data.domain.DomainResponse

internal sealed interface CreateNotesStates {
  data object Init : CreateNotesStates

  data class EditStates(val noteId: Long, val mutableNotes: MutableNote) : CreateNotesStates

  data object CreateState : CreateNotesStates
}

enum class SideEffect {
  UPDATED,
  ERROR
}

internal class CreateNotesViewModel(
  private val createNotes: CreateNotes,
  private val openNoteForId: OpenNoteForId,
  private val updateNotes: UpdateNotes
) : StateScreenModel<CreateNotesStates>(CreateNotesStates.Init) {

  private val _sideEffects = MutableSharedFlow<SideEffect>(replay = 0)

  val sideEffects = _sideEffects.asSharedFlow()

  fun getNote(id: Long) {
    if (id == 0L) {
      mutableState.update {
        CreateNotesStates.CreateState
      }
    } else {
      screenModelScope.launch {
        mutableState.update {
          CreateNotesStates.EditStates(id, openNoteForId(id))
        }
      }
    }
  }

  fun updateNote(mutableNotes: MutableNote) {
    screenModelScope.launch {
      when (val updatedNote = updateNotes(mutableNotes)) {
        is DomainResponse.Fail -> _sideEffects.emit(
          SideEffect.ERROR
        )

        is DomainResponse.Success -> {
          _sideEffects.emit(SideEffect.UPDATED)
          mutableState.update {
            CreateNotesStates.EditStates(updatedNote.data.notesId, updatedNote.data)
          }
        }
      }
    }
  }

  fun createNote(mutableNote: MutableNote) {
    screenModelScope.launch {
      when (val createdNote = createNotes(mutableNote)) {
        is DomainResponse.Fail -> _sideEffects.emit(
         SideEffect.ERROR
        )

        is DomainResponse.Success -> {
          _sideEffects.emit(SideEffect.UPDATED)
        }
      }
    }
  }
}