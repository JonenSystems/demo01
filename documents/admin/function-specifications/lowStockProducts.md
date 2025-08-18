# 機能詳細説明書
### 機能名
在庫不足商品表示

### 概要
在庫不足商品を一覧表示する機能

|大分類<br>（サブシステム）|中分類<br>（機能分類）|エンドポイント|
|----|----|----|
|管理者機能|商品管理|/admin/products/low-stock|

---

## 1. パッケージ構成
```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── shopping/
│   │               ├── controller/
│   │               │   └── AdminProductController.java
│   │               ├── service/
│   │               │   ├── AdminProductService.java
│   │               │   └── impl/
│   │               │       └── AdminProductServiceImpl.java
│   │               ├── repository/
│   │               │   ├── AdminProductRepository.java
│   │               │   └── impl/
│   │               │       └── AdminProductRepositoryImpl.java
│   │               └── model/
│   │                   ├── dto/
│   │                   │   └── AdminProductDto.java
│   │                   └── entity/
│   │                       └── Product.java
│   └── resources/
│       └── templates/
│           └── admin/
│               └── low-stock-products.html
```

#### ファイル説明
- **AdminProductController.java**: 管理者商品管理に関するリクエストを処理するコントローラー。在庫不足商品一覧画面の表示とデータの取得を行う。
- **AdminProductService.java**: 管理者向け商品管理サービスインターフェース。在庫不足商品の取得機能を定義する。
- **AdminProductServiceImpl.java**: 管理者向け商品管理サービス実装クラス。在庫不足商品の取得処理を実装する。
- **AdminProductRepository.java**: 管理者向け商品データアクセス層インターフェース。在庫不足商品の取得機能を定義する。
- **AdminProductRepositoryImpl.java**: 管理者向け商品データアクセス層実装クラス。在庫不足商品の取得処理を実装する。
- **AdminProductDto.java**: 管理者向け商品情報のデータ転送オブジェクト。商品の詳細情報を管理する。
- **Product.java**: 商品情報を管理するエンティティクラス。データベースの商品テーブルに対応する。
- **low-stock-products.html**: 管理者在庫不足商品一覧画面のテンプレートファイル。在庫不足商品一覧を表示する。

---

## 3. クラス図
[クラス図](class/cl-lowStockProducts.md)

---

## 4. 処理フロー
[処理フロー](sequence/sq-lowStockProducts.md) 