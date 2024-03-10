package org.arindom.takenotes.data

enum class NoteType(val arg: Int) {
  SINGLE(0),
  LIST(1);
  companion object {
    fun resolveNoteType(arg: Int): NoteType {
      return values().firstOrNull { it.arg == arg }
        ?: throw IllegalArgumentException("No matching NoteType for value $arg")
    }
  }
}

fun Int.mapToEntityNoteType() = when (this) {
  0 -> NoteType.SINGLE
  1 -> NoteType.LIST
  else -> error("NoteType for $this not implemented")
}