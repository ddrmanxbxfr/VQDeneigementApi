package com.apes.app.servlet

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import com.apes.app.data.AvisData
import com.apes.app.models.AvisDeneigement
import com.datastax.driver.core.Session
import com.apes.app.tools.NumberHandler
import org.scalatra.ScalatraServlet
import scala.collection.mutable.MutableList

class AvisServlet(aDatabaseSessions: Session) extends ScalatraServlet with JacksonJsonSupport {
  private final val BASEAPI_PATH = "/"
  private final val APIPATH_WITH_ID = BASEAPI_PATH + ":id"
  private final val mAvisData = new AvisData(aDatabaseSessions)
  // Sets up automatic case class to JSON output serialization, required by
  // the JValueResult trait.
  protected implicit val jsonFormats: Formats = DefaultFormats
  
    // Before every action runs, set the content type to be in JSON format.
  before() {
    contentType = formats("json")
  }
  
  get(BASEAPI_PATH) {  //  This will get all the arrondissements !s                        
     mAvisData.all
  }
  
  get(APIPATH_WITH_ID) {  //  <= this is a route matcher
    // this is an action
    // this action would show the article which has the specified :id
        val id = NumberHandler.ParseInt(params("id"))
        var lstToReturn: MutableList[AvisDeneigement] = MutableList()
        mAvisData.all.map {x => if (x.CodeArrondissement.contains(id)) {
         lstToReturn.+=(x)
        }
        }
        
        lstToReturn
  }

  post(BASEAPI_PATH) {
    // submit/create an article
     mAvisData.SaveToDB(parsedBody.extract[AvisDeneigement])
  }

  delete(APIPATH_WITH_ID) {
    // delete the article with the specified :id
  }
}