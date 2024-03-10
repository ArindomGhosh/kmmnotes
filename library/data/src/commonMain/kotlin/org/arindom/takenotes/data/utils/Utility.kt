package org.arindom.takenotes.data.utils

inline fun <T,R> T.map(action:(T)->R):R{
  return action(this)
}