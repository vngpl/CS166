DROP TABLE IF EXISTS availableAt;
DROP TABLE IF EXISTS contains;
DROP TABLE IF EXISTS Order_;
DROP TABLE IF EXISTS Item;
DROP TABLE IF EXISTS Store;
DROP TABLE IF EXISTS User_;

CREATE TABLE User_ (
	role CHAR(8) NOT NULL,
	login CHAR(16) NOT NULL,
	password CHAR(16) NOT NULL,
	phoneNumber CHAR(10) NOT NULL,
	favoriteItem CHAR(16),
	address CHAR(32) NOT NULL,
	PRIMARY KEY(login)
);

CREATE TABLE Store (
	storeID INTEGER NOT NULL,
	city CHAR(32) NOT NULL,
	state CHAR(2) NOT NULL,
	reviewScore REAL,
	address CHAR(32) NOT NULL,
	isOpen BOOLEAN NOT NULL,
	PRIMARY KEY (storeID)
);

CREATE TABLE Order_ (
	orderID INTEGER NOT NULL,
	orderTimeStamp TIMESTAMP NOT NULL,
	orderStatus CHAR(16) NOT NULL,
	totalPrice INTEGER NOT NULL,
	login CHAR(16) NOT NULL,
	storeID INTEGER NOT NULL,
	PRIMARY KEY(orderID),
	FOREIGN KEY(login) REFERENCES User_(login) ON DELETE NO ACTION,
	FOREIGN KEY(storeID) REFERENCES Store(storeID) ON DELETE NO ACTION
);

CREATE TABLE Item (
	ingredients CHAR(1024),
	itemName CHAR(32) NOT NULL,
	price INTEGER NOT NULL,
	description CHAR(64),
	imageURL CHAR(128),
	type CHAR(32) NOT NULL,
	PRIMARY KEY(itemName)
);

CREATE TABLE contains (
	itemName CHAR(32) NOT NULL,
	orderID INTEGER NOT NULL,
	FOREIGN KEY(itemName) REFERENCES Item(itemName) ON DELETE NO ACTION,
	FOREIGN KEY(orderID) REFERENCES Order_(orderID) ON DELETE NO ACTION
);

CREATE TABLE availableAt (
	itemName CHAR(32) NOT NULL,
	storeID INTEGER NOT NULL,
	FOREIGN KEY(itemName) REFERENCES Item(itemName) ON DELETE NO ACTION,
	FOREIGN KEY(storeID) REFERENCES Store(storeID) ON DELETE NO ACTION
);
