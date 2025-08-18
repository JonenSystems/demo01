# シーケンス図_注文詳細表示

## シーケンス図

```mermaid
sequenceDiagram
    actor ユーザー
    participant view as View<br/>order-detail.html
    participant AdminOrderController as Controller<br/>AdminOrderController
    participant AdminOrderService as Service<br/>AdminOrderService<br/>AdminOrderServiceImpl
    participant AdminOrderRepository as Repository<br/>AdminOrderRepository<br/>AdminOrderRepositoryImpl
    participant AdminOrderDto as DTO<br/>AdminOrderDto
    participant Order as Entity<br/>Order
    participant DB as DB<br/>H2<br/>PUBLIC<br/>orders

    ユーザー->>view: 注文詳細画面表示要求
    view->>AdminOrderController: GET /admin/orders/{orderId}
    AdminOrderController->>AdminOrderService: getOrderById(orderId)
    AdminOrderService->>AdminOrderRepository: findById(orderId)
    AdminOrderRepository->>DB: SELECT * FROM orders WHERE id = ?
    DB-->>AdminOrderRepository: 注文データ
    AdminOrderRepository-->>AdminOrderService: Order
    AdminOrderService->>AdminOrderService: convertToDto(Order)
    AdminOrderService-->>AdminOrderController: AdminOrderDto
    AdminOrderController->>AdminOrderService: getAvailableStatuses()
    AdminOrderService-->>AdminOrderController: List<String>
    AdminOrderController->>view: model.addAttribute("orderDto", orderDto)<br/>model.addAttribute("statusOptions", statusOptions)
    view-->>ユーザー: 注文詳細画面表示

    Note over DB: データベース<br/>H2<br/>PUBLIC<br/>orders
```

## シーケンス図の解説

### 処理フロー
1. **ユーザーが注文詳細画面表示を要求**
   - ユーザーが特定の注文の詳細画面にアクセス

2. **ViewからControllerへのリクエスト**
   - `order-detail.html`から`AdminOrderController`の`orderDetail`メソッドにGETリクエスト
   - 注文IDをパスパラメータとして受け取る

3. **ControllerからServiceへの処理委譲**
   - `AdminOrderController`が`AdminOrderService`の`getOrderById`メソッドを呼び出し
   - 指定された注文IDで注文データを取得

4. **ServiceからRepositoryへのデータ取得**
   - `AdminOrderServiceImpl`が`AdminOrderRepository`の`findById`メソッドを呼び出し
   - データベースから特定の注文データを取得

5. **データベースアクセス**
   - `AdminOrderRepositoryImpl`がH2データベースのordersテーブルにクエリを実行
   - 指定されたIDの注文データを取得

6. **EntityからDTOへの変換**
   - 取得した`Order`エンティティを`AdminOrderDto`に変換

7. **ステータスオプションの取得**
   - `AdminOrderService`が`getAvailableStatuses`メソッドを呼び出し
   - 利用可能な注文ステータスの一覧を取得

8. **Viewへのデータ設定**
   - `AdminOrderController`がModelに`orderDto`と`statusOptions`を設定

9. **画面表示**
   - `order-detail.html`テンプレートが注文詳細とステータスオプションを表示

### 主要なクラスとメソッド
- **AdminOrderController.orderDetail()**: 注文詳細表示のエントリーポイント
- **AdminOrderService.getOrderById()**: 注文詳細取得のビジネスロジック
- **AdminOrderRepository.findById()**: データベースからの特定注文データ取得
- **AdminOrderDto**: 注文詳細情報を管理するDTOクラス 