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

var fs = require('fs');
var request = require('request');
var obj;
var API_URL = "http://localhost:8080/arrondissement";

fs.readFile('ARROND.JSON', 'utf8', function (err, data) {
  if (err) throw err;
  obj = JSON.parse(data);
  for (var i = 0; i < obj.Arrondissements.Arrondissement.length; i = i + 1) {
    console.log('yp');
    var objToWorkOn = obj.Arrondissements.Arrondissement[i];
    var objForApi = "{" +
      "\"code\": " +parseInt(objToWorkOn.Code) + "," +
      "\"Nom\": \"" + objToWorkOn.Nom + "\"," +
      "\"Abreviation\": \"" + objToWorkOn.Abreviation+ "\"," +
      "\"Superficie\": " + parseFloat(objToWorkOn.Superficie)+ "," +
      "\"Perimetre\": " + parseFloat(objToWorkOn.Perimetre) + "," +
      "\"Geometrie\": \""  + "\"}";

    request.post(
      {
      url:API_URL,
      body: objForApi,
      headers : { 'content-type': 'Text/Json' }
    }
      ,
    function (error, response, body) {
      if (!error && response.statusCode == 200) {
        console.log(response.statusCode)
      } else {
        console.log('error in api insert' + response.body); 
        console.log(objForApi);
      }
    }
    );
  }
});
