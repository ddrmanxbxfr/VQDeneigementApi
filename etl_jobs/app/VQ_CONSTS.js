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

var FeedParser = require('feedparser');
var request = require('request');
var LastVQApiFetchDate;
var API_URL = "http://localhost:8080/avis";
var VilleQuebecNeige = require('./models/VilleQuebec.js');
var VQ_RSS_URL = 'http://www.ville.quebec.qc.ca/Rss/rss.aspx?f=den';
var MAX_API_TIME = 50000; // This is in MS

module.exports.callbackVQRss = function (error, meta, articles){
  if (error) console.error(error);
  else {
    console.log('Feed info');
    console.log('%s - %s - %s', meta.title, meta.link, meta.xmlurl);
    console.log('Articles');
    articles.forEach(function (article){
      console.log('%s - %s (%s)', article.date, article.title, article.link);
    });
  }
}

module.exports.ShouldFetchFromAPI = function () {
	var curDate = new Date();
	
	if (LastVQApiFetchDate === undefined || curDate.getTime() - LastVQApiFetchDate.getTime() > MAX_API_TIME) {
		LastVQApiFetchDate = new Date();
		return true;
	} else {
		return false;
	}
}

module.exports.FetchDataFromAPI = function () {
var reqRss = request(VQ_RSS_URL),
		feedParser = new FeedParser();

		reqRss.on('error', function(error) {
			console.log(error);
		})
		.on('response', function(resRss) {
			var stream = this;
			console.log(resRss.statusCode);
			if (resRss.statusCode !== 200) return this.emit('error', new Error('Bad status code'));
			stream.pipe(feedParser);
		});

		feedParser.on('error', function(error) {
			console.log(error);
		})

		.on('readable', function() {
			// This is where the action is!
			var stream = this
			, meta = this.meta // **NOTE** the "meta" is always available in the context of the feedparser instance
			, item;

			while (item = stream.read()) {
                    var itemToSend = "{" +
                        "\"Title\": \"" + item.title.replace(/"/g, "").replace(/(\r\n|\n|\r)/gm,"") + "\"," +
                        "\"Description\": \"" + item.description.replace(/"/g, "").replace(/(\r\n|\n|\r)/gm,"") + "\"," +
                        "\"Pubdate\": \"" + item.pubdate + "\"," +
                        "\"Date\": \"" + item.date + "\"," +
                        "\"CodeArrondissement\": [] }";
                
                request.post(
                    {
                      url:API_URL,
                    body:itemToSend,
                    headers : { 'content-type': 'Text/Json' }
                    }
                    ,
                    function (error, response, body) {
                    if (!error && response.statusCode == 200) {
                        console.log(response.statusCode)
                    } else {
                        console.log('error in api insert' + response.body); 
                        console.log(itemToSend);
                    }
                    }
                    );
                
			}
		});
}