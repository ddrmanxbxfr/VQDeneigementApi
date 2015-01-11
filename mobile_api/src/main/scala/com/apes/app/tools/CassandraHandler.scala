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