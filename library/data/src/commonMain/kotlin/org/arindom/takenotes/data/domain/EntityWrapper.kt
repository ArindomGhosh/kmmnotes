package org.arindom.takenotes.data.domain

sealed interface DomainResponse<T> {
  data class Success<T>(val `data`: T) : DomainResponse<T>
  data class Fail(val throwable: Throwable) : DomainResponse<Nothing>
}