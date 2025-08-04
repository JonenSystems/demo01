-- H2データベース用初期データ

-- 商品データの投入
INSERT INTO products (id, name, description, price, category, stock_quantity, image_path, created_at, updated_at) VALUES
(1, 'ノートパソコン', '高性能なビジネス用ノートパソコン', 120000.00, 'エレクトロニクス', 10, '/images/7f861d2a-b24d-40df-ad2b-ac491e945bb4.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'スマートフォン', '最新のスマートフォン', 80000.00, 'エレクトロニクス', 15, '/images/ab4491db-f147-43ba-af84-0280e3acd035.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'ワイヤレスイヤホン', 'ノイズキャンセリング機能付き', 25000.00, 'エレクトロニクス', 20, '/images/7127e2ba-0633-4449-b0d1-b7b1d7cc2002.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'デジタルカメラ', '高画質なデジタルカメラ', 45000.00, 'エレクトロニクス', 8, '/images/d1d42a58-4118-4920-a49e-8a3c2c5b0d6b.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'タブレット', '軽量で持ち運びやすいタブレット', 60000.00, 'エレクトロニクス', 12, '/images/df9fa236-7820-4e3a-bd25-1db40b2403c9.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ユーザーデータの投入
-- 管理者ユーザー: admin / admin123
-- 一般ユーザー: user1 / password
INSERT INTO users (id, username, password, role, enabled, created_at, updated_at) VALUES
(1, 'admin', '$2a$10$yET5njgbb6SB6975h/MGrOrJXhRZdoiRIsReY8yzZdXBxgVOG4KjS', 'ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'user1', '$2a$10$L7E90/tnFa4SS6yhZb1uROjYGXEM9jnlGGQ5P2fp4dvb/bsvre546', 'USER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 顧客データの投入
INSERT INTO customers (id, name, email, phone, address, created_at, updated_at) VALUES
(1, '田中太郎', 'tanaka@example.com', '090-1234-5678', '東京都渋谷区1-1-1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, '佐藤花子', 'sato@example.com', '090-9876-5432', '大阪府大阪市2-2-2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, '鈴木一郎', 'suzuki@example.com', '090-5555-5555', '愛知県名古屋市3-3-3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 注文データの投入
INSERT INTO orders (id, order_number, customer_id, total_amount, status, created_at, updated_at) VALUES
(1, 'ORD-2024-001', 1, 145000.00, 'PENDING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'ORD-2024-002', 2, 85000.00, 'PREPARING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'ORD-2024-003', 3, 105000.00, 'SHIPPED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 注文詳細データの投入
INSERT INTO order_details (id, order_id, product_id, quantity, unit_price, subtotal, created_at, updated_at) VALUES
(1, 1, 1, 1, 120000.00, 120000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, 3, 1, 25000.00, 25000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 2, 2, 1, 80000.00, 80000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 2, 4, 1, 45000.00, 45000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 3, 5, 1, 60000.00, 60000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 3, 3, 2, 25000.00, 50000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); 