# 機能詳細説明書
### 機能名
カートクリア

### 概要
カート内の全商品を削除する機能

|大分類<br>（サブシステム）|中分類<br>（機能分類）|エンドポイント|
|----|----|----|
|ユーザー機能|カート管理|/cart/clear|

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
- **UserCartController.java**: カートクリアのリクエストを受け付け、カート画面にリダイレクトするコントローラー
- **UserCartService.java**: カートクリアに関するビジネスロジックを定義するサービスインターフェース
- **UserCartServiceImpl.java**: カートクリアに関するビジネスロジックを実装するサービス
- **UserCartRepository.java**: カート情報のデータアクセスを定義するリポジトリインターフェース
- **UserCartRepositoryImpl.java**: カート情報のデータアクセスを実装するリポジトリ
- **UserCartItemDto.java**: カートアイテム情報を転送するためのDTOクラス
- **Product.java**: 商品エンティティクラス
- **SessionCart.java**: セッション内のカート情報を管理する共通クラス
- **cart.html**: カート画面（クリアボタンの送信先）

---

## 3. クラス図
[クラス図](class/cl-clearCart.md)

---

## 4. 処理フロー
[処理フロー](sequence/sq-clearCart.md)