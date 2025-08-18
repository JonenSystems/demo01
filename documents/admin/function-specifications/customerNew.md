# 機能詳細説明書
### 機能名
顧客新規作成

### 概要
顧客新規作成画面を表示する機能

|大分類<br>（サブシステム）|中分類<br>（機能分類）|エンドポイント|
|----|----|----|
|管理者機能|顧客管理|/admin/customers/new|

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
│   │               └── model/
│   │                   └── form/
│   │                       └── AdminCustomerForm.java
│   └── resources/
│       └── templates/
│           └── admin/
│               └── customer-form.html
```

#### ファイル説明
- **AdminCustomerController.java**: 管理者顧客管理に関するリクエストを処理するコントローラー。顧客新規作成画面の表示を行う。
- **AdminCustomerForm.java**: 管理者向け顧客フォームクラス。顧客の入力データを管理する。
- **customer-form.html**: 管理者顧客フォーム画面のテンプレートファイル。顧客の新規作成・編集フォームを表示する。

---

## 3. クラス図
[クラス図](class/cl-customerNew.md)

---

## 4. 処理フロー
[処理フロー](sequence/sq-customerNew.md) 