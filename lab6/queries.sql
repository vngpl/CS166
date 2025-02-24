-- 1
SELECT    S.sname, COUNT(*) AS totalParts
FROM      suppliers S
JOIN      catalog C ON S.sid = C.sid
GROUP BY  S.sname;

-- 2
SELECT    S.sname, COUNT(*) AS totalParts
FROM      suppliers S
JOIN      catalog C ON S.sid = C.sid
GROUP BY  S.sname
HAVING    COUNT(*) >= 3;

-- 3
SELECT    S.sname, COUNT(*) AS totalParts
FROM      suppliers S
JOIN      catalog C ON S.sid = C.sid
JOIN      parts P ON P.pid = C.pid
WHERE     P.color = 'Green'
GROUP BY  S.sname
HAVING    COUNT(DISTINCT P.color) = 1;

-- 4
SELECT    S.sname, MAX(C.cost) AS mostExpensive
FROM      suppliers S
JOIN      catalog C ON S.sid = C.sid
JOIN      parts P ON P.pid = C.pid
WHERE     P.color IN ('Green', 'Red')
GROUP BY  S.sname
HAVING    COUNT(DISTINCT P.color) = 2;