# 画面仕様

## 全体共通仕様
- ユーザ画面は認証なしでアクセス可能とする。
- 管理者画面はログイン機能のありとする。
- 共通CSSファイル（`/static/css/style.css`）を用いる。
- **共通レイアウト**：ヘッダー・フッターは共通部品化されており、`user-layout.html`と`admin-layout.html`を使用する。
- 共通ヘッダー：サイト名とナビゲーション（商品一覧・カートへのリンク）を表示する。
- 共通フッター：コピーライト情報を表示する。
- ルートパス（/）アクセス時は商品一覧にリダイレクトする。
- **JavaScriptは外部ファイル化**：インラインスクリプトは使用せず、すべて外部JavaScriptファイル（`/static/js/`）に配置する。

## リソースタイプ仕様
|No.|リソースタイプ|アクセスパス|ファイル命名規則|実装仕様|
|---|---|---|---|---|
|1|テンプレート|ユーザー画面：templates/user/<br>管理者画面：templates/admin/<br>共通レイアウト：templates/|{機能}.html<br>（例1：product-list.html）<br>（例2：admin-product-list.html）<br>共通レイアウト：user-layout.html, admin-layout.html|- Formクラス経由でデータを参照する<br>- 動的要素には必ずSelenium操作用のid属性を付与する<br>- 金額表示は#numbers.formatDecimalを使用する<br>- **共通レイアウトを使用する（th:replace）**<br>- **インラインスクリプトは使用禁止**|
|2|CSS|static/css/|style.css（共通CSS）|- モダンなデザイン・レスポンシブ対応<br>- 統一されたボタン・テーブル・フォームスタイル<br>- カード形式レイアウト・グリッドレイアウト（HTML構造もdiv+CSS推奨）<br>- アラート・トースト通知・ローディング状態<br>|
|3|JavaScript|static/js/|{機能}.js<br>管理者画面：admin/{機能}.js|- **インラインスクリプト禁止**<br>- 外部ファイル化必須<br>- カート追加機能（Ajax + トースト通知）<br>- 画像エラーハンドリング<br>- フォーム送信制御<br>- 確認ダイアログ機能|
|4|アップロード画像<br>（商品画像）|static/images/product-images/|UUID形式<br>（例：3ff7fe06-443c-417d-978b-2c95816943fd.png）|- UUID形式のファイル名<br>- 対応形式：JPG、PNG、GIF、WebP<br>- 最大ファイルサイズ：10MB<br>- 自動削除機能|
---

# 画面ファイル構成

## 全体構成

```
src/
├── main/
│   ├── java/
│   │   └── com/example/shopping/
│   │       └── config/                           # 画面関連設定
│   │           ├── SecurityConfig.java          # セキュリティ設定（認証・認可）
│   │           ├── WebConfig.java               # Web設定（リソースハンドラー等）
│   │           └── FileUploadConfig.java        # ファイルアップロード設定
│   └── resources/
│       ├── static/                              # 静的リソース
│       │   ├── css/
│       │   │   └── style.css                    # 共通CSSファイル（760行）
│       │   ├── js/
│       │   │   ├── product-list.js              # 商品一覧用JavaScript（80行）
│       │   │   ├── cart.js                      # カート画面用
│       │   │   ├── order-confirm.js             # 注文確認画面用
│       │   │   └── admin/
│       │   │           ├── product-list.js      # 管理者商品一覧画面用
│       │   │           └── product-form.js      # 管理者商品登録・編集画面用
│       │   ├── images/                          # 画像ファイル
│       │   │   ├── product-images/              # 商品画像ディレクトリ
│       │   │   ├── logos/                       # ロゴ画像（将来の拡張用）
│       │   │   ├── banners/                     # バナー画像（将来の拡張用）
│       │   │   ├── icons/                       # アイコン画像（将来の拡張用）
│       │   │   └── README.md                    # 画像ディレクトリ説明
│       │   └── index.html                       # 静的ホームページ
│       ├── templates/                           # Thymeleafテンプレート
│       │   ├── user-layout.html                 # ユーザー画面用共通レイアウト
│       │   ├── admin-layout.html                # 管理者画面用共通レイアウト
│       │   ├── user/                            # ユーザー画面
│       │   │   ├── product-list.html            # 商品一覧画面（5.3KB, 105行）
│       │   │   ├── cart.html                    # カート画面（6.8KB, 121行）
│       │   │   ├── customer-input.html          # 顧客情報入力画面（4.4KB, 86行）
│       │   │   ├── order-confirm.html           # 注文確認画面（7.0KB, 124行）
│       │   │   └── order-complete.html          # 注文完了画面（6.3KB, 117行）
│       │   └── admin/                           # 管理者画面
│       │       ├── login.html                   # 管理者ログイン画面（2.2KB, 51行）
│       │       ├── dashboard.html               # 管理者ダッシュボード（6.2KB, 133行）
│       │       ├── product-list.html            # 商品管理画面（9.7KB, 162行）
│       │       ├── product-form.html            # 商品登録・編集画面（6.2KB, 106行）
│       │       ├── product-delete-confirm.html  # 商品削除確認画面（4.0KB, 91行）
│       │       ├── customer-list.html           # 顧客管理画面（7.7KB, 137行）
│       │       ├── customer-form.html           # 顧客登録・編集画面（3.3KB, 71行）
│       │       ├── customer-delete-confirm.html # 顧客削除確認画面（3.5KB, 83行）
│       │       ├── order-list.html              # 注文管理画面（9.6KB, 170行）
│       │       ├── order-detail.html            # 注文詳細画面（8.2KB, 165行）
│       │       └── low-stock-products.html      # 在庫不足商品画面（5.5KB, 112行）
│       ├── application.properties               # アプリケーション設定
│       ├── application-dev.properties           # 開発環境設定
│       └── application-prod.properties          # 本番環境設定
└── test/
    └── resources/
        └── application-test.properties          # テスト環境設定
```

## テンプレート構成

### 共通レイアウト（templates/）

|No.|レイアウト名|ファイル名|用途|機能|
|---|---|---|---|---|
|1|ユーザー画面用共通レイアウト|user-layout.html|ユーザー画面全般|ヘッダー（サイト名、ナビゲーション、カートアイテム数）<br>フッター（コピーライト）<br>head要素（メタタグ、CSS、タイトル）|
|2|管理者画面用共通レイアウト|admin-layout.html|管理者画面全般|ヘッダー（管理者画面タイトル、ナビゲーション、ログアウト）<br>フッター（コピーライト）<br>head要素（メタタグ、CSS、タイトル）|

### ユーザー画面（templates/user/）

|No.|画面名|ファイル名|形式・様式|機能|適用レイアウト|
|---|---|---|---|---|---|
|1|商品一覧画面|product-list.html|グリッドレイアウト・カード形式|商品一覧表示<br>カテゴリフィルター<br>商品画像表示（No Imageプレースホルダー対応）<br>カートへの商品追加機能|user-layout.html|
|2|カート画面|cart.html|テーブル形式|商品一覧表示<br>数量変更・削除機能<br>合計金額表示|user-layout.html|
|3|顧客情報入力画面|customer-input.html|フォーム形式|バリデーション対応<br>エラー表示機能|user-layout.html|
|4|注文確認画面|order-confirm.html|確認形式|注文内容確認<br>顧客情報表示<br>合計金額表示|user-layout.html|
|5|注文完了画面|order-complete.html|完了形式|注文完了メッセージ<br>注文詳細表示|user-layout.html|

### 管理者画面（templates/admin/）

|No.|画面名|ファイル名|形式・様式|機能|適用レイアウト|
|---|---|---|---|---|---|
|1|管理者ログイン画面|login.html|ログインフォーム|モダンなログインデザイン<br>エラーメッセージ表示|**専用レイアウト<br>（共通部品化対象外）**|
|2|管理者ダッシュボード|dashboard.html|ダッシュボード形式|統計情報表示<br>在庫不足商品一覧<br>クイックアクション|admin-layout.html|
|3|商品管理画面|product-list.html|テーブル形式|商品一覧表示<br>検索・フィルター機能<br>編集・削除機能|admin-layout.html|
|4|商品登録・編集画面|product-form.html|フォーム形式|画像アップロード機能<br>バリデーション対応|admin-layout.html|
|5|商品削除確認画面|product-delete-confirm.html|確認形式|商品削除確認|admin-layout.html|
|6|顧客管理画面|customer-list.html|テーブル形式|顧客一覧表示<br>検索機能<br>編集・削除機能|admin-layout.html|
|7|顧客登録・編集画面|customer-form.html|フォーム形式|顧客情報登録・編集|admin-layout.html|
|8|顧客削除確認画面|customer-delete-confirm.html|確認形式|顧客削除確認|admin-layout.html|
|9|注文管理画面|order-list.html|テーブル形式|注文一覧表示<br>ステータスフィルター・検索機能|admin-layout.html|
|10|注文詳細画面|order-detail.html|詳細表示形式|注文詳細表示<br>ステータス変更機能|admin-layout.html|
|11|在庫不足商品画面|low-stock-products.html|テーブル形式|在庫不足商品一覧表示|admin-layout.html|

## 静的リソース構成

### CSSファイル（static/css/）
- **style.css**: 共通CSSファイル（760行）

### JavaScriptファイル（static/js/）
- **product-list.js**: 商品一覧用JavaScript（80行）
  - カート追加機能（Ajax + トースト通知）
  - 画像エラーハンドリング
- **cart.js**: カート画面用JavaScript（新規）
  - 画像エラーハンドリング
  - 商品削除確認ダイアログ
  - カートクリア確認ダイアログ
- **order-confirm.js**: 注文確認画面用JavaScript（新規）
  - 注文確定確認ダイアログ
- **admin/product-list.js**: 管理者商品一覧画面用JavaScript（新規）
  - 画像エラーハンドリング
- **admin/product-form.js**: 管理者商品登録・編集画面用JavaScript（新規）
  - 画像エラーハンドリング

### 画像ファイル（static/images/）
- **product-images/**: 商品画像ディレクトリ
- **logos/**: ロゴ画像（将来の拡張用）
- **banners/**: バナー画像（将来の拡張用）
- **icons/**: アイコン画像（将来の拡張用）

## 設定クラス（src/main/java/com/example/shopping/config/）
|No.|設定名|ファイル名|設定項目|
|---|---|---|---|
|1|セキュリティ設定|SecurityConfig.java|認証・認可設定<br>- 管理者画面の認証要求<br>- ログイン・ログアウト設定<br>- CSRF保護設定<br>- セッション管理設定|
|2|Web設定|WebConfig.java|リソースハンドラー設定<br>- 静的リソース（CSS、JS、画像）のマッピング<br>- ファイルアップロードパス設定<br>- エラーページ設定|
|3|ファイルアップロード設定|FileUploadConfig.java|ファイルアップロード設定<br>- 最大ファイルサイズ（10MB）<br>- 対応ファイル形式（JPG、PNG、GIF、WebP）<br>- アップロードディレクトリ設定|

---

# Thymeleafテンプレート実装仕様

## 基本原則
- **Formクラス経由でデータを参照する**
- **動的要素には必ずSelenium操作用のid属性を付与する**
- **金額表示は#numbers.formatDecimalを使用する**
- **インラインスクリプトは使用禁止**：すべてのJavaScriptは外部ファイルに配置する

## 金額表示の実装
### 正しい実装方法
```html
<!-- ✅ 推奨される金額表示 -->
<span th:text="'¥' + ${#numbers.formatDecimal(product.price ?: 0, 0, 'COMMA', 0, 'POINT')}"></span>

<!-- パラメータの説明 -->
<!-- formatDecimal(値, 小数点以下桁数, グループ区切り, 小数点以下最小桁数, 小数点記号) -->
<!-- 金額用途では小数点以下桁数は0でOK -->
```

### 非推奨の実装方法
```html
<!-- ❌ 非推奨（Thymeleaf 3.1/Spring6で非対応） -->
<span th:text="'¥' + ${#numbers.formatInteger(product.price, 1, 'COMMA', 0, 'POINT')}"></span>

<!-- ❌ 非推奨（nullの場合にエラー） -->
<span th:text="'¥' + ${#numbers.formatDecimal(product.price, 0, 'COMMA', 0, 'POINT')}"></span>
```

## Formクラス経由でのデータ参照
### 正しい実装
```html
<!-- ✅ Formクラス経由でデータを参照 -->
<div th:each="product : ${productListForm.products}">
    <span th:text="${product.name}"></span>
    <span th:text="${product.price}"></span>
</div>

<div th:each="item : ${cartForm.cartItems}">
    <span th:text="${item.name}"></span>
    <span th:text="${item.quantity}"></span>
</div>
```

### 非推奨の実装
```html
<!-- ❌ 直接的なモデル属性参照 -->
<div th:each="product : ${products}">
    <span th:text="${product.name}"></span>
</div>
```

## 条件分岐とnull安全
```html
<!-- null安全な実装 -->
<div th:if="${productListForm.products != null and !productListForm.products.empty}">
    <div th:each="product : ${productListForm.products}">
        <span th:text="${product.name ?: '商品名なし'}"></span>
        <span th:text="'¥' + ${#numbers.formatDecimal(product.price ?: 0, 0, 'COMMA', 0, 'POINT')}"></span>
    </div>
</div>

<div th:if="${productListForm.products == null or productListForm.products.empty}">
    <p>商品が見つかりませんでした。</p>
</div>
```

## Selenium操作用id属性の実装

### 必須id属性
以下の要素には必ずid属性を付与する：

```html
<!-- フォーム要素 -->
<input type="text" id="name" th:field="*{name}" class="form-control" required>
<input type="email" id="email" th:field="*{email}" class="form-control" required>
<input type="tel" id="phone" th:field="*{phone}" class="form-control">
<textarea id="address" th:field="*{address}" class="form-control" rows="3"></textarea>

<!-- 検索・フィルター要素 -->
<input type="text" id="searchName" name="searchName" th:value="${searchName}" class="form-control">
<select id="searchCategory" name="searchCategory" class="form-control">
<input type="email" id="searchEmail" name="searchEmail" th:value="${searchEmail}" class="form-control">

<!-- ボタン・リンク要素 -->
<button type="submit" id="login-btn" class="login-btn">ログイン</button>
<a id="edit-product" th:href="@{/admin/products/{id}/edit(id=${product.id})}" class="btn btn-primary btn-sm">編集</a>
<a id="delete-product" th:href="@{/admin/products/{id}/delete(id=${product.id})}" class="btn btn-danger btn-sm">削除</a>

<!-- 動的コンテンツ要素 -->
<div id="toast" class="toast"></div>
<div id="product-grid" class="product-grid">
```

## 画像表示の実装

### 商品画像表示
```html
<!-- 画像がある場合 -->
<div th:if="${product.imagePath != null and !product.imagePath.empty and product.imagePath != ''}">
    <img th:src="${product.imagePath}" 
         th:alt="${product.name}" 
         class="product-image"
         data-has-image="true">
    <div class="product-image no-image-placeholder hidden">
        No Image
    </div>
</div>

<!-- 画像がない場合 -->
<div th:unless="${product.imagePath != null and !product.imagePath.empty and product.imagePath != ''}" 
     class="product-image no-image-placeholder">
    No Image
</div>
```

## エラーハンドリング

### バリデーションエラー表示
```html
<!-- フォームエラー表示 -->
<div class="form-group">
    <label for="name">お名前 <span class="text-danger">*</span></label>
    <input type="text" id="name" th:field="*{name}" class="form-control" 
           th:classappend="${#fields.hasErrors('name')} ? 'is-invalid' : ''" required>
    <div th:if="${#fields.hasErrors('name')}" class="invalid-feedback" th:errors="*{name}"></div>
</div>
```

### アラート表示
```html
<!-- 成功メッセージ -->
<div th:if="${param.logout}" class="alert alert-success">
    ログアウトしました。
</div>

<!-- エラーメッセージ -->
<div th:if="${param.error}" class="alert alert-danger">
    ユーザー名またはパスワードが正しくありません。
</div>

<!-- 警告メッセージ -->
<div th:if="${!productListForm.hasProducts()}" class="alert alert-warning">
    <p>商品が見つかりませんでした。</p>
</div>
```

## レスポンシブ対応

### グリッドレイアウト
```css
/* 商品グリッド */
.product-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    gap: 1.5rem;
    margin-top: 1rem;
}

/* レスポンシブ対応 */
@media (max-width: 768px) {
    .product-grid {
        grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
        gap: 1rem;
    }
}
```

## アクセシビリティ対応

### セマンティックHTML
```html
<!-- ヘッダー -->
<header class="header">
    <div class="container">
        <a href="/" class="site-title">オンラインショップ</a>
        <nav class="nav-links">
            <a href="/">商品一覧</a>
            <a href="/cart">カート <span th:if="${cartItemCount > 0}" th:text="'(' + ${cartItemCount} + ')'">(0)</span></a>
        </nav>
    </div>
</header>

<!-- メインコンテンツ -->
<main class="main-content">
    <div class="container">
        <!-- コンテンツ -->
    </div>
</main>

<!-- フッター -->
<footer class="footer">
    <div class="container">
        <p>&copy; 2024 オンラインショップ. All rights reserved.</p>
    </div>
</footer>
```

## パフォーマンス最適化

### 画像最適化
```html
<!-- 画像キャッシュバスティング -->
<img th:src="${productForm.imagePath + '?v=' + #dates.format(#dates.createNow(), 'yyyyMMddHHmmss')}" 
     th:alt="${productForm.name}" 
     class="product-image">
```

### 条件付きレンダリング
```html
<!-- 不要な要素の非表示 -->
<div class="no-image-placeholder hidden" th:classappend="${product.imagePath != null and !product.imagePath.empty} ? 'hidden' : ''">
    No Image
</div>
```

---

## JavaScript実装仕様

### 基本原則
- **インラインスクリプト禁止**：`onclick`、`onerror`、`onload`などのインライン属性は使用しない
- **外部ファイル化必須**：すべてのJavaScriptは`/static/js/`ディレクトリに配置
- **機能別ファイル分割**：画面ごとに専用のJavaScriptファイルを作成
- **イベントリスナー使用**：DOMContentLoadedイベントで初期化処理を実行

### 実装パターン

#### 基本構造
```javascript
// {機能名}.js
// {機能名}画面用JavaScript

// 共通機能
function handleImageError(img) {
    img.style.display = 'none';
    const placeholder = img.nextElementSibling;
    if (placeholder && placeholder.classList.contains('no-image-placeholder')) {
        placeholder.style.display = 'flex';
    }
}

// DOMContentLoadedイベントリスナー
document.addEventListener('DOMContentLoaded', function() {
    // 初期化処理
    initializeImageErrorHandling();
    initializeConfirmDialogs();
});

// 初期化関数
function initializeImageErrorHandling() {
    const images = document.querySelectorAll('.product-image');
    images.forEach(img => {
        img.addEventListener('error', function() {
            handleImageError(this);
        });
    });
}

function initializeConfirmDialogs() {
    // 確認ダイアログの設定
}
```

#### 画像エラーハンドリング
```javascript
// 画像エラーハンドリング
function handleImageError(img) {
    img.style.display = 'none';
    const placeholder = img.nextElementSibling;
    if (placeholder && placeholder.classList.contains('no-image-placeholder')) {
        placeholder.style.display = 'flex';
    }
}

// 使用例
document.addEventListener('DOMContentLoaded', function() {
    const productImages = document.querySelectorAll('.product-image');
    productImages.forEach(img => {
        img.addEventListener('error', function() {
            handleImageError(this);
        });
    });
});
```

#### 確認ダイアログ
```javascript
// 確認ダイアログ
function confirmAction(message) {
    return confirm(message);
}

// 使用例
document.addEventListener('DOMContentLoaded', function() {
    const deleteButtons = document.querySelectorAll('form[action*="/cart/remove"] button[type="submit"]');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirmAction('この商品をカートから削除しますか？')) {
                e.preventDefault();
            }
        });
    });
});
```

### HTMLテンプレートでの実装

#### 外部JavaScriptファイルの読み込み
```html
<!-- ユーザー画面 -->
<script th:src="@{/js/product-list.js}"></script>
<script th:src="@{/js/cart.js}"></script>
<script th:src="@{/js/order-confirm.js}"></script>

<!-- 管理者画面 -->
<script th:src="@{/js/admin/product-list.js}"></script>
<script th:src="@{/js/admin/product-form.js}"></script>
```

#### 画像エラーハンドリング対応
```html
<!-- 画像がある場合 -->
<div th:if="${product.imagePath != null and !product.imagePath.empty}">
    <img th:src="${product.imagePath}" 
         th:alt="${product.name}" 
         class="product-image">
    <div class="product-image no-image-placeholder hidden">
        No Image
    </div>
</div>

<!-- 画像がない場合 -->
<div th:unless="${product.imagePath != null and !product.imagePath.empty}" 
     class="product-image no-image-placeholder">
    No Image
</div>
```

#### 確認ダイアログ対応
```html
<!-- インラインスクリプト禁止 -->
<!-- ❌ 非推奨 -->
<button type="submit" onclick="return confirm('削除しますか？')">削除</button>

<!-- ✅ 推奨 -->
<button type="submit" class="btn btn-danger">削除</button>
```

### セキュリティ考慮事項

#### CSP対応
- **インラインスクリプト禁止**：CSPエラーを防ぐため、すべてのJavaScriptを外部ファイルに配置
- **XSS対策**：外部ファイル化によりインラインスクリプト攻撃を防止
- **ファイル配置**：適切なディレクトリ構造で管理

#### エラーハンドリング
```javascript
// エラーハンドリングの実装
document.addEventListener('DOMContentLoaded', function() {
    try {
        // 初期化処理
        initializeFeatures();
    } catch (error) {
        console.error('JavaScript初期化エラー:', error);
    }
});

function initializeFeatures() {
    // 機能の初期化
}
```

### パフォーマンス最適化

#### ファイル分割
- **機能別分割**：画面ごとに専用ファイルを作成
- **共通機能**：共通のユーティリティ関数は別ファイルに分離可能
- **遅延読み込み**：必要に応じて動的読み込みを検討

#### キャッシュ対策
```html
<!-- キャッシュバスティング -->
<script th:src="@{/js/product-list.js(v=${#dates.format(#dates.createNow(), 'yyyyMMddHHmmss')})}"></script>
```

### テスト対応

#### Seleniumテスト対応
```javascript
// テスト用のid属性を活用
document.addEventListener('DOMContentLoaded', function() {
    // テスト用の要素を特定しやすいようにid属性を使用
    const deleteButton = document.getElementById('delete-product-1');
    if (deleteButton) {
        deleteButton.addEventListener('click', function(e) {
            if (!confirmAction('この商品を削除しますか？')) {
                e.preventDefault();
            }
        });
    }
});
```

#### デバッグ対応
```javascript
// 開発環境でのデバッグ
if (typeof console !== 'undefined' && console.log) {
    console.log('JavaScript初期化完了');
}
```

---

# 実装チェックリスト

## 必須実装項目
- [x] Formクラス経由でのデータ参照
- [x] 金額表示の#numbers.formatDecimal使用
- [x] Selenium操作用id属性の付与
- [x] null安全な条件分岐
- [x] レスポンシブ対応
- [x] アクセシビリティ対応
- [x] エラーハンドリング
- [x] 画像表示の適切な実装
- [x] **インラインスクリプト禁止**：すべてのJavaScriptを外部ファイルに配置

## 推奨実装項目
- [x] セマンティックHTML構造
- [x] 統一されたCSSクラス命名
- [x] パフォーマンス最適化
- [x] ユーザビリティ向上
- [x] セキュリティ対策
- [x] **JavaScript外部ファイル化**：機能別ファイル分割と適切なディレクトリ構造
- [x] **CSP対応**：Content Security Policyに準拠した実装

---