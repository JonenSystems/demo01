# 機能詳細説明書
### 機能名
カート商品追加

### 概要
Ajaxでカートに商品を追加する機能

|大分類<br>（サブシステム）|中分類<br>（機能分類）|エンドポイント|
|----|----|----|
|ユーザー機能|カート管理|/cart/add|

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
        └── product-list.html
```

#### ファイル説明
- **UserCartController.java**: カート商品追加のAjaxリクエストを受け付け、JSONレスポンスを返すコントローラー
- **UserCartService.java**: カート商品追加に関するビジネスロジックを定義するサービスインターフェース
- **UserCartServiceImpl.java**: カート商品追加に関するビジネスロジックを実装するサービス
- **UserCartRepository.java**: カート情報のデータアクセスを定義するリポジトリインターフェース
- **UserCartRepositoryImpl.java**: カート情報のデータアクセスを実装するリポジトリ
- **UserCartItemDto.java**: カートアイテム情報を転送するためのDTOクラス
- **Product.java**: 商品エンティティクラス
- **SessionCart.java**: セッション内のカート情報を管理する共通クラス
- **product-list.html**: 商品一覧画面（Ajaxリクエストの送信元）

---

## 3. クラス図
[クラス図](class/cl-addToCart.md)

---

## 4. 処理フロー
[処理フロー](sequence/sq-addToCart.md)