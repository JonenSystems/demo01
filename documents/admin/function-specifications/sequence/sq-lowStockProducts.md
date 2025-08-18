# シーケンス図_在庫不足商品表示

## シーケンス図

```mermaid
sequenceDiagram
    actor ユーザー
    participant view as View<br/>low-stock-products.html
    participant AdminProductController as Controller<br/>AdminProductController
    participant AdminProductService as Service<br/>AdminProductService<br/>AdminProductServiceImpl
    participant AdminProductRepository as Repository<br/>AdminProductRepository<br/>AdminProductRepositoryImpl
    participant Product as Entity<br/>Product
    participant DB as DB<br/>H2<br/>PUBLIC<br/>products

    ユーザー->>view: 在庫不足商品画面表示要求
    view->>AdminProductController: GET /admin/products/low-stock
    AdminProductController->>AdminProductService: getLowStockProducts(threshold)
    AdminProductService->>AdminProductRepository: findLowStockProducts(threshold)
    AdminProductRepository->>DB: SELECT * FROM products WHERE stock < ?
    DB-->>AdminProductRepository: 在庫不足商品データ
    AdminProductRepository-->>AdminProductService: List<Product>
    AdminProductService-->>AdminProductController: List<Product>
    AdminProductController->>view: model.addAttribute("lowStockProducts", lowStockProducts)
    view-->>ユーザー: 在庫不足商品画面表示

    Note over DB: データベース<br/>H2<br/>PUBLIC<br/>products
```

## シーケンス図の解説

### 処理フロー
1. **ユーザーが在庫不足商品画面表示を要求**
   - ユーザーがブラウザで在庫不足商品画面にアクセス

2. **ViewからControllerへのリクエスト**
   - `low-stock-products.html`から`AdminProductController`の`lowStockProducts`メソッドにGETリクエスト
   - 在庫閾値をパラメータとして受け取る（デフォルト値: 10）

3. **ControllerからServiceへの処理委譲**
   - `AdminProductController`が`AdminProductService`の`getLowStockProducts`メソッドを呼び出し
   - 在庫閾値を渡して在庫不足商品を取得

4. **ServiceからRepositoryへのデータ取得**
   - `AdminProductServiceImpl`が`AdminProductRepository`の`findLowStockProducts`メソッドを呼び出し
   - データベースから在庫不足商品を取得

5. **データベースアクセス**
   - `AdminProductRepositoryImpl`がH2データベースのproductsテーブルにクエリを実行
   - 在庫数が閾値未満の商品データを取得

6. **Viewへのデータ設定**
   - `AdminProductController`がModelに`lowStockProducts`を設定

7. **画面表示**
   - `low-stock-products.html`テンプレートが在庫不足商品一覧を表示

### 主要なクラスとメソッド
- **AdminProductController.lowStockProducts()**: 在庫不足商品表示のエントリーポイント
- **AdminProductService.getLowStockProducts()**: 在庫不足商品取得のビジネスロジック
- **AdminProductRepository.findLowStockProducts()**: データベースからの在庫不足商品取得 