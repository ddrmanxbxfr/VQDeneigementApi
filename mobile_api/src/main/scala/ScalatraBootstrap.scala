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