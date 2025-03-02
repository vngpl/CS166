-- 1. Count how many parts in NYC have more than 70 parts on_hand
SELECT COUNT(*)
FROM part_nyc PNYC
WHERE PNYC.on_hand > 70;

-- 2. Count how many total parts on_hand, in both NYC and SFO, are Red
SELECT SUM(totalParts) AS totalRedParts_NYC_SFO
FROM (
  SELECT SUM(PNYC.on_hand) AS totalParts
  FROM part_nyc PNYC
  JOIN color C ON PNYC.color = C.color_id
  WHERE C.color_name = 'Red'
    UNION ALL
  SELECT SUM(PSFO.on_hand) AS totalParts
  FROM part_sfo PSFO
  JOIN color C ON PSFO.color = C.color_id
  WHERE C.color_name = 'Red'
) AS combinedResult;

-- 3. List all the suppliers that have more total on_hand parts in NYC than they do in SFO.
SELECT NYC.supplier_name
FROM (
  SELECT S.supplier_name, SUM(PNYC.on_hand) AS total_nyc
  FROM part_nyc PNYC
  JOIN supplier S ON PNYC.supplier = S.supplier_id
  GROUP BY S.supplier_name
) NYC
JOIN (
  SELECT S.supplier_name, SUM(PSFO.on_hand) AS total_sfo
  FROM part_sfo PSFO
  JOIN supplier S ON PSFO.supplier = S.supplier_id
  GROUP BY S.supplier_name
) SFO
ON NYC.supplier_name = SFO.supplier_name
WHERE NYC.total_nyc > SFO.total_sfo;

-- 4. List all suppliers that supply parts in NYC that aren’t supplied by anyone in SFO.
SELECT DISTINCT S.supplier_name
FROM supplier S
JOIN part_nyc PNYC ON S.supplier_id = PNYC.supplier
WHERE PNYC.part_number NOT IN (
  SELECT PSFO.part_number
  FROM part_sfo PSFO
);

-- 5. Update all of the NYC on_hand values to on_hand - 10.
UPDATE part_nyc
SET on_hand = on_hand - 10
WHERE on_hand >= 10;

-- 6. Delete all parts from NYC which have less than 30 parts on_hand.
DELETE
FROM part_nyc
WHERE on_hand < 30;