/* Replace the location to where you saved the data files*/
COPY Users
FROM '/class/classes/vnagp002/CS166_Project_Phase_3/data/users.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Items
FROM '/class/classes/vnagp002/CS166_Project_Phase_3/data/items.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Store
FROM '/class/classes/vnagp002/CS166_Project_Phase_3/data/store.csv'
WITH DELIMITER ',' CSV HEADER;

COPY FoodOrder
FROM '/class/classes/vnagp002/CS166_Project_Phase_3/data/foodorder.csv'
WITH DELIMITER ',' CSV HEADER;

COPY ItemsInOrder
FROM '/class/classes/vnagp002/CS166_Project_Phase_3/data/itemsinorder.csv'
WITH DELIMITER ',' CSV HEADER;
