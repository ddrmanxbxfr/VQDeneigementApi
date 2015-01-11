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

import com.apes.app._
import com.apes.app.servlet._
import com.apes.app.settings._
import org.scalatra._
import javax.servlet.ServletContext
import _root_.akka.actor.ActorSystem
import com.datastax.driver.core.Cluster
import com.apes.app.tools.CassandraHandler
import com.datastax.driver.core.Session
class ScalatraBootstrap extends LifeCycle {
  val akkaSystem = ActorSystem("AvisSystem")
  val dbConnection: Cluster = CassandraHandler.Connect
  val dbSession: Session = CassandraHandler.GetSession(dbConnection)
  override def init(context: ServletContext) {
    context.mount(new AvisServlet(dbSession), AppRoutes.AVIS_BASE_ROUTE)
    context.mount(new ArrondissementServlet(dbSession), AppRoutes.ARRONDISSEMENT_BASE_ROUTE)
    context.mount(new MyScalatraServlet, "/")
  }
  
  override def destroy(context: ServletContext){
    akkaSystem.shutdown()
    dbSession.close()
    CassandraHandler.CloseConnection(dbConnection)
  }
}