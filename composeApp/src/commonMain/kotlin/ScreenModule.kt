import cafe.adriel.voyager.core.registry.screenModule
import org.arindom.takenotes.createNotes.CreateNoteScreen
import org.arindom.takenotes.homeScreen.HomeScreen
import org.arindom.takenotes.navigation.TakeNoteScreenProvider

val featureTakeNotesScreenModule = screenModule {
  register<TakeNoteScreenProvider.Home> {
    HomeScreen()
  }
  register<TakeNoteScreenProvider.CreateNotes> {
    CreateNoteScreen(it.id)
  }
}