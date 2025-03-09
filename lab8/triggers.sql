DROP SEQUENCE IF EXISTS part_number_seq;
DROP TRIGGER IF EXISTS part_nyc_trigger ON part_nyc;

CREATE SEQUENCE part_number_seq START WITH 50000;

CREATE OR REPLACE FUNCTION increment_part_number_seq()
RETURNS TRIGGER AS
$BODY$
BEGIN
  NEW.part_number := nextval('part_number_seq');
  RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql;

CREATE TRIGGER part_nyc_trigger
BEFORE INSERT ON part_nyc
FOR EACH ROW
EXECUTE PROCEDURE increment_part_number_seq();