# 機能詳細説明書
### 機能名
カート商品数量更新

### 概要
カート内の商品数量を更新する機能

|大分類<br>（サブシステム）|中分類<br>（機能分類）|エンドポイント|
|----|----|----|
|ユーザー機能|カート管理|/cart/update|

---

## 1. パッケージ構成
```
com.example.shopping
├── controller
│   └── UserCartController.java
├── service
│   ├── UserCartService.java
│   └── impl
│       └── UserCartServiceImpl.java
├── repository
│   ├── UserCartRepository.java
│   └── impl
│       └── UserCartRepositoryImpl.java
├── model
│   ├── dto
│   │   └── UserCartItemDto.java
│   └── entity
│       └── Product.java
├── common
│   └── SessionCart.java
└── resources
    └── templates
        └── user
            └── cart.html
```

#### ファイル説明
- **UserCartController.java**: カート商品数量更新のリクエストを受け付け、カート画面にリダイレクトするコントローラー
- **UserCartService.java**: カート商品数量更新に関するビジネスロジックを定義するサービスインターフェース
- **UserCartServiceImpl.java**: カート商品数量更新に関するビジネスロジックを実装するサービス
- **UserCartRepository.java**: カート情報のデータアクセスを定義するリポジトリインターフェース
- **UserCartRepositoryImpl.java**: カート情報のデータアクセスを実装するリポジトリ
- **UserCartItemDto.java**: カートアイテム情報を転送するためのDTOクラス
- **Product.java**: 商品エンティティクラス
- **SessionCart.java**: セッション内のカート情報を管理する共通クラス
- **cart.html**: カート画面（数量更新フォームの送信先）

---

## 3. クラス図
[クラス図](class/cl-updateCartItemQuantity.md)

---

## 4. 処理フロー
[処理フロー](sequence/sq-updateCartItemQuantity.md)