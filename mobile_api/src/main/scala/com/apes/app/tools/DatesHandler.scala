package com.apes.app.tools
import java.util.Date
import java.text.SimpleDateFormat

object DatesHandler {
  private final val DATE_FORMAT = "YYYY-MM-DD HH:MM:SS"
  def FormatDate(aDateToFormat: Date): String = {
     val dateFormatter: SimpleDateFormat = new SimpleDateFormat(DATE_FORMAT)
     return dateFormatter.format(aDateToFormat)
  }
  
  def ConvertToDate(aDateStr: String): Date = {
    return new Date(aDateStr)
  }
}