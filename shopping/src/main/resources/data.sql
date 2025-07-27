-- 外部キー制約を一時無効化
SET REFERENTIAL_INTEGRITY FALSE;

-- 全テーブルのデータを削除（AUTO_INCREMENTもリセット）
TRUNCATE TABLE order_details;
TRUNCATE TABLE orders;
TRUNCATE TABLE customers;
TRUNCATE TABLE products;
TRUNCATE TABLE users;

-- 外部キー制約を再有効化
SET REFERENTIAL_INTEGRITY TRUE;

-- 商品データの投入
INSERT INTO products (id, name, description, price, category, stock_quantity, image_path, created_at, updated_at) VALUES
(1, 'ノートパソコン', '高性能なビジネス用ノートパソコン', 120000.00, 'エレクトロニクス', 10, '/images/laptop.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'スマートフォン', '最新のスマートフォン', 80000.00, 'エレクトロニクス', 15, '/images/smartphone.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'ワイヤレスイヤホン', 'ノイズキャンセリング機能付き', 25000.00, 'エレクトロニクス', 20, '/images/earphones.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'デジタルカメラ', '高画質なデジタルカメラ', 45000.00, 'エレクトロニクス', 8, '/images/camera.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'タブレット', '軽量で持ち運びやすいタブレット', 60000.00, 'エレクトロニクス', 12, '/images/tablet.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ユーザーデータの投入
-- 管理者ユーザー: admin / password
-- 一般ユーザー: user1 / password
INSERT INTO users (id, username, password, role, enabled, created_at, updated_at) VALUES
(1, 'admin', '$2a$10$AV3Ed1Xe9qwt1qLXlns3aeFvinhEb603oMU9fJmIeKNV3OO/cm88u', 'ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'user1', '$2a$10$JjH0T8QmINM70.g3LC0DAuV/MRJER73OUWFx1dEZi7/DR5Ajs1J9C', 'USER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 注文データの投入（サンプル）
-- 注：顧客データは動的に作成されるため、ここでは投入しない

-- 注文詳細データの投入（サンプル）
-- 注：注文データも動的に作成されるため、ここでは投入しない 