DROP INDEX IF EXISTS supplier_supplier_id;
DROP INDEX IF EXISTS supplier_supplier_name;

DROP INDEX IF EXISTS part_nyc_on_hand;
DROP INDEX IF EXISTS part_nyc_supplier;
DROP INDEX IF EXISTS part_nyc_part_number;
DROP INDEX IF EXISTS part_nyc_color;

DROP INDEX IF EXISTS part_sfo_on_hand;
DROP INDEX IF EXISTS part_sfo_supplier;
DROP INDEX IF EXISTS part_sfo_part_number;
DROP INDEX IF EXISTS part_sfo_color;

DROP INDEX IF EXISTS color_color_id;
DROP INDEX IF EXISTS color_color_name;

CREATE INDEX supplier_supplier_id
ON supplier
USING BTREE
(supplier_id);

CREATE INDEX supplier_supplier_name
ON supplier
USING BTREE
(supplier_name);

CREATE INDEX part_nyc_on_hand
ON part_nyc
USING BTREE
(on_hand);

CREATE INDEX part_nyc_supplier
ON part_nyc
USING BTREE
(supplier);

CREATE INDEX part_nyc_part_number
ON part_nyc
USING BTREE
(part_number);

CREATE INDEX part_nyc_color
ON part_nyc
USING BTREE
(color);

CREATE INDEX part_sfo_on_hand
ON part_sfo
USING BTREE
(on_hand);

CREATE INDEX part_sfo_supplier
ON part_sfo
USING BTREE
(supplier);

CREATE INDEX part_sfo_part_number
ON part_sfo
USING BTREE
(part_number);

CREATE INDEX part_sfo_color
ON part_sfo
USING BTREE
(color);

CREATE INDEX color_color_id
ON color
USING BTREE
(color_id);

CREATE INDEX color_color_name
ON color
USING BTREE
(color_name);