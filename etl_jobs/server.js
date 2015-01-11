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
