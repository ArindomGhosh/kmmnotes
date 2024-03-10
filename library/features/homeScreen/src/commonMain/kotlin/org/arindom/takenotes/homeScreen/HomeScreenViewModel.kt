package org.arindom.takenotes.homeScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.arindom.takenotes.data.domain.DomainResponse
import org.arindom.takenotes.homeScreen.domain.usecases.DeleteNotesUseCase
import org.arindom.takenotes.homeScreen.domain.usecases.FilterByLabel
import org.arindom.takenotes.homeScreen.domain.usecases.FilterUseCase
import org.arindom.takenotes.homeScreen.domain.usecases.LoadUserNotes
import org.arindom.takenotes.homeScreen.presentation.NotesViewModel

internal sealed interface HomeScreenState {
  data object Init : HomeScreenState
  data object Loading : HomeScreenState
  data class Loaded(
    val query: String = "",
    val data: ImmutableList<NotesViewModel>,
  ) : HomeScreenState

  data class Error(val throwable: Throwable) : HomeScreenState

}

sealed interface TopBarMode {
  data object SEARCH : TopBarMode
  data object DELETE : TopBarMode
  data class Label(
    val label: String
  ) : TopBarMode
}

internal class HomeScreenViewModel(
  private val loadUserNotes: LoadUserNotes,
  private val filterUseCase: FilterUseCase,
  private val deleteNotesUseCase: DeleteNotesUseCase,
  private val filterByLabel: FilterByLabel

) : StateScreenModel<HomeScreenState>(HomeScreenState.Init) {

  private var _searchListState = mutableStateOf<ImmutableList<String>>(persistentListOf())
  private val _topBaseModeState = mutableStateOf<TopBarMode>(TopBarMode.SEARCH)
  private val _deleteNoteList = mutableSetOf<Int>()
  private val _selectedCountState = mutableStateOf<Int>(0)
  val selectedCountState: State<Int>
    get() = _selectedCountState

  val topBarModeState: State<TopBarMode>
    get() = _topBaseModeState

  init {
    mutableState.update {
      HomeScreenState.Loading
    }
    loadAllNotes()
  }

  val searchListState: State<ImmutableList<String>>
    get() = _searchListState

  private var _query = mutableStateOf("")
  val query: State<String>
    get() = _query

  fun onQueryChanged(query: String) {
    _query.value = query
    mutableState.update {
      HomeScreenState.Loading
    }
    screenModelScope.launch {
      if (query.isNotBlank())
        loadFilteredNotes(query)
      else
        loadAllNotes()
    }
  }

  fun addToDeleteList(pos: Int) {
    _deleteNoteList.add(pos)
    _selectedCountState.value = _deleteNoteList.size
  }

  fun removeFromDeleteList(pos: Int) {
    _deleteNoteList.remove(pos)
    _selectedCountState.value = _deleteNoteList.size
  }

  fun setTopBarState(topBarMode: TopBarMode) {
    _topBaseModeState.value = topBarMode
    handleTopBarMode(topBarMode)
  }

  fun deleteNotes() {
    Napier.i("delete list size: ${_deleteNoteList.size}")
    val currentState = mutableState.value
    if (currentState is HomeScreenState.Loaded) {
      screenModelScope.launch {
        val noteIds = _deleteNoteList.map {
          currentState.data[it].notes.id
        }
        when (deleteNotesUseCase(noteIds)) {
          is DomainResponse.Fail -> {
            // todo handle side effects
          }

          is DomainResponse.Success -> {
            // todo happy path
          }
        }
        _topBaseModeState.value = TopBarMode.SEARCH
        _deleteNoteList.clear()
        _selectedCountState.value = _deleteNoteList.size
        loadAllNotes()
      }
    }
  }

  fun filterByLabel(label: TopBarMode.Label) {
    setTopBarState(label)
  }

  private fun handleTopBarMode(topBarMode: TopBarMode) {
    when (topBarMode) {
      TopBarMode.SEARCH -> {
        _deleteNoteList.clear()
        if (mutableState.value is HomeScreenState.Loaded) {
          loadAllNotes()
        }
      }

      TopBarMode.DELETE -> {
        loadAllNotes()
      }

      is TopBarMode.Label -> {
        screenModelScope.launch {
          mutableState.update {
            HomeScreenState.Loaded(
              data = filterByLabel.invoke(topBarMode.label).toImmutableList()
            )
          }
        }
      }
    }
  }


  private fun loadAllNotes() {
    screenModelScope.launch {
      loadUserNotes(deleteMode = _topBaseModeState.value == TopBarMode.DELETE)
        .collectLatest { notesVmList ->
          mutableState.update {
            HomeScreenState.Loaded(
              data = notesVmList
            )
          }
          _searchListState.value = notesVmList.map { it.notes.header }
            .toImmutableList()
        }
    }
  }

  private suspend fun loadFilteredNotes(query: String) {
    val filteredList = filterUseCase.invoke(query).toImmutableList()
    mutableState.update {
      HomeScreenState.Loaded(
        query = query, data = filteredList
      )
    }
  }
}