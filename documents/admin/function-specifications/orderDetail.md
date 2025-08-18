# 機能詳細説明書
### 機能名
注文詳細表示

### 概要
注文詳細を表示する機能

|大分類<br>（サブシステム）|中分類<br>（機能分類）|エンドポイント|
|----|----|----|
|管理者機能|注文管理|/admin/orders/{id}|

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
│   │               │   └── AdminOrderController.java
│   │               ├── service/
│   │               │   ├── AdminOrderService.java
│   │               │   └── impl/
│   │               │       └── AdminOrderServiceImpl.java
│   │               ├── repository/
│   │               │   ├── AdminOrderRepository.java
│   │               │   └── impl/
│   │               │       └── AdminOrderRepositoryImpl.java
│   │               └── model/
│   │                   ├── dto/
│   │                   │   └── AdminOrderDto.java
│   │                   └── entity/
│   │                       └── Order.java
│   └── resources/
│       └── templates/
│           └── admin/
│               └── order-detail.html
```

#### ファイル説明
- **AdminOrderController.java**: 管理者注文管理に関するリクエストを処理するコントローラー。注文詳細画面の表示とデータの取得を行う。
- **AdminOrderService.java**: 管理者向け注文管理サービスインターフェース。注文詳細の取得機能を定義する。
- **AdminOrderServiceImpl.java**: 管理者向け注文管理サービス実装クラス。注文詳細の取得処理を実装する。
- **AdminOrderRepository.java**: 管理者向け注文データアクセス層インターフェース。注文詳細の取得機能を定義する。
- **AdminOrderRepositoryImpl.java**: 管理者向け注文データアクセス層実装クラス。注文詳細の取得処理を実装する。
- **AdminOrderDto.java**: 管理者向け注文情報のデータ転送オブジェクト。注文の詳細情報を管理する。
- **Order.java**: 注文情報を管理するエンティティクラス。データベースの注文テーブルに対応する。
- **order-detail.html**: 管理者注文詳細画面のテンプレートファイル。注文詳細を表示する。

---

## 3. クラス図
[クラス図](class/cl-orderDetail.md)

---

## 4. 処理フロー
[処理フロー](sequence/sq-orderDetail.md) 