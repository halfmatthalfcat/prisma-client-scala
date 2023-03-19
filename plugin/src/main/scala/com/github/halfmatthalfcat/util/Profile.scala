package com.github.halfmatthalfcat.util

object Profile {
  def fn[T](fn: => T): (Long, T) = {
    val start = System.nanoTime()
    val result = fn
    val end = System.nanoTime()
    (end - start, result)
  }
}
