var FeedParser = require('feedparser');
var request = require('request');
var LastVQApiFetchDate;
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

module.exports.FetchDataFromAPI = function (res) {
var reqRss = request(VQ_RSS_URL),
		feedParser = new FeedParser();

		reqRss.on('error', function(error) {
			console.log(error);
		})
		.on('response', function(resRss) {
			var stream = this;
			console.log(res.statusCode);
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

				var VQRssItem = new VilleQuebecNeige();
				VQRssItem.pubdate = item.pubdate;
				VQRssItem.date = item.date;
				VQRssItem.title = item.title;
				VQRssItem.description = item.description;

				VQRssItem.save(function(err) {
					if (err)
						console.log(err);

					console.log('VQRssItem created');
				})

				console.log(item.title);
				console.log(item.description);
				console.log(item.date);
				console.log(item.pubdate);
			}
		});
		
		res.json({ message: 'This is path for all avis...' });
}