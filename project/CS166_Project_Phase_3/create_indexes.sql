DROP INDEX IF EXISTS USER_LOGIN_INDEX;
DROP INDEX IF EXISTS ITEM_TYPE_INDEX;
DROP INDEX IF EXISTS ITEM_PRICE_INDEX;

-- Speeds up the login process.
CREATE INDEX USER_LOGIN_INDEX
ON Users
(login, password);

-- Speeds up evaluation when the customer filter by item type.
CREATE INDEX ITEM_TYPE_INDEX
ON Items
(typeOfItem);

-- Speeds up evaluation when the customer filters by price range.
CREATE INDEX ITEM_PRICE_INDEX
ON Items
-- This is a range query, so we use a B-tree.
USING BTREE
(price);