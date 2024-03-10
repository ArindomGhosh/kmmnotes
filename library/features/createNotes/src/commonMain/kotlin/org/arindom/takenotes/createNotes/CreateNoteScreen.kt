package org.arindom.takenotes.createNotes

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.SnackbarResult.Dismissed
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.map
import org.arindom.takenotes.createNotes.SideEffect.ERROR
import org.arindom.takenotes.createNotes.SideEffect.UPDATED
import org.arindom.takenotes.createNotes.domain.entities.AlertMessage
import org.arindom.takenotes.createNotes.domain.entities.AlertType
import org.arindom.takenotes.createNotes.domain.entities.MutableNote
import org.arindom.takenotes.ui.IndeterminateCircularIndicator
import org.arindom.takenotes.voyager.koin.getScreenModel

class SnackbarVis(private val alertMessage: AlertMessage) : SnackbarVisuals {
  override val actionLabel: String
    get() = when (alertMessage.alertType) {
      AlertType.INFO -> "Ok"
      AlertType.ERR -> "Error"
    }
  override val duration: SnackbarDuration
    get() = SnackbarDuration.Indefinite
  override val message: String
    get() = alertMessage.message
  override val withDismissAction: Boolean
    get() = false

  val isError: Boolean = alertMessage.alertType == AlertType.ERR
}

class CreateNoteScreen(
  private val noteId: Long = 0L
) : Screen {
  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val createNotesViewModel = getScreenModel<CreateNotesViewModel>()
    createNotesViewModel.getNote(noteId)
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(this.key, snackbarHostState, createNotesViewModel.sideEffects) {
      createNotesViewModel
        .sideEffects
        .map {
          when (it) {
            UPDATED -> {
              navigator.pop()
              SnackbarVis(AlertMessage("updated", AlertType.INFO))
            }

            ERROR -> SnackbarVis(AlertMessage("Err", AlertType.ERR))
          }
        }
        .collect {
          val result = snackbarHostState.showSnackbar(it)
          when (result) {
            Dismissed -> {
              Napier.i("Snakbar Dismissed")
            }

            ActionPerformed -> {
              Napier.i("action has been performed")
            }
          }
        }
    }
    Scaffold(snackbarHost = {
      SnackbarHost(snackbarHostState) { data ->
        val isError = (data.visuals as? SnackbarVis)?.isError ?: false
        val buttonColor = if (isError) {
          ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.error
          )
        } else {
          ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.inversePrimary
          )
        }
        Snackbar(modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.secondary)
          .padding(12.dp),
          action = {
            TextButton(
              onClick = { if (isError) data.dismiss() else data.performAction() },
              colors = buttonColor
            ) { Text(data.visuals.actionLabel ?: "") }
          }) {
          Text(
            data.visuals.message
          )
        }
      }
    },
      topBar = {
        TopAppBar(
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            titleContentColor = MaterialTheme.colorScheme.primary,
          ),
          navigationIcon = {
            IconButton({ navigator.pop() }) {
              Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
          },
          title = {

          }
        )
      }) {
      Column(
        modifier = Modifier.padding(it)
      ) {
        val state by createNotesViewModel.state.collectAsState()
        when (val currentState = state) {
          CreateNotesStates.CreateState -> {

          }

          is CreateNotesStates.EditStates -> {
            EditNotes(
              mutableNote = currentState.mutableNotes,
              onSave = createNotesViewModel::updateNote
            )
          }

          CreateNotesStates.Init -> Box(modifier = Modifier.fillMaxSize()) {
            IndeterminateCircularIndicator(
              modifier = Modifier.align(Alignment.Center), isLoading = true
            )
          }
        }
      }
    }
  }

  @Composable
  fun NewNotes(

  ) {

  }

  @Composable
  private fun EditNotes(
    modifier: Modifier = Modifier,
    mutableNote: MutableNote,
    onSave: (MutableNote) -> Unit
  ) {
    var header by remember { mutableStateOf(mutableNote.header) }
    var content by remember { mutableStateOf(mutableNote.content) }
    var label by remember { mutableStateOf(mutableNote.label) }

    Column(modifier = modifier.padding(20.dp)) {
      TextField(
        header,
        onValueChange = {
          header = it
        },
        label = {
          Text("Title")
        },
        maxLines = 1,
        textStyle = MaterialTheme.typography.headlineSmall,
      )
      Text(
        text = mutableNote.createdAt,
        style = MaterialTheme.typography.bodySmall,
      )
      TextField(
        content,
        onValueChange = {
          content = it
        },
        label = {
          Text("Content")
        },
        maxLines = 1,
        textStyle = MaterialTheme.typography.headlineSmall,
      )
      Button(onClick = {
        onSave(
          mutableNote.copy(
            header = header,
            content = content
          )
        )
      }) {
        Text("Save")
      }
    }
  }
}