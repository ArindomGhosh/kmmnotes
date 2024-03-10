package org.arindom.takenotes.database.dao

import app.cash.sqldelight.TransacterImpl
import kotlinx.coroutines.flow.flow

sealed interface TransactionResult {
  data object Succeed : TransactionResult
  data object Failed : TransactionResult

}

inline fun TransacterImpl.dataTransaction(crossinline body: () -> Unit): TransactionResult {
  lateinit var transactionResult: TransactionResult
  transaction {
    afterCommit {
      transactionResult = TransactionResult.Succeed
    }
    afterRollback {
      transactionResult = TransactionResult.Failed
    }
    body()
  }
  return transactionResult
}

inline fun TransactionResult.asFlow() = flow {
  emit(this@asFlow)
}
