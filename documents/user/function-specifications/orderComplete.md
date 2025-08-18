# 機能詳細説明書
### 機能名
注文完了

### 概要
注文完了画面を表示する機能

|大分類<br>（サブシステム）|中分類<br>（機能分類）|エンドポイント|
|----|----|----|
|ユーザー機能|注文|/order/complete|

---

## 1. パッケージ構成
```
com.example.shopping
├── controller
│   └── UserOrderController.java
├── service
│   ├── UserOrderService.java
│   └── impl
│       └── UserOrderServiceImpl.java
├── repository
│   ├── UserOrderRepository.java
│   └── impl
│       └── UserOrderRepositoryImpl.java
├── model
│   ├── dto
│   │   └── UserOrderDto.java
│   ├── form
│   │   └── UserOrderConfirmForm.java
│   └── entity
│       ├── Order.java
│       └── OrderDetail.java
├── common
│   └── SessionOrder.java
└── resources
    └── templates
        └── order-complete.html
```

#### ファイル説明
- **UserOrderController.java**: 注文完了画面表示のリクエストを受け付け、適切なビューを返すコントローラー
- **UserOrderService.java**: 注文完了情報の取得に関するビジネスロジックを定義するサービスインターフェース
- **UserOrderServiceImpl.java**: 注文完了情報の取得に関するビジネスロジックを実装するサービス
- **UserOrderRepository.java**: 注文情報のデータアクセスを定義するリポジトリインターフェース
- **UserOrderRepositoryImpl.java**: 注文情報のデータアクセスを実装するリポジトリ
- **UserOrderDto.java**: 注文情報を転送するためのDTOクラス
- **UserOrderConfirmForm.java**: 注文完了画面で使用するフォームクラス
- **Order.java**: 注文エンティティクラス
- **OrderDetail.java**: 注文詳細エンティティクラス
- **SessionOrder.java**: セッション内の注文情報を管理する共通クラス
- **order-complete.html**: 注文完了画面を表示するThymeleafテンプレート

---

## 3. クラス図
[クラス図](class/cl-orderComplete.md)

---

## 4. 処理フロー
[処理フロー](sequence/sq-orderComplete.md)