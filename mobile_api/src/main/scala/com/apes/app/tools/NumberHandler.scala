package com.apes.app.tools

object NumberHandler {
  def ParseInt(aStrToParse: String): Integer = {
    try {
      aStrToParse.toInt
    } catch {
      case e: Throwable => -1
    }
  }
}