# 機能詳細説明書
### 機能名
顧客保存

### 概要
顧客情報を保存する機能

|大分類<br>（サブシステム）|中分類<br>（機能分類）|エンドポイント|
|----|----|----|
|管理者機能|顧客管理|/admin/customers/save|

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
│   │                   │   └── AdminCustomerForm.java
│   │                   ├── dto/
│   │                   │   └── AdminCustomerDto.java
│   │                   └── entity/
│   │                       └── Customer.java
```

#### ファイル説明
- **AdminCustomerController.java**: 管理者顧客管理に関するリクエストを処理するコントローラー。顧客保存処理を行う。
- **AdminCustomerService.java**: 管理者向け顧客管理サービスインターフェース。顧客保存機能を定義する。
- **AdminCustomerServiceImpl.java**: 管理者向け顧客管理サービス実装クラス。顧客保存処理を実装する。
- **AdminCustomerRepository.java**: 管理者向け顧客データアクセス層インターフェース。顧客保存機能を定義する。
- **AdminCustomerRepositoryImpl.java**: 管理者向け顧客データアクセス層実装クラス。顧客保存処理を実装する。
- **AdminCustomerForm.java**: 管理者向け顧客フォームクラス。顧客の入力データを管理する。
- **AdminCustomerDto.java**: 管理者向け顧客情報のデータ転送オブジェクト。顧客の詳細情報を管理する。
- **Customer.java**: 顧客情報を管理するエンティティクラス。データベースの顧客テーブルに対応する。

---

## 3. クラス図
[クラス図](class/cl-customerSave.md)

---

## 4. 処理フロー
[処理フロー](sequence/sq-customerSave.md) 