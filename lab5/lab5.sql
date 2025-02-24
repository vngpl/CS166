-- 1
SELECT  P.pid
FROM    parts P
JOIN    catalog C ON P.pid = C.pid
WHERE   cost < 10;

-- 2
SELECT  P.pname
FROM    parts P
JOIN    catalog C ON P.pid = C.pid
WHERE   cost < 10;

-- 3
SELECT  S.address
FROM    suppliers S
JOIN    catalog C ON S.sid = C.sid
JOIN    parts P   ON P.pid = C.pid
WHERE   P.pname = 'Fire Hydrant Cap';

-- 4
SELECT  S.sname
FROM    suppliers S
JOIN    catalog C ON S.sid = C.sid
JOIN    parts P   ON P.pid = C.pid
WHERE   P.color = 'Green';

-- 5
SELECT    S.sname, P.pname
FROM      suppliers S
JOIN      catalog C ON S.sid = C.sid
JOIN      parts P   ON P.pid = C.pid
ORDER BY  S.sname;