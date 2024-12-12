-- Create additional databases, users, or run initial migrations
CREATE DATABASE ecommerce_product_cataloge;
CREATE USER firas WITH PASSWORD 'Firas123@';
GRANT ALL PRIVILEGES ON DATABASE ecommerce_product_cataloge TO firas;