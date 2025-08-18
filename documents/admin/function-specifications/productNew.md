# 機能詳細説明書
### 機能名
商品新規作成

### 概要
商品新規作成画面を表示する機能

|大分類<br>（サブシステム）|中分類<br>（機能分類）|エンドポイント|
|----|----|----|
|管理者機能|商品管理|/admin/products/new|

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
│   │               └── model/
│   │                   └── form/
│   │                       └── AdminProductForm.java
│   └── resources/
│       └── templates/
│           └── admin/
│               └── product-form.html
```

#### ファイル説明
- **AdminProductController.java**: 管理者商品管理に関するリクエストを処理するコントローラー。商品新規作成画面の表示を行う。
- **AdminProductService.java**: 管理者向け商品管理サービスインターフェース。利用可能なカテゴリの取得機能を定義する。
- **AdminProductServiceImpl.java**: 管理者向け商品管理サービス実装クラス。利用可能なカテゴリの取得処理を実装する。
- **AdminProductForm.java**: 管理者向け商品フォームクラス。商品の入力データを管理する。
- **product-form.html**: 管理者商品フォーム画面のテンプレートファイル。商品の新規作成・編集フォームを表示する。

---

## 3. クラス図
[クラス図](class/cl-productNew.md)

---

## 4. 処理フロー
[処理フロー](sequence/sq-productNew.md) 