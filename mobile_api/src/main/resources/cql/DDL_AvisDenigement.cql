use apesdeneigement;

CREATE TABLE avisdeneigement (CodeArrondissement list<int>, 
		Title varchar, 
		Description varchar, 
		Pubdate varchar, 
		Date varchar,
		PRIMARY KEY (Title, Description, Pubdate, Date));