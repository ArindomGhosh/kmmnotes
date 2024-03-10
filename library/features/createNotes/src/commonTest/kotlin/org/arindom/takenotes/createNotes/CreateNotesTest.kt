package org.arindom.takenotes.createNotes

import io.mockative.Mock
import io.mockative.any
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.coVerify
import io.mockative.mock
import io.mockative.once
import kotlinx.coroutines.test.runTest
import org.arindom.takenotes.createNotes.domain.entities.usecases.MutableNote
import org.arindom.takenotes.createNotes.domain.usecases.CreateNotes
import org.arindom.takenotes.data.NoteType
import org.arindom.takenotes.data.domain.DomainResponse
import org.arindom.takenotes.data.exceptions.TransactionException
import org.arindom.takenotes.database.dao.INotesDao
import org.arindom.takenotes.database.dao.TransactionResult
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateNotesTest {

  @Mock
  val notesDao = mock(classOf<INotesDao>())

  @Test
  fun `test if CreateNotes return Success on valid response from database`() = runTest {
    val model = MutableNote(
      notesId = 0L,
      header = "title",
      content = "Content",
      createdAt = "2024-02-14",
      noteType = NoteType.SINGLE,
      isActive = true,
      label = null
    )
    coEvery {
      notesDao.insertNotes(any())
    }.returns(TransactionResult.Succeed)

    val sut = CreateNotes(notesDao)
    val result = sut(model)
    coVerify { notesDao.insertNotes(any()) }.wasInvoked(exactly = once)
    assertEquals(DomainResponse.Success(true),result)
  }

  @Test
  fun `test if CreateNotes return Fail on valid response from database`() = runTest {
    val model = MutableNote(
      notesId = 0L,
      header = "title",
      content = "Content",
      createdAt = "2024-02-14",
      noteType = NoteType.SINGLE,
      isActive = true,
      label = null
    )
    coEvery {
      notesDao.insertNotes(any())
    }.returns(TransactionResult.Failed)

    val sut = CreateNotes(notesDao)
    val result = sut(model)
    coVerify { notesDao.insertNotes(any()) }.wasInvoked(exactly = once)
    assertEquals(DomainResponse.Fail(TransactionException.InsertOperationException),result)
  }
}