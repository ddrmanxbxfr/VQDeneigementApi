package com.apes.app.data

import com.datastax.driver.core.Session
import com.datastax.driver.core.PreparedStatement
import com.datastax.driver.core.ResultSetFuture
import scala.collection.immutable.List
import com.datastax.driver.core.Row
import scala.collection.mutable.MutableList
import scala.collection.JavaConversions.asScalaIterator
import scala.collection.JavaConverters._
import java.util.Date
import com.apes.app.models.AvisDeneigement
import org.apache.commons.lang3.StringEscapeUtils
class AvisData (aDatabaseSession: Session) {
  private final val TABLE_NAME = "avisdeneigement"
  private final val COLUMNS = "CodeArrondissement, Title, Description, Pubdate, Date"
  private final val SELECT_CQL_STATEMENT = "select " + COLUMNS + " from " + TABLE_NAME
  private final val INSERT_CQL_STATEMENT = "INSERT INTO " + TABLE_NAME +
                                            "(" + COLUMNS + ") VALUES (?, ?, ?, ?, ?)"
  private final val DELETE_CQL_STATEMENT = "DELETE FROM " + TABLE_NAME + " WHERE Title = ? AND Pubdate = ?"
  private final val mDeleteStatement = aDatabaseSession.prepare(DELETE_CQL_STATEMENT) 
  private final val mInsertStatement = aDatabaseSession.prepare(INSERT_CQL_STATEMENT)
  private final val mArrondissementLookup = new ArrondissementData(aDatabaseSession)
  /**
   * Some fake arrondissement data so we can simulate retrievals.
   */
   def all: List[AvisDeneigement] = {
     val dataset = FetchFromDB 
     var lstToReturn: MutableList[AvisDeneigement] = MutableList()
     for (x <- asScalaIterator(dataset.getUninterruptibly().iterator())) {
             lstToReturn += AvisDeneigement(
             x.getString("Title"),
             x.getString("Description"),
             x.getString("Pubdate"),
             x.getString("Date"),
             x.getList("CodeArrondissement", classOf[Integer]).asScala.toList
                 ) 
     }
     return lstToReturn.toList
  }
  
   def SaveToDB(aAvisDeneigement: AvisDeneigement): Void = {
    aDatabaseSession.executeAsync(mInsertStatement.bind(List.concat(aAvisDeneigement.CodeArrondissement,
                                                        GetCodeArrondissementLookup(aAvisDeneigement)).asJava,
                                          aAvisDeneigement.Title,
                                          aAvisDeneigement.Description,
                                          aAvisDeneigement.Pubdate,
                                          aAvisDeneigement.Date
                                                      ))
    return null
  }
   
   def DeleteFromDb(aTitle: String, aPubDate: Date): Void = {
     aDatabaseSession.executeAsync(mDeleteStatement.bind(aTitle, aPubDate))
     return null
   }
   
   private def FetchFromDB: ResultSetFuture = {
     aDatabaseSession.executeAsync(SELECT_CQL_STATEMENT)
   }
   
   private def GetCodeArrondissementLookup(aAvisDeneigement: AvisDeneigement): List[Integer] = {
     var lstToReturn: MutableList[Integer] = MutableList()     
     mArrondissementLookup.all.map { x => if (StringEscapeUtils.unescapeHtml4(aAvisDeneigement.Description).contains(x.Nom)) {
       lstToReturn.+=(x.code)
     }                            
     }
     
    return lstToReturn.toList
   }
}