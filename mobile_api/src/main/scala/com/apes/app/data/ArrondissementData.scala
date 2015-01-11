/*
 * This file is part of VQDeneigementApi.
 * VQDeneigementApi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * VQDeneigementApi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with VQDeneigementApi.  If not, see <http://www.gnu.org/licenses/>.
 * */

package com.apes.app.data
import com.apes.app.models.Arrondissement
import com.datastax.driver.core.Session
import com.datastax.driver.core.PreparedStatement
import com.datastax.driver.core.ResultSetFuture
import scala.collection.JavaConversions._
import com.datastax.driver.core.Row
import scala.collection.mutable.MutableList

class ArrondissementData(aDatabaseSession: Session) {
  private final val TABLE_NAME = "arrondissements"
  private final val SELECT_CQL_STATEMENT = "select Code,Nom,Abreviation,Superficie,Perimetre,Geometrie from " + TABLE_NAME
  private final val INSERT_CQL_STATEMENT = "INSERT INTO " + TABLE_NAME +
                                            "(Code,Nom,Abreviation,Superficie,Perimetre,Geometrie) VALUES (?, ?, ?, ?, ?, ?)"
  private final val DELETE_CQL_STATEMENT = "DELETE FROM " + TABLE_NAME + " WHERE code = ?"
  private final val mDeleteStatement = aDatabaseSession.prepare(DELETE_CQL_STATEMENT) 
  private final val mInsertStatement = aDatabaseSession.prepare(INSERT_CQL_STATEMENT)
  /**
   * Some fake arrondissement data so we can simulate retrievals.
   */                         
   def all: List[Arrondissement] = {
     val dataset = FetchFromDB 
     var lstToReturn: MutableList[Arrondissement] = MutableList()
     for (row <- dataset.getUninterruptibly()) {
         lstToReturn += Arrondissement(row.getInt("code"),
             row.getString("nom"),
             row.getString("abreviation"),
             row.getDouble("superficie"),
             row.getDouble("perimetre"),
             row.getString("geometrie")
                 )
      }
     return lstToReturn.toList
  }
  
   def SaveToDB(aArrondissement: Arrondissement): Void = {
    aDatabaseSession.executeAsync(mInsertStatement.bind(aArrondissement.code,
                                                      aArrondissement.Nom,
                                                      aArrondissement.Abreviation,
                                                      new java.lang.Double(aArrondissement.Superficie.doubleValue),
                                                      new java.lang.Double(aArrondissement.Perimetre.doubleValue),
                                                      aArrondissement.Geometrie))
    return null
  }
   
   def DeleteFromDb(aArrondissementCode: Integer): Void = {
     aDatabaseSession.executeAsync(mDeleteStatement.bind(aArrondissementCode))
     return null
   }
   
   private def FetchFromDB: ResultSetFuture = {
     aDatabaseSession.executeAsync(SELECT_CQL_STATEMENT)
   }
}