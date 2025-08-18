# 機能詳細説明書
### 機能名
注文確定

### 概要
注文を確定し、データベースに保存する機能

|大分類<br>（サブシステム）|中分類<br>（機能分類）|エンドポイント|
|----|----|----|
|ユーザー機能|注文|/order/place|

---

## 1. パッケージ構成
```
com.example.shopping
├── controller
│   └── UserOrderController.java
├── service
│   ├── UserOrderService.java
│   ├── UserCartService.java
│   └── impl
│       ├── UserOrderServiceImpl.java
│       └── UserCartServiceImpl.java
├── repository
│   ├── UserOrderRepository.java
│   ├── UserOrderDetailRepository.java
│   ├── UserCartRepository.java
│   └── impl
│       ├── UserOrderRepositoryImpl.java
│       ├── UserOrderDetailRepositoryImpl.java
│       └── UserCartRepositoryImpl.java
├── model
│   ├── dto
│   │   ├── UserOrderDto.java
│   │   ├── UserCartItemDto.java
│   │   └── UserCustomerDto.java
│   ├── form
│   │   ├── UserOrderConfirmForm.java
│   │   └── UserCustomerForm.java
│   └── entity
│       ├── Order.java
│       ├── OrderDetail.java
│       ├── Product.java
│       └── Customer.java
├── common
│   ├── SessionCart.java
│   ├── SessionCustomer.java
│   └── SessionOrder.java
└── resources
    └── templates
        └── user
            └── order-confirm.html
```

#### ファイル説明
- **UserOrderController.java**: 注文確定のリクエストを受け付け、注文完了画面にリダイレクトするコントローラー
- **UserOrderService.java**: 注文確定に関するビジネスロジックを定義するサービスインターフェース
- **UserOrderServiceImpl.java**: 注文確定に関するビジネスロジックを実装するサービス
- **UserCartService.java**: カート情報の取得に関するビジネスロジックを定義するサービスインターフェース
- **UserCartServiceImpl.java**: カート情報の取得に関するビジネスロジックを実装するサービス
- **UserOrderRepository.java**: 注文情報のデータアクセスを定義するリポジトリインターフェース
- **UserOrderRepositoryImpl.java**: 注文情報のデータアクセスを実装するリポジトリ
- **UserOrderDetailRepository.java**: 注文詳細情報のデータアクセスを定義するリポジトリインターフェース
- **UserOrderDetailRepositoryImpl.java**: 注文詳細情報のデータアクセスを実装するリポジトリ
- **UserCartRepository.java**: カート情報のデータアクセスを定義するリポジトリインターフェース
- **UserCartRepositoryImpl.java**: カート情報のデータアクセスを実装するリポジトリ
- **UserOrderDto.java**: 注文情報を転送するためのDTOクラス
- **UserCartItemDto.java**: カートアイテム情報を転送するためのDTOクラス
- **UserCustomerDto.java**: 顧客情報を転送するためのDTOクラス
- **UserOrderConfirmForm.java**: 注文確認画面で使用するフォームクラス
- **UserCustomerForm.java**: 顧客情報フォームクラス
- **Order.java**: 注文エンティティクラス
- **OrderDetail.java**: 注文詳細エンティティクラス
- **Product.java**: 商品エンティティクラス
- **Customer.java**: 顧客エンティティクラス
- **SessionCart.java**: セッション内のカート情報を管理する共通クラス
- **SessionCustomer.java**: セッション内の顧客情報を管理する共通クラス
- **SessionOrder.java**: セッション内の注文情報を管理する共通クラス
- **order-confirm.html**: 注文確認画面（確定ボタンの送信先）

---

## 3. クラス図
[クラス図](class/cl-placeOrder.md)

---

## 4. 処理フロー
[処理フロー](sequence/sq-placeOrder.md)