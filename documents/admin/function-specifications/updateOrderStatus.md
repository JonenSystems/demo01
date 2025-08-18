# 機能詳細説明書
### 機能名
注文ステータス更新

### 概要
注文ステータスを更新する機能

|大分類<br>（サブシステム）|中分類<br>（機能分類）|エンドポイント|
|----|----|----|
|管理者機能|注文管理|/admin/orders/{id}/status|

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
│   │                   └── entity/
│   │                       └── Order.java
```

#### ファイル説明
- **AdminOrderController.java**: 管理者注文管理に関するリクエストを処理するコントローラー。注文ステータス更新処理を行う。
- **AdminOrderService.java**: 管理者向け注文管理サービスインターフェース。注文ステータス更新機能を定義する。
- **AdminOrderServiceImpl.java**: 管理者向け注文管理サービス実装クラス。注文ステータス更新処理を実装する。
- **AdminOrderRepository.java**: 管理者向け注文データアクセス層インターフェース。注文ステータス更新機能を定義する。
- **AdminOrderRepositoryImpl.java**: 管理者向け注文データアクセス層実装クラス。注文ステータス更新処理を実装する。
- **Order.java**: 注文情報を管理するエンティティクラス。データベースの注文テーブルに対応する。

---

## 3. クラス図
[クラス図](class/cl-updateOrderStatus.md)

---

## 4. 処理フロー
[処理フロー](sequence/sq-updateOrderStatus.md) 