package com.apes.app.tools

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session
import com.apes.app.settings.AppDBSettings
object CassandraHandler {
  def Connect: Cluster = {
    return Cluster.builder().addContactPoint(AppDBSettings.DBAddress).build();
  }
  
  def GetSession(aClusterObject: Cluster): Session = {
    return aClusterObject.connect(AppDBSettings.DBName)
  }
  
  def CloseConnection(aClusterObject: Cluster): Void = {
    aClusterObject.close();
    return null;
  }
}