-- 1. Insert 4 Categories
INSERT INTO categories (name) VALUES
    ('Electronics'),
    ('Apparel'),
    ('Home & Kitchen'),
    ('Fitness & Outdoors');

-- 2. Insert 15 Products with PHP prices (linked to categories 1 to 4)
INSERT INTO products (name, description, price, category_id) VALUES
-- Electronics (category_id: 1)
('Wireless Noise-Canceling Headphones', 'Over-ear Bluetooth headphones with active noise cancellation and 40-hour battery life.', 4500.00, 1),
('Mechanical Gaming Keyboard', 'RGB backlit mechanical keyboard with tactile blue switches and detachable cable.', 2500.00, 1),
('Ergonomic Wireless Mouse', 'Rechargeable vertical mouse designed to reduce wrist strain during long working hours.', 1200.00, 1),
('1080p Streaming Webcam', 'Full HD webcam with built-in dual microphones and privacy cover for video calls.', 1800.00, 1),

-- Apparel (category_id: 2)
('Classic Denim Jacket', 'Unisex regular-fit denim jacket made from 100% durable cotton.', 1999.00, 2),
('Organic Cotton Hooded Sweatshirt', 'Ultra-soft fleece-lined hoodie with adjustable drawstrings and kangaroo pocket.', 1450.00, 2),
('Breathable Running Shoes', 'Lightweight mesh athletic sneakers with shock-absorbing foam soles.', 3500.00, 2),
('Waterproof Windbreaker', 'Packable lightweight rain jacket with zippered pockets and adjustable hood.', 2200.00, 2),

-- Home & Kitchen (category_id: 3)
('Stainless Steel Electric Kettle', '1.7-liter rapid-boil water heater with automatic shut-off safety feature.', 999.00, 3),
('Programmable 6-Quart Slow Cooker', 'Digital countertop crockpot with warm settings and a dishwasher-safe ceramic pot.', 3200.00, 3),
('Non-Stick Ceramic Frying Pan', '10-inch eco-friendly scratch-resistant pan compatible with induction stoves.', 1150.00, 3),
('Digital Kitchen Food Scale', 'High-precision slim glass scale with tare function, measuring up to 5kg.', 450.00, 3),

-- Fitness & Outdoors (category_id: 4)
('Adjustable Dumbbell Set', 'Pair of heavy-duty selectorized dumbbells adjustable from 2.5kg to 24kg.', 8500.00, 4),
('High-Density Yoga Mat', '6mm thick non-slip eco-TPE exercise mat with alignment lines and carrying strap.', 850.00, 4),
('Insulated Stainless Steel Water Bottle', 'Double-walled vacuum thermos that keeps drinks cold for 24 hours or hot for 12.', 750.00, 4);
