# 機能詳細説明書
### 機能名
商品保存

### 概要
商品情報を保存する機能

|大分類<br>（サブシステム）|中分類<br>（機能分類）|エンドポイント|
|----|----|----|
|管理者機能|商品管理|/admin/products/save|

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
│   │                   ├── form/
│   │                   │   └── AdminProductForm.java
│   │                   ├── dto/
│   │                   │   └── AdminProductDto.java
│   │                   └── entity/
│   │                       └── Product.java
```

#### ファイル説明
- **AdminProductController.java**: 管理者商品管理に関するリクエストを処理するコントローラー。商品保存処理を行う。
- **AdminProductService.java**: 管理者向け商品管理サービスインターフェース。商品保存機能を定義する。
- **AdminProductServiceImpl.java**: 管理者向け商品管理サービス実装クラス。商品保存処理を実装する。
- **AdminProductRepository.java**: 管理者向け商品データアクセス層インターフェース。商品保存機能を定義する。
- **AdminProductRepositoryImpl.java**: 管理者向け商品データアクセス層実装クラス。商品保存処理を実装する。
- **AdminProductForm.java**: 管理者向け商品フォームクラス。商品の入力データを管理する。
- **AdminProductDto.java**: 管理者向け商品情報のデータ転送オブジェクト。商品の詳細情報を管理する。
- **Product.java**: 商品情報を管理するエンティティクラス。データベースの商品テーブルに対応する。

---

## 3. クラス図
[クラス図](class/cl-productSave.md)

---

## 4. 処理フロー
[処理フロー](sequence/sq-productSave.md) 