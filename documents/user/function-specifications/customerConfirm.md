# 機能詳細説明書
### 機能名
顧客情報確認

### 概要
入力された顧客情報を確認する機能

|大分類<br>（サブシステム）|中分類<br>（機能分類）|エンドポイント|
|----|----|----|
|ユーザー機能|顧客情報|/customer/confirm|

---

## 1. パッケージ構成
```
com.example.shopping
├── controller
│   └── UserCustomerController.java
├── service
│   ├── UserCustomerService.java
│   └── impl
│       └── UserCustomerServiceImpl.java
├── repository
│   ├── UserCustomerRepository.java
│   └── impl
│       └── UserCustomerRepositoryImpl.java
├── model
│   ├── dto
│   │   └── UserCustomerDto.java
│   ├── form
│   │   └── UserCustomerForm.java
│   └── entity
│       └── Customer.java
├── common
│   ├── SessionCart.java
│   └── SessionCustomer.java
└── resources
    └── templates
        └── customer-confirm.html
```

#### ファイル説明
- **UserCustomerController.java**: 顧客情報確認のリクエストを受け付け、適切なビューを返すコントローラー
- **UserCustomerService.java**: 顧客情報の保存に関するビジネスロジックを定義するサービスインターフェース
- **UserCustomerServiceImpl.java**: 顧客情報の保存に関するビジネスロジックを実装するサービス
- **UserCustomerRepository.java**: 顧客情報のデータアクセスを定義するリポジトリインターフェース
- **UserCustomerRepositoryImpl.java**: 顧客情報のデータアクセスを実装するリポジトリ
- **UserCustomerDto.java**: 顧客情報を転送するためのDTOクラス
- **UserCustomerForm.java**: 顧客情報確認画面で使用するフォームクラス
- **Customer.java**: 顧客エンティティクラス
- **SessionCart.java**: セッション内のカート情報を管理する共通クラス
- **SessionCustomer.java**: セッション内の顧客情報を管理する共通クラス
- **customer-confirm.html**: 顧客情報確認画面を表示するThymeleafテンプレート

---

## 3. クラス図
[クラス図](class/cl-customerConfirm.md)

---

## 4. 処理フロー
[処理フロー](sequence/sq-customerConfirm.md)