INSERT INTO product (id, name, description, available_quantity, price, is_active, is_sold, category_id)
VALUES (
           3,
           'Earbuds',
           'Ecouteurs Bluetooth Generic M10 ',
    500,
    29900,
    true,
    false,
    151
);


INSERT INTO product_images (product_id, image_path)
VALUES
(3, '/images/products/3/earbuds2.jpg');
