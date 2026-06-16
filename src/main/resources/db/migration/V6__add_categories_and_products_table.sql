-- Create categories table
CREATE TABLE categories(
    id              TINYINT PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(255)
);

-- Create products table
CREATE TABLE products(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(255),
    price           DECIMAL(10,2),
    category_id     TINYINT,
    CONSTRAINT fk_category
        FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
);