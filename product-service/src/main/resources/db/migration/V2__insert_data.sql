

INSERT INTO category (id, name, is_active, created_date, last_modified_date)
VALUES
    (nextval('category_seq'), 'Electronics', TRUE, CURRENT_TIMESTAMP, NULL),
    (nextval('category_seq'), 'Books', TRUE, CURRENT_TIMESTAMP, NULL),
    (nextval('category_seq'), 'Clothing', TRUE, CURRENT_TIMESTAMP, NULL),
    (nextval('category_seq'), 'Toys', TRUE, CURRENT_TIMESTAMP, NULL),
    (nextval('category_seq'), 'Sports', TRUE, CURRENT_TIMESTAMP, NULL),
    (nextval('category_seq'), 'Health', TRUE, CURRENT_TIMESTAMP, NULL),
    (nextval('category_seq'), 'Home Appliances', TRUE, CURRENT_TIMESTAMP, NULL),
    (nextval('category_seq'), 'Furniture', TRUE, CURRENT_TIMESTAMP, NULL),
    (nextval('category_seq'), 'Automobiles', TRUE, CURRENT_TIMESTAMP, NULL),
    (nextval('category_seq'), 'Groceries', TRUE, CURRENT_TIMESTAMP, NULL);