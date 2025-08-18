# シーケンス図_注文確定

## 概要
注文確定機能のシーケンス図です。注文を確定し、データベースに保存する際の処理フローを示します。

## シーケンス図

```mermaid
sequenceDiagram
    actor ユーザー
    participant view as View<br/>user/order-confirm.html
    participant UserOrderController as Controller<br/>UserOrderController
    participant UserOrderDto as DTO<br/>UserOrderDto
    participant UserCartItemDto as DTO<br/>UserCartItemDto
    participant UserCustomerDto as DTO<br/>UserCustomerDto
    participant HttpSession as HttpSession
    participant SessionCart as Common<br/>SessionCart<br/>SessionCartImpl
    participant SessionCustomer as Common<br/>SessionCustomer<br/>SessionCustomerImpl
    participant SessionOrder as Common<br/>SessionOrder<br/>SessionOrderImpl
    participant UserOrderService as Service<br/>UserOrderService<br/>UserOrderServiceImpl
    participant UserCartService as Service<br/>UserCartService<br/>UserCartServiceImpl
    participant UserOrderRepository as Repository<br/>UserOrderRepository<br/>UserOrderRepositoryImpl
    participant UserOrderDetailRepository as Repository<br/>UserOrderDetailRepository<br/>UserOrderDetailRepositoryImpl
    participant Order as Entity<br/>Order
    participant OrderDetail as Entity<br/>OrderDetail
    participant DB as DB<br/>H2 Database<br/>orders, order_details

    ユーザー->>view: 注文確定ボタンクリック<br/>POST /order/confirm
    view->>UserOrderController: confirmOrder()
    UserOrderController->>SessionCart: isCartEmpty(session)
    SessionCart->>HttpSession: セッションからカート状態確認
    HttpSession-->>SessionCart: カート状態
    SessionCart-->>UserOrderController: カート空判定
    alt カートが空
        UserOrderController->>view: 商品一覧にリダイレクト
        view-->>ユーザー: 商品一覧画面表示
    else カートに商品がある
        UserOrderController->>SessionCart: getCartItems(session)
        SessionCart->>HttpSession: セッションからカートアイテム取得
        HttpSession-->>SessionCart: List<UserCartItemDto>
        SessionCart-->>UserOrderController: カートアイテムリスト
        UserOrderController->>UserCartService: enrichCartItemsWithProductInfo(cartItems)
        UserCartService->>UserCartService: カートアイテムに商品情報を設定
        UserCartService-->>UserOrderController: 商品情報が設定されたカートアイテムリスト
        UserOrderController->>SessionCustomer: getCustomerForm(session)
        SessionCustomer->>HttpSession: セッションから顧客情報取得
        HttpSession-->>SessionCustomer: UserCustomerForm
        SessionCustomer-->>UserOrderController: 顧客情報フォーム
        alt 顧客情報が存在しない
            UserOrderController->>view: 顧客情報入力画面にリダイレクト
            view-->>ユーザー: 顧客情報入力画面表示
        else 顧客情報が存在
            UserOrderController->>UserCustomerForm: toDto()
            UserCustomerForm-->>UserOrderController: UserCustomerDto
            UserOrderController->>UserOrderService: saveOrderToDatabase(cartItems, customerDto)
            UserOrderService->>UserOrderService: 注文データ作成
            UserOrderService->>UserOrderRepository: save(order)
            UserOrderRepository->>DB: INSERT INTO orders<br/>(customer_name, customer_email, total_amount, order_date)
            Note right of DB: データベース<br/>H2 Database<br/>orders
            DB-->>UserOrderRepository: 注文ID
            UserOrderRepository-->>UserOrderService: Order
            loop 各カートアイテム
                UserOrderService->>UserOrderDetailRepository: save(orderDetail)
                UserOrderDetailRepository->>DB: INSERT INTO order_details<br/>(order_id, product_id, quantity, unit_price)
                Note right of DB: データベース<br/>H2 Database<br/>order_details
                DB-->>UserOrderDetailRepository: 注文詳細ID
                UserOrderDetailRepository-->>UserOrderService: OrderDetail
            end
            UserOrderService-->>UserOrderController: UserOrderDto
            alt 注文保存成功
                UserOrderController->>SessionOrder: setOrder(orderDto, session)
                SessionOrder->>HttpSession: セッションに注文情報保存
                HttpSession-->>SessionOrder: 保存完了
                SessionOrder-->>UserOrderController: 保存完了
                UserOrderController->>SessionCart: clearCart(session)
                SessionCart->>HttpSession: セッションからカート情報クリア
                HttpSession-->>SessionCart: クリア完了
                SessionCart-->>UserOrderController: クリア完了
                UserOrderController->>view: 注文完了画面にリダイレクト
                view-->>ユーザー: 注文完了画面表示
            else 注文保存失敗
                UserOrderController->>view: 注文確認画面にリダイレクト（エラーメッセージ）
                view-->>ユーザー: エラーメッセージ付き注文確認画面表示
            end
        end
    end
```

## 解説

### 処理フロー
1. **ユーザーアクション**: ユーザーが注文確認画面で「注文確定」ボタンをクリック
2. **POSTリクエスト**: 注文確定処理をPOSTで送信（`/order/confirm`）
3. **コントローラー処理**: `UserOrderController.confirmOrder()`メソッドが実行される
4. **カート確認**: `SessionCart.isCartEmpty()`でカートが空かチェック
5. **カート空判定**: カートが空の場合は商品一覧にリダイレクト
6. **カートアイテム取得**: `SessionCart.getCartItems()`でセッションからカートアイテムを取得
7. **商品情報設定**: `UserCartService.enrichCartItemsWithProductInfo()`でカートアイテムに商品詳細情報を設定
8. **顧客情報取得**: `SessionCustomer.getCustomerForm()`でセッションから顧客情報を取得
9. **顧客情報確認**: 顧客情報が存在しない場合は顧客情報入力画面にリダイレクト
10. **DTO変換**: `UserCustomerForm.toDto()`でフォームをDTOに変換
11. **注文保存**: `UserOrderService.saveOrderToDatabase()`で注文をデータベースに保存
12. **注文テーブル保存**: `UserOrderRepository.save()`で注文情報をordersテーブルに保存
13. **注文詳細保存**: 各カートアイテムを`UserOrderDetailRepository.save()`でorder_detailsテーブルに保存
14. **セッション更新**: 注文情報をセッションに保存し、カートをクリア
15. **画面遷移**: 成功時は注文完了画面、失敗時はエラーメッセージ付き注文確認画面に遷移

### 主要なクラスと役割
- **UserOrderController**: リクエストを受け取り、注文確定処理を統括
- **UserOrderService**: 注文データの作成とデータベース保存を担当
- **UserCartService**: カートアイテムに商品情報を設定
- **SessionCart**: セッション内のカート情報を管理
- **SessionCustomer**: セッション内の顧客情報を管理
- **SessionOrder**: セッション内の注文情報を管理
- **UserOrderRepository**: 注文情報のデータベースアクセスを担当
- **UserOrderDetailRepository**: 注文詳細情報のデータベースアクセスを担当
- **Order**: 注文エンティティ
- **OrderDetail**: 注文詳細エンティティ
- **HttpSession**: カート情報、顧客情報、注文情報の永続化

### 特徴
- トランザクション管理による注文データの整合性確保
- セッション管理による注文情報の永続化
- カート情報の自動クリア
- エラーハンドリングによる適切なフィードバック
- データベースへの永続化処理 