#! /bin/bash
#! /bin/bash
echo "creating db named ... "$USER"_lab7_DB"
cs166_createdb $USER'_lab7_DB'
cs166_db_status

#export DB_NAME=$USER"_DB"
#echo "creating db named ... "$DB_NAME
#createdb -h localhost -p $PGPORT $DB_NAME
#pg_ctl status

cp -a *.dat $PGDATA/

#cs166_psql $USER'_lab7_DB' < create_tables.sql
