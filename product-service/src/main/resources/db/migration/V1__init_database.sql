CREATE SEQUENCE IF NOT EXISTS category_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS product_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS product_images_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE category
(
    id          INTEGER NOT NULL,
    name        VARCHAR(255),
    description VARCHAR(255),
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE product
(
    id                 INTEGER NOT NULL,
    name               VARCHAR(255),
    description        VARCHAR(255),
    available_quantity DOUBLE PRECISION,
    price              DECIMAL,
    is_active          BOOLEAN,
    is_sold            BOOLEAN,
    category_id        INTEGER,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

-- Create Product Images Table (for @ElementCollection)
CREATE TABLE product_images (
                                product_id INT,
                                image_path VARCHAR(255),
                                FOREIGN KEY (product_id) REFERENCES product(id)
);

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);