// server.js

// BASE SETUP
// =============================================================================

// call the packages we need
var schedule = require('node-schedule');
var express    = require('express'); 		// call express
var app        = express(); 				// define our app using express
var bodyParser = require('body-parser');
var VQ_CONSTS = require('./app/VQ_CONSTS.js'); // VQ Related specific logic


function doRssLogic() {
    if (VQ_CONSTS.ShouldFetchFromAPI() === true) {
    VQ_CONSTS.FetchDataFromAPI();
    } else {
        console.log('Skipping exec ! ');
    }
}

var rule = new schedule.RecurrenceRule();
rule.minute = [15, 30, 45];

// configure app to use bodyParser()
// this will let us get the data from a POST
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

console.log('Magic happens ');
doRssLogic();


var j = schedule.scheduleJob(rule, function(){
    doRssLogic();
});
