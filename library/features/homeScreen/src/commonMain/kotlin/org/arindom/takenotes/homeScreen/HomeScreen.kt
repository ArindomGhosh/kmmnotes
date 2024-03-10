package org.arindom.takenotes.homeScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableCollection
import kotlinx.collections.immutable.ImmutableList
import org.arindom.takenotes.homeScreen.presentation.NotesViewModel
import org.arindom.takenotes.navigation.TakeNoteScreenProvider
import org.arindom.takenotes.ui.ErrorHandler
import org.arindom.takenotes.ui.IndeterminateCircularIndicator
import org.arindom.takenotes.ui.utils.Callback
import org.arindom.takenotes.ui.utils.VoidCallback
import org.arindom.takenotes.voyager.koin.getScreenModel

class HomeScreen : Screen {

  @Composable
  override fun Content() {
    LifecycleEffect(onStarted = {
      Napier.i("HomeScreen LifeCycle Started")
    }, onDisposed = {
      Napier.i("HomeScreen LifeCycle Disposed")
    })
    val homeScreenViewModel = getScreenModel<HomeScreenViewModel>()
    val navigator = LocalNavigator.currentOrThrow
    Scaffold(
      modifier = Modifier.padding(16.dp).fillMaxSize(),
      topBar = {
        AnimatedTopBar(state = homeScreenViewModel.topBarModeState.value,
          searchQuery = homeScreenViewModel.query.value,
          noteTitles = homeScreenViewModel.searchListState.value,
          selectedCount = homeScreenViewModel.selectedCountState.value,
          deleteClickListener = homeScreenViewModel::deleteNotes,
          onDisableDeleteMode = { homeScreenViewModel.setTopBarState(TopBarMode.SEARCH) },
          searchQueryChange = homeScreenViewModel::onQueryChanged,
          onBackNavClicked = { homeScreenViewModel.setTopBarState(TopBarMode.SEARCH) })
      },
      floatingActionButton = {
        IconButton(colors = IconButtonDefaults.filledIconButtonColors(), onClick = {
          Napier.i("Create new Notes")
        }) {
          Icon(Icons.Filled.Add, contentDescription = null)
        }
      },
    ) {
      Box(
        modifier = Modifier.fillMaxSize().padding(it)
      ) {
        Column(
          modifier = Modifier.padding(top = 6.dp).fillMaxSize()
        ) {
          val homeScreenState by homeScreenViewModel.state.collectAsState()
          when (val currentState = homeScreenState) {
            is HomeScreenState.Error -> ErrorHandler(
              header = "Error", content = "Error content"
            )

            HomeScreenState.Init -> Box(modifier = Modifier.fillMaxSize()) { }
            is HomeScreenState.Loaded -> {
              ShowNotes(noteViewModelList = currentState.data,
                onLabelClick = { label ->
                  homeScreenViewModel.filterByLabel(TopBarMode.Label(label))
                },
                onLongPressListener = { homeScreenViewModel.setTopBarState(TopBarMode.DELETE) },
                selectToDelete = homeScreenViewModel::addToDeleteList,
                unSelectToDelete = homeScreenViewModel::removeFromDeleteList,
                onNotesSelected = { id ->
                  navigator.push(ScreenRegistry.get(TakeNoteScreenProvider.CreateNotes(id)))
                })
            }

            HomeScreenState.Loading -> {
              Box(modifier = Modifier.fillMaxSize()) {
                IndeterminateCircularIndicator(
                  modifier = Modifier.align(Alignment.Center), isLoading = true
                )
              }
            }
          }
        }
      }
    }
  }

  @Composable
  private fun AnimatedTopBar(
    state: TopBarMode,
    searchQuery: String,
    noteTitles: ImmutableList<String>,
    selectedCount: Int,
    deleteClickListener: VoidCallback,
    onDisableDeleteMode: VoidCallback,
    searchQueryChange: Callback<String>,
    onBackNavClicked: VoidCallback = {}
  ) {
    when (state) {
      TopBarMode.DELETE -> {
        AnimatedVisibility(
          visible = true, enter = slideInVertically() + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
          ), exit = slideOutVertically() + fadeOut()
        ) {
          DeleteModeTopBar(
            selectedCount = selectedCount,
            deleteNotes = deleteClickListener,
            disableDeleteMode = onDisableDeleteMode
          )
        }
      }

      is TopBarMode.Label -> {
        AnimatedVisibility(
          visible = true, enter = slideInVertically() + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
          ), exit = slideOutVertically() + fadeOut()
        ) {
          LabelAppBar(
            title = state.label, onBackNavClicked = onBackNavClicked
          )
        }
      }

      TopBarMode.SEARCH -> {
        AnimatedVisibility(
          visible = true, enter = slideInVertically() + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
          ), exit = slideOutVertically() + fadeOut()
        ) {
          SearchNotesBar(
            query = searchQuery,
            noteTitles = noteTitles,
            onSearchQueryChanges = searchQueryChange,
          )
        }
      }
    }
  }

  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  private fun LabelAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackNavClicked: VoidCallback,

    ) {
    TopAppBar(modifier = modifier, colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant,
      titleContentColor = MaterialTheme.colorScheme.primary,
    ), navigationIcon = {
      IconButton({ onBackNavClicked() }) {
        Icon(Icons.Default.ArrowBack, contentDescription = null)
      }
    }, title = {
      Text(title)
    })
  }

  @Composable
  internal fun ShowNotes(
    modifier: Modifier = Modifier,
    noteViewModelList: ImmutableCollection<NotesViewModel>,
    onLabelClick: Callback<String>,
    onLongPressListener: Callback<NotesViewModel>,
    selectToDelete: Callback<Int>,
    unSelectToDelete: Callback<Int>,
    onNotesSelected: Callback<Long>
  ) {
    LazyColumn(
      modifier = modifier.background(Color.White), verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
      itemsIndexed(noteViewModelList.toList()) { pos, noteViewModel ->
        NotesCard(notesViewModel = noteViewModel,
          onLabelClick = onLabelClick,
          onLongPressListener = onLongPressListener,
          selectToDelete = { selectToDelete(pos) },
          unSelectToDelete = { unSelectToDelete(pos) },
          onNotesSelected = {
            onNotesSelected(noteViewModel.notes.id)
          })
      }
    }
  }

  @OptIn(ExperimentalFoundationApi::class)
  @Composable
  internal fun NotesCard(
    modifier: Modifier = Modifier,
    notesViewModel: NotesViewModel,
    onLabelClick: Callback<String>,
    onLongPressListener: Callback<NotesViewModel>,
    selectToDelete: VoidCallback,
    unSelectToDelete: VoidCallback,
    onNotesSelected: VoidCallback
  ) {
    val isSelectedState =
      remember(notesViewModel) { mutableStateOf(notesViewModel.isMarkedForDelete) }
    Card(
      modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp))
        .combinedClickable(onClick = {
          if (notesViewModel.deleteMode) {
            isSelectedState.value = !isSelectedState.value
            notesViewModel.isMarkedForDelete = isSelectedState.value
            if (isSelectedState.value) {
              selectToDelete()
            } else {
              unSelectToDelete()
            }
          } else {
            onNotesSelected()
          }
        }, onLongClick = {
          onLongPressListener(notesViewModel)
        }), elevation = CardDefaults.cardElevation(
        defaultElevation = 8.dp
      ), colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
      )
    ) {
      Box(modifier = Modifier.padding(8.dp)) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth().height(50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = notesViewModel.notes.header,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = notesViewModel.notes.createdAt,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.tertiary
              )
            }
            Column(
              modifier = Modifier.fillMaxHeight(),
              verticalArrangement = Arrangement.SpaceBetween,
              horizontalAlignment = Alignment.End
            ) {
              if (!notesViewModel.notes.label.isNullOrEmpty()) {
                LabelCard(
                  label = notesViewModel.notes.label, onClick = onLabelClick
                )
              }
              if (notesViewModel.deleteMode) RadioButton(selected = isSelectedState.value,
                onClick = {
                  isSelectedState.value = !isSelectedState.value
                  notesViewModel.isMarkedForDelete = isSelectedState.value
                  if (isSelectedState.value) {
                    selectToDelete()
                  } else {
                    unSelectToDelete()
                  }
                })
            }

          }
          Text(notesViewModel.notes.content, style = MaterialTheme.typography.bodyMedium)
        }
      }
    }
  }

  @Composable
  fun LabelCard(
    modifier: Modifier = Modifier, label: String, onClick: Callback<String>
  ) {
    OutlinedCard(
      modifier = modifier.clickable { onClick(label) }, colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      ), border = BorderStroke(1.dp, Color.Black)
    ) {
      Text(
        label,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier.padding(horizontal = 6.dp),
        textAlign = TextAlign.Center
      )
    }
  }

  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  fun DeleteModeTopBar(
    modifier: Modifier = Modifier,
    selectedCount: Int,
    deleteNotes: VoidCallback,
    disableDeleteMode: VoidCallback
  ) {

    CenterAlignedTopAppBar(modifier = modifier, colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant,
      titleContentColor = MaterialTheme.colorScheme.primary,
    ), title = {
      if (selectedCount > 0) Text("$selectedCount item selected")
    }, navigationIcon = {
      IconButton(onClick = {
        disableDeleteMode()
      }) {
        Icon(Icons.Default.Clear, contentDescription = null)
      }
    }, actions = {
      IconButton(onClick = {
        deleteNotes()
      }) {
        Icon(Icons.Outlined.Delete, contentDescription = null)
      }
    })
  }

  @Composable
  @OptIn(ExperimentalMaterial3Api::class)
  private fun SearchNotesBar(
    query: String, noteTitles: ImmutableList<String>, onSearchQueryChanges: Callback<String>
  ) {
    val searchQuery = remember(query) { mutableStateOf(query) }
    val shownOptions = remember(searchQuery.value, noteTitles) {
      if (searchQuery.value.isNotBlank()) {
        noteTitles.filter { it.contains(searchQuery.value, ignoreCase = true) }
      } else noteTitles
    }
    var isSearchbarActive by remember {
      mutableStateOf(false)
    }
    SearchBar(modifier = Modifier.fillMaxWidth(), query = searchQuery.value, onQueryChange = {
      searchQuery.value = it
    }, active = isSearchbarActive, onActiveChange = {
      isSearchbarActive = it
    }, onSearch = {
      onSearchQueryChanges(searchQuery.value)
      isSearchbarActive = false
    }, placeholder = {
      Text("Search your Notes")
    }, leadingIcon = {
      Icon(Icons.Default.Search, contentDescription = null)
    }, trailingIcon = {
      if (searchQuery.value.isNotBlank()) {
        IconButton(
          onClick = {
            if (!isSearchbarActive) {
              onSearchQueryChanges("")
            } else {
              searchQuery.value = ""
            }
            Napier.i("clear clicked")
          },
        ) {
          Icon(Icons.Default.Clear, contentDescription = null)
        }
      }
    }) {
      shownOptions.forEach { title ->
        ListItem(
          headlineContent = { Text(title) },
          leadingContent = { Icon(Icons.Outlined.CheckCircle, contentDescription = null) },
          modifier = Modifier.clickable {
            onSearchQueryChanges(title)
            isSearchbarActive = false
          }.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
        )
      }
    }
  }
}