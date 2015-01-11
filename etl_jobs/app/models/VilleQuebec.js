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

var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var VilleQuebecNeigeSchema   = new Schema({
	title: { type: String  , unique : true, dropDups: true },
	description:{ type: String  , unique : true , dropDups: true },
	pubdate: { type: Date  , unique : true, dropDups: true },
	date: { type: Date  , unique : true,  dropDups: true },
});

module.exports = mongoose.model('VilleQuebecNeige', VilleQuebecNeigeSchema);