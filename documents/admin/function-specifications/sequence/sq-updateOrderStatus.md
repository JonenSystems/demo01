# シーケンス図_注文ステータス更新

## シーケンス図

```mermaid
sequenceDiagram
    actor ユーザー
    participant view as View<br/>order-detail.html
    participant AdminOrderController as Controller<br/>AdminOrderController
    participant AdminOrderService as Service<br/>AdminOrderService<br/>AdminOrderServiceImpl
    participant AdminOrderRepository as Repository<br/>AdminOrderRepository<br/>AdminOrderRepositoryImpl
    participant Order as Entity<br/>Order
    participant DB as DB<br/>H2<br/>PUBLIC<br/>orders

    ユーザー->>view: 注文ステータス更新要求
    view->>AdminOrderController: POST /admin/orders/{orderId}/status
    AdminOrderController->>AdminOrderService: updateOrderStatus(orderId, status)
    AdminOrderService->>AdminOrderRepository: findById(orderId)
    AdminOrderRepository->>DB: SELECT * FROM orders WHERE id = ?
    DB-->>AdminOrderRepository: 注文データ
    AdminOrderRepository-->>AdminOrderService: Order
    AdminOrderService->>AdminOrderService: setStatus(status)
    AdminOrderService->>AdminOrderRepository: save(Order)
    AdminOrderRepository->>DB: UPDATE orders SET status = ? WHERE id = ?
    DB-->>AdminOrderRepository: 更新結果
    AdminOrderRepository-->>AdminOrderService: Order
    AdminOrderService-->>AdminOrderController: boolean
    AdminOrderController->>view: redirect:/admin/orders/{orderId}
    view-->>ユーザー: 注文詳細画面にリダイレクト

    Note over DB: データベース<br/>H2<br/>PUBLIC<br/>orders
```

## シーケンス図の解説

### 処理フロー
1. **ユーザーが注文ステータス更新を要求**
   - ユーザーが注文詳細画面でステータスを変更して送信

2. **ViewからControllerへのリクエスト**
   - `order-detail.html`から`AdminOrderController`の`updateOrderStatus`メソッドにPOSTリクエスト
   - 注文IDと新しいステータスをパラメータとして受け取る

3. **ControllerからServiceへの処理委譲**
   - `AdminOrderController`が`AdminOrderService`の`updateOrderStatus`メソッドを呼び出し
   - 注文IDと新しいステータスを渡す

4. **ServiceからRepositoryへのデータ取得**
   - `AdminOrderServiceImpl`が`AdminOrderRepository`の`findById`メソッドを呼び出し
   - 更新対象の注文データを取得

5. **データベースアクセス（取得）**
   - `AdminOrderRepositoryImpl`がH2データベースのordersテーブルから注文データを取得

6. **ステータス更新処理**
   - `AdminOrderService`が注文エンティティのステータスを更新

7. **データベースアクセス（更新）**
   - `AdminOrderRepositoryImpl`がH2データベースのordersテーブルを更新
   - 新しいステータスで注文データを保存

8. **処理結果の返却**
   - 更新処理の成功/失敗をboolean値で返却

9. **リダイレクト処理**
   - `AdminOrderController`が注文詳細画面にリダイレクト
   - 成功/失敗メッセージをFlash属性に設定

10. **画面表示**
    - 注文詳細画面が再表示され、更新結果が表示される

### 主要なクラスとメソッド
- **AdminOrderController.updateOrderStatus()**: 注文ステータス更新のエントリーポイント
- **AdminOrderService.updateOrderStatus()**: 注文ステータス更新のビジネスロジック
- **AdminOrderRepository.save()**: データベースへの注文データ保存
- **Order.setStatus()**: 注文エンティティのステータス更新 