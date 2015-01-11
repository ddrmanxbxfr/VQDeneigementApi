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

package com.apes.app.servlet
import org.scalatra.ScalatraServlet
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import com.apes.app.data.ArrondissementData
import com.apes.app.models.Arrondissement
import com.datastax.driver.core.Session
import com.apes.app.tools.NumberHandler


class ArrondissementServlet(aDatabaseSessions: Session) extends ScalatraServlet with JacksonJsonSupport {
  private final val BASEAPI_PATH = "/"
  private final val APIPATH_WITH_ID = BASEAPI_PATH + ":id"
  private final val mArrondissementData = new ArrondissementData(aDatabaseSessions)
  // Sets up automatic case class to JSON output serialization, required by
  // the JValueResult trait.
  protected implicit val jsonFormats: Formats = DefaultFormats
  
    // Before every action runs, set the content type to be in JSON format.
  before() {
    contentType = formats("json")
  }
  
  get(BASEAPI_PATH) {  //  This will get all the arrondissements !s                        
     mArrondissementData.all
  }
  
  get(APIPATH_WITH_ID) {  //  <= this is a route matcher
    // this is an action
    // this action would show the article which has the specified :id
        val id = NumberHandler.ParseInt(params("id"))
        mArrondissementData.all.find (_.code == id) match {
        case Some(b) => b
        case None => halt(404, "{\"error\": \"ObjectNotFound\"}")
        }
  }

  post(BASEAPI_PATH) {
    // submit/create an article
     mArrondissementData.SaveToDB(parsedBody.extract[Arrondissement])
  }

  delete(APIPATH_WITH_ID) {
    // delete the article with the specified :id
    mArrondissementData.DeleteFromDb(params("id").toInt)
  }
}