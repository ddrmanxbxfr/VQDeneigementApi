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
