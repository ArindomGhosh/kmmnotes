package org.arindom.takenotes.data.exceptions

sealed class TransactionException(override val message: String) : Throwable(message) {
  data object InsertOperationException : TransactionException("unable to insert")
  data object UpdateOperationException : TransactionException("unable to update")

  data object ReadOperationException : TransactionException("Unable to read")

  data object DeleteOperationException : TransactionException("unable to delete")
}