package org.arindom.takenotes.ui.utils

typealias VoidCallback = () -> Unit
typealias VoidSuspendedCallback = suspend () -> Unit

typealias Callback<T> = (T) -> Unit
typealias SuspendCallback<T> = suspend (T) -> Unit