echo "Query time without indexes"
cat <(echo '\timing') queries.sql | psql -h localhost -p $PGPORT $USER'_homework' | grep Time | awk -F "Time" '{print "Query" FNR $2;}'

psql -h localhost -p $PGPORT $USER'_homework' < create-indexes.sql > /dev/null

echo "Query time with indexes"
cat <(echo '\timing') queries.sql |psql -h localhost -p $PGPORT $USER'_homework' | grep Time | awk -F "Time" '{print "Query" FNR $2;}'