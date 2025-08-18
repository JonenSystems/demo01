# 機能詳細説明書
### 機能名
管理者ログイン

### 概要
管理者ログイン画面を表示する機能

|大分類<br>（サブシステム）|中分類<br>（機能分類）|エンドポイント|
|----|----|----|
|管理者機能|認証|/admin/login|

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
│   │               │   └── AdminAuthController.java
│   │               └── model/
│   │                   └── form/
│   │                       └── AdminLoginForm.java
│   └── resources/
│       └── templates/
│           └── admin/
│               └── login.html
```

#### ファイル説明
- **AdminAuthController.java**: 管理者認証に関するリクエストを処理するコントローラー。ログイン画面の表示とフォームの初期化を行う。
- **AdminLoginForm.java**: ログイン画面のフォームデータを管理するクラス。バリデーション機能を持つ。
- **login.html**: 管理者ログイン画面のテンプレートファイル。ユーザー名とパスワードの入力フォームを表示する。

---

## 3. クラス図
[クラス図](class/cl-showLoginForm.md)

---

## 4. 処理フロー
[処理フロー](sequence/sq-showLoginForm.md) 