CREATE TABLE "bank_transactions" (
	"id"	INTEGER,
	"user_from_id"	INTEGER,
	"user_to_id"	INTEGER,
	"amount"	INTEGER,
	"time"	INTEGER,
	"comment"	TEXT,
	PRIMARY KEY("id")
);

CREATE TABLE "items" (
	"id"	INTEGER,
	"owner_id"	INTEGER,
	"name"	TEXT,
	"info"	INTEGER,
	PRIMARY KEY("id")
);

CREATE TABLE "items_transactions" (
	"id"	INTEGER,
	"item_id"	INTEGER,
	"bank_transaction_id"	INTEGER,
	PRIMARY KEY("id")
);

CREATE TABLE "users" (
	"id"	INTEGER,
	"name"	TEXT,
	"money"	INTEGER,
	"code"	TEXT,
	PRIMARY KEY("id")
);
