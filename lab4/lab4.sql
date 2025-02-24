-- 1
SELECT Title, Year
FROM BOOKS;

-- 2
SELECT *
FROM STUDENTS
WHERE Major = "CS";

-- 3
SELECT *
FROM STUDENTS S, borrows B
WHERE S.StID = B.StID;

-- 4
SELECT *
FROM BOOKS
WHERE Publisher = "McGraw-Hill" AND Year < 1990

-- 5
SELECT AName
FROM AUTHORS
WHERE Address = "Davis";

-- 6
SELECT StName
FROM STUDENTS
WHERE Age > 30 AND Major <> "CS"; -- NOTE: "<>"" == "!="" == "NOT EQUAL TO"

-- 7
SELECT AName AS Name
FROM AUTHORS;

-- 8
SELECT S.StName
FROM STUDENTS S, borrows B
WHERE S.StID = B.StID
AND S.Major = "CS";

-- 9
SELECT B.Title
FROM BOOKS B, has-written HW
WHERE B.DocID = HW.DocID
AND HW.AName = "Jones";

-- 10
SELECT B.Title
FROM BOOKS B, has-written HW
WHERE B.DocID = HW.DocID
AND HW.AName = "Jones"
AND B.DocID NOT IN (
    SELECT DocID,
    FROM describes
    WHERE Keyword = "database"
);

-- 11
SELECT StName
FROM STUDENTS
WHERE Age = (
    SELECT MIN(Age)
    FROM STUDENTS
);

-- 12
SELECT Title
FROM BOOKS
WHERE Title = (
    SELECT MIN(Year)
    FROM BOOKS
);