

INSERT INTO category (id, name, is_active, created_date, last_modified_date)
VALUES
    (nextval('category_seq'), 'Smartphones', TRUE, CURRENT_TIMESTAMP, NULL),
    (nextval('category_seq'), 'Ordinateurs', TRUE, CURRENT_TIMESTAMP, NULL),
    (nextval('category_seq'), 'Smartwatch', TRUE, CURRENT_TIMESTAMP, NULL),
    (nextval('category_seq'), 'Accessoires telephonie', TRUE, CURRENT_TIMESTAMP, NULL),
    (nextval('category_seq'), 'Accessoires de jeux', TRUE, CURRENT_TIMESTAMP, NULL),
    (nextval('category_seq'), 'Composants informatique', TRUE, CURRENT_TIMESTAMP, NULL),
    (nextval('category_seq'), 'Console de jeux', TRUE, CURRENT_TIMESTAMP, NULL),
    (nextval('category_seq'), 'videosurveillance', TRUE, CURRENT_TIMESTAMP, NULL),
    (nextval('category_seq'), 'Photos & Caméscopes', TRUE, CURRENT_TIMESTAMP, NULL),
    (nextval('category_seq'), 'Son numerique.', TRUE, CURRENT_TIMESTAMP, NULL);