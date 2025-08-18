# 機能詳細説明書
### 機能名
管理者ダッシュボード

### 概要
管理者ダッシュボードを表示する機能

|大分類<br>（サブシステム）|中分類<br>（機能分類）|エンドポイント|
|----|----|----|
|管理者機能|ダッシュボード|/admin|

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
│   │               │   └── AdminController.java
│   │               ├── service/
│   │               │   ├── AdminOrderService.java
│   │               │   ├── AdminProductService.java
│   │               │   └── impl/
│   │               │       ├── AdminOrderServiceImpl.java
│   │               │       └── AdminProductServiceImpl.java
│   │               ├── repository/
│   │               │   ├── AdminOrderRepository.java
│   │               │   ├── AdminProductRepository.java
│   │               │   └── impl/
│   │               │       ├── AdminOrderRepositoryImpl.java
│   │               │       └── AdminProductRepositoryImpl.java
│   │               └── model/
│   │                   ├── dto/
│   │                   │   ├── AdminOrderDto.java
│   │                   │   └── AdminProductDto.java
│   │                   └── entity/
│   │                       ├── Order.java
│   │                       └── Product.java
│   └── resources/
│       └── templates/
│           └── admin/
│               └── dashboard.html
```

#### ファイル説明
- **AdminController.java**: 管理者ダッシュボードに関するリクエストを処理するコントローラー。ダッシュボード画面の表示とデータの取得を行う。
- **AdminOrderService.java**: 管理者向け注文管理サービスインターフェース。注文統計情報の取得機能を定義する。
- **AdminProductService.java**: 管理者向け商品管理サービスインターフェース。在庫不足商品の取得機能を定義する。
- **AdminOrderServiceImpl.java**: 管理者向け注文管理サービス実装クラス。注文統計情報の取得処理を実装する。
- **AdminProductServiceImpl.java**: 管理者向け商品管理サービス実装クラス。在庫不足商品の取得処理を実装する。
- **AdminOrderRepository.java**: 管理者向け注文データアクセス層インターフェース。注文統計情報の取得機能を定義する。
- **AdminProductRepository.java**: 管理者向け商品データアクセス層インターフェース。在庫不足商品の取得機能を定義する。
- **AdminOrderRepositoryImpl.java**: 管理者向け注文データアクセス層実装クラス。注文統計情報の取得処理を実装する。
- **AdminProductRepositoryImpl.java**: 管理者向け商品データアクセス層実装クラス。在庫不足商品の取得処理を実装する。
- **AdminOrderDto.java**: 管理者向け注文情報のデータ転送オブジェクト。注文の詳細情報を管理する。
- **AdminProductDto.java**: 管理者向け商品情報のデータ転送オブジェクト。商品の詳細情報を管理する。
- **Order.java**: 注文情報を管理するエンティティクラス。データベースの注文テーブルに対応する。
- **Product.java**: 商品情報を管理するエンティティクラス。データベースの商品テーブルに対応する。
- **dashboard.html**: 管理者ダッシュボード画面のテンプレートファイル。注文統計情報と在庫不足商品一覧を表示する。

---

## 3. クラス図
[クラス図](class/cl-dashboard.md)

---

## 4. 処理フロー
[処理フロー](sequence/sq-dashboard.md) 