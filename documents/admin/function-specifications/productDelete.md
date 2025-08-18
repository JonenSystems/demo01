# 機能詳細説明書
### 機能名
商品削除

### 概要
商品を削除する機能

|大分類<br>（サブシステム）|中分類<br>（機能分類）|エンドポイント|
|----|----|----|
|管理者機能|商品管理|/admin/products/{id}/delete|

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
│   │                   └── entity/
│   │                       └── Product.java
```

#### ファイル説明
- **AdminProductController.java**: 管理者商品管理に関するリクエストを処理するコントローラー。商品削除処理を行う。
- **AdminProductService.java**: 管理者向け商品管理サービスインターフェース。商品削除機能を定義する。
- **AdminProductServiceImpl.java**: 管理者向け商品管理サービス実装クラス。商品削除処理を実装する。
- **AdminProductRepository.java**: 管理者向け商品データアクセス層インターフェース。商品削除機能を定義する。
- **AdminProductRepositoryImpl.java**: 管理者向け商品データアクセス層実装クラス。商品削除処理を実装する。
- **Product.java**: 商品情報を管理するエンティティクラス。データベースの商品テーブルに対応する。

---

## 3. クラス図
[クラス図](class/cl-productDelete.md)

---

## 4. 処理フロー
[処理フロー](sequence/sq-productDelete.md) 