# 機能詳細説明書
### 機能名
顧客削除

### 概要
顧客を削除する機能

|大分類<br>（サブシステム）|中分類<br>（機能分類）|エンドポイント|
|----|----|----|
|管理者機能|顧客管理|/admin/customers/{id}/delete|

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
│   │                   └── entity/
│   │                       └── Customer.java
```

#### ファイル説明
- **AdminCustomerController.java**: 管理者顧客管理に関するリクエストを処理するコントローラー。顧客削除処理を行う。
- **AdminCustomerService.java**: 管理者向け顧客管理サービスインターフェース。顧客削除機能を定義する。
- **AdminCustomerServiceImpl.java**: 管理者向け顧客管理サービス実装クラス。顧客削除処理を実装する。
- **AdminCustomerRepository.java**: 管理者向け顧客データアクセス層インターフェース。顧客削除機能を定義する。
- **AdminCustomerRepositoryImpl.java**: 管理者向け顧客データアクセス層実装クラス。顧客削除処理を実装する。
- **Customer.java**: 顧客情報を管理するエンティティクラス。データベースの顧客テーブルに対応する。

---

## 3. クラス図
[クラス図](class/cl-customerDelete.md)

---

## 4. 処理フロー
[処理フロー](sequence/sq-customerDelete.md) 