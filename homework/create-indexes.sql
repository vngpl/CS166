DROP INDEX IF EXISTS idx_movie_name;
DROP INDEX IF EXISTS idx_casts_pid;
DROP INDEX IF EXISTS idx_casts_mid;
DROP INDEX IF EXISTS idx_movie_year;
DROP INDEX IF EXISTS idx_genre_mid;
DROP INDEX IF EXISTS idx_genre_genre;
DROP INDEX IF EXISTS idx_movie_directors_did;
DROP INDEX IF EXISTS idx_movie_directors_mid;
DROP INDEX IF EXISTS idx_casts_pid_mid;
DROP INDEX IF EXISTS idx_movie_directors_did_mid;

-- Index on CASTS.pid to speed up joins between ACTOR and CASTS in queries 1, 3, and 5
CREATE INDEX idx_casts_pid ON CASTS (pid);

-- Index on CASTS.mid to speed up joins between CASTS and MOVIE in queries 1, 3, and 5
CREATE INDEX idx_casts_mid ON CASTS (mid);

-- B-tree index on MOVIE.name to speed up equality searches in query 1
CREATE INDEX idx_movie_name ON MOVIE (name);

-- Index on MOVIE_DIRECTORS.did to speed up joins between DIRECTORS and MOVIE_DIRECTORS in queries 2 and 4
CREATE INDEX idx_movie_directors_did ON MOVIE_DIRECTORS (did);

-- Index on MOVIE_DIRECTORS.mid to speed up joins between MOVIE and MOVIE_DIRECTORS in queries 2 and 4
CREATE INDEX idx_movie_directors_mid ON MOVIE_DIRECTORS (mid);

-- Index on GENRE.mid to speed up the join between MOVIE and GENRE in query 2
CREATE INDEX idx_genre_mid ON GENRE (mid);

-- B-tree index on GENRE.genre to speed up equality searches in query 2
CREATE INDEX idx_genre_genre ON GENRE (genre);

-- Index on MOVIE.year to optimize range-based queries in queries 2, 3, and 5
CREATE INDEX idx_movie_year ON MOVIE (year);

-- Index on CASTS (pid, mid) to speed up ACTOR-MOVIE lookups in queries 3 and 5
CREATE INDEX idx_casts_pid_mid ON CASTS (pid, mid);

-- Index on MOVIE_DIRECTORS (did, mid) to speed up DIRECTOR-MOVIE lookups in queries 2 and 4
CREATE INDEX idx_movie_directors_did_mid ON MOVIE_DIRECTORS (did, mid);