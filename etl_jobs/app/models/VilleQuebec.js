var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var VilleQuebecNeigeSchema   = new Schema({
	title: { type: String  , unique : true, dropDups: true },
	description:{ type: String  , unique : true , dropDups: true },
	pubdate: { type: Date  , unique : true, dropDups: true },
	date: { type: Date  , unique : true,  dropDups: true },
});

module.exports = mongoose.model('VilleQuebecNeige', VilleQuebecNeigeSchema);