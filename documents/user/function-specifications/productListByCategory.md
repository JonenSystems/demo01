# 機能詳細説明書
### 機能名
カテゴリ別商品表示

### 概要
指定カテゴリの商品を一覧表示する機能

|大分類<br>（サブシステム）|中分類<br>（機能分類）|エンドポイント|
|----|----|----|
|ユーザー機能|商品表示|/products/category/{category}|

---

## 1. パッケージ構成
```
com.example.shopping
├── controller
│   └── UserProductController.java
├── service
│   ├── UserProductService.java
│   └── impl
│       └── UserProductServiceImpl.java
├── repository
│   ├── UserProductRepository.java
│   └── impl
│       └── UserProductRepositoryImpl.java
├── model
│   ├── dto
│   │   └── UserProductDto.java
│   ├── form
│   │   └── UserProductListForm.java
│   └── entity
│       └── Product.java
├── common
│   └── SessionCart.java
└── resources
    └── templates
        └── product-list.html
```

#### ファイル説明
- **UserProductController.java**: カテゴリ別商品一覧表示のリクエストを受け付け、適切なビューを返すコントローラー
- **UserProductService.java**: カテゴリ別商品情報の取得に関するビジネスロジックを定義するサービスインターフェース
- **UserProductServiceImpl.java**: カテゴリ別商品情報の取得に関するビジネスロジックを実装するサービス
- **UserProductRepository.java**: カテゴリ別商品情報のデータアクセスを定義するリポジトリインターフェース
- **UserProductRepositoryImpl.java**: カテゴリ別商品情報のデータアクセスを実装するリポジトリ
- **UserProductDto.java**: 商品情報を転送するためのDTOクラス
- **UserProductListForm.java**: カテゴリ別商品一覧画面で使用するフォームクラス
- **Product.java**: 商品エンティティクラス
- **SessionCart.java**: セッション内のカート情報を管理する共通クラス
- **product-list.html**: カテゴリ別商品一覧を表示するThymeleafテンプレート

---

## 3. クラス図
[クラス図](class/cl-productListByCategory.md)

---

## 4. 処理フロー
[処理フロー](sequence/sq-productListByCategory.md)