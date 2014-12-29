// server.js

// BASE SETUP
// =============================================================================

// call the packages we need
var express    = require('express'); 		// call express
var app        = express(); 				// define our app using express
var bodyParser = require('body-parser');
var VQ_CONSTS = require('./app/VQ_CONSTS.js'); // VQ Related specific logic


// configure app to use bodyParser()
// this will let us get the data from a POST
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

console.log('Magic happens ');


if (VQ_CONSTS.ShouldFetchFromAPI() === true) {
VQ_CONSTS.FetchDataFromAPI();
} else {
    console.log('Skipping exec ! ');
}