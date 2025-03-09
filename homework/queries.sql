-- 1. List the first and last names of all the actors who played in the movie 'Officer 444'. [~13 rows expected]
SELECT A.fname, A.lname
FROM ACTOR A
JOIN CASTS C ON C.pid = A.id
JOIN MOVIE M ON M.id = C.mid
WHERE M.name = 'Officer 444';

-- 2. List all the directors who directed a 'Film-Noir' movie in a leap year. (You need to check that the genre is 'Film-Noir' and simply assume that every year divisible by 4 is a leap year.) Your query should return director name, the movie name, and the year. [~113 rows]
SELECT D.fname, D.lname
FROM DIRECTORS D
JOIN MOVIE_DIRECTORS MD ON MD.did = D.id
JOIN MOVIE M ON M.id = MD.mid
JOIN GENRE G ON G.mid = M.id
WHERE G.genre = 'Film-Noir' AND M.year % 4 = 0;

-- 3. List all the actors who acted in a film before 1900 and also in a film after 2000. (That is: < 1900 and > 2000.) [~48 rows]
SELECT DISTINCT A.id, A.fname, A.lname
FROM ACTOR A
JOIN CASTS C1 ON C1.pid = A.id
JOIN MOVIE M1 ON M1.id = C1.mid AND M1.year < 1900
JOIN CASTS C2 ON C2.pid = A.id
JOIN MOVIE M2 ON M2.id = C2.mid AND M2.year > 2000;

-- SELECT A.fname, A.lname, M.name, M.year
-- FROM ACTOR A
-- JOIN CASTS C ON C.pid = A.id
-- JOIN MOVIE M ON M.id = C.mid
-- WHERE A.id IN (
--   SELECT A.id
--   FROM ACTOR A
--   JOIN CASTS C ON C.pid = A.id
--   JOIN MOVIE M ON M.id = C.mid
--   WHERE M.year < 1900
--   INTERSECT
--   SELECT A.id
--   FROM ACTOR A
--   JOIN CASTS C ON C.pid = A.id
--   JOIN MOVIE M ON M.id = C.mid
--   WHERE M.year > 2000
-- )
-- ORDER BY A.lname, M.year;

-- Some actors appear in films before 1900 and after 2000 because they are credited in documentaries/historical films. A majority of those credited are historical figures and leaders.

-- -- 4. List all directors who directed 500 movies or more, in descending order of the number of movies they directed. Return the directors' names and the number of movies each of them directed. [~47 rows]
SELECT D.fname, D.lname, MD.movie_count
FROM DIRECTORS D
JOIN (
  SELECT MD.did, COUNT(DISTINCT MD.mid) AS movie_count
  FROM MOVIE_DIRECTORS MD
  GROUP BY MD.did
  HAVING COUNT(DISTINCT MD.mid) >= 500
) MD ON D.id = MD.did
ORDER BY MD.movie_count DESC;

-- 5. We want to find actors that played five or more roles in the same movie during the year 2010. Notice that CASTS may have occasional duplicates, but we are not interested in these: we want actors that had five or more distinct roles in the same movie in the year 2010. Write a query that returns the actors' names, the movie name, and the number of distinct roles that they played in that movie (which will be ≥ 5). [~24 rows]
SELECT DISTINCT A.fname, A.lname, M.name, COUNT(DISTINCT C.role) AS role_count
FROM ACTOR A
JOIN CASTS C ON C.pid = A.id
JOIN MOVIE M ON M.id = C.mid
WHERE M.year = 2010
GROUP BY A.id, M.id
HAVING COUNT(DISTINCT C.role) >= 5;