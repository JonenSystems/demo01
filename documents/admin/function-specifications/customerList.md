# 機能詳細説明書
### 機能名
顧客一覧表示

### 概要
管理者用顧客一覧を表示する機能

|大分類<br>（サブシステム）|中分類<br>（機能分類）|エンドポイント|
|----|----|----|
|管理者機能|顧客管理|/admin/customers|

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
│   │               │   └── AdminCustomerController.java
│   │               ├── service/
│   │               │   ├── AdminCustomerService.java
│   │               │   └── impl/
│   │               │       └── AdminCustomerServiceImpl.java
│   │               ├── repository/
│   │               │   ├── AdminCustomerRepository.java
│   │               │   └── impl/
│   │               │       └── AdminCustomerRepositoryImpl.java
│   │               └── model/
│   │                   ├── form/
│   │                   │   └── AdminCustomerListForm.java
│   │                   ├── dto/
│   │                   │   └── AdminCustomerDto.java
│   │                   └── entity/
│   │                       └── Customer.java
│   └── resources/
│       └── templates/
│           └── admin/
│               └── customer-list.html
```

#### ファイル説明
- **AdminCustomerController.java**: 管理者顧客管理に関するリクエストを処理するコントローラー。顧客一覧画面の表示とデータの取得を行う。
- **AdminCustomerService.java**: 管理者向け顧客管理サービスインターフェース。顧客一覧の取得機能を定義する。
- **AdminCustomerServiceImpl.java**: 管理者向け顧客管理サービス実装クラス。顧客一覧の取得処理を実装する。
- **AdminCustomerRepository.java**: 管理者向け顧客データアクセス層インターフェース。顧客一覧の取得機能を定義する。
- **AdminCustomerRepositoryImpl.java**: 管理者向け顧客データアクセス層実装クラス。顧客一覧の取得処理を実装する。
- **AdminCustomerListForm.java**: 管理者向け顧客一覧フォームクラス。顧客一覧の表示データを管理する。
- **AdminCustomerDto.java**: 管理者向け顧客情報のデータ転送オブジェクト。顧客の詳細情報を管理する。
- **Customer.java**: 顧客情報を管理するエンティティクラス。データベースの顧客テーブルに対応する。
- **customer-list.html**: 管理者顧客一覧画面のテンプレートファイル。顧客一覧を表示する。

---

## 3. クラス図
[クラス図](class/cl-customerList.md)

---

## 4. 処理フロー
[処理フロー](sequence/sq-customerList.md) 