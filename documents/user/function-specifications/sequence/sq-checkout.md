# シーケンス図_注文確認

## 概要
注文確認機能のシーケンス図です。注文内容を確認する際の処理フローを示します。

## シーケンス図

```mermaid
sequenceDiagram
    actor ユーザー
    participant view as View<br/>user/order-confirm.html
    participant UserOrderConfirmForm as Form<br/>UserOrderConfirmForm
    participant UserOrderController as Controller<br/>UserOrderController
    participant UserOrderDto as DTO<br/>UserOrderDto
    participant UserCartItemDto as DTO<br/>UserCartItemDto
    participant UserCustomerDto as DTO<br/>UserCustomerDto
    participant HttpSession as HttpSession
    participant SessionCart as Common<br/>SessionCart<br/>SessionCartImpl
    participant SessionCustomer as Common<br/>SessionCustomer<br/>SessionCustomerImpl
    participant UserOrderService as Service<br/>UserOrderService<br/>UserOrderServiceImpl
    participant UserCartService as Service<br/>UserCartService<br/>UserCartServiceImpl

    ユーザー->>view: 注文確認画面アクセス<br/>GET /order/checkout
    view->>UserOrderController: checkout()
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
        UserOrderController->>UserOrderService: createOrderConfirmData(cartItems, customerDto)
        UserOrderService->>UserOrderService: 注文確認データ作成
        UserOrderService-->>UserOrderController: UserOrderDto
        UserOrderController->>UserOrderConfirmForm: setCartItems()
        UserOrderController->>UserOrderConfirmForm: setTotalAmount()
        UserOrderController->>UserOrderConfirmForm: setCustomer()
        UserOrderController->>SessionCart: getCartItemCount(session)
        SessionCart->>HttpSession: セッションからカートアイテム数取得
        HttpSession-->>SessionCart: カートアイテム数
        SessionCart-->>UserOrderController: カートアイテム数
        UserOrderController->>view: Model設定
        view-->>ユーザー: 注文確認画面表示
    end
```

## 解説

### 処理フロー
1. **ユーザーアクセス**: ユーザーが注文確認画面（`/order/checkout`）にアクセス
2. **コントローラー処理**: `UserOrderController.checkout()`メソッドが実行される
3. **カート確認**: `SessionCart.isCartEmpty()`でカートが空かチェック
4. **カート空判定**: カートが空の場合は商品一覧にリダイレクト
5. **カートアイテム取得**: `SessionCart.getCartItems()`でセッションからカートアイテムを取得
6. **商品情報設定**: `UserCartService.enrichCartItemsWithProductInfo()`でカートアイテムに商品詳細情報を設定
7. **顧客情報取得**: `SessionCustomer.getCustomerForm()`でセッションから顧客情報を取得
8. **注文確認データ作成**: `UserOrderService.createOrderConfirmData()`で注文確認用のデータを作成
9. **フォーム設定**: `UserOrderConfirmForm`にカートアイテム、合計金額、顧客情報を設定
10. **カート情報取得**: `SessionCart.getCartItemCount()`でカートアイテム数を取得
11. **画面表示**: 注文確認画面を表示

### 主要なクラスと役割
- **UserOrderController**: リクエストを受け取り、注文確認処理を統括
- **UserOrderService**: 注文確認データの作成を担当
- **UserCartService**: カートアイテムに商品情報を設定
- **SessionCart**: セッション内のカート情報を管理
- **SessionCustomer**: セッション内の顧客情報を管理
- **UserOrderConfirmForm**: 注文確認用のフォームデータを保持
- **UserOrderDto**: 注文情報のデータ転送オブジェクト
- **HttpSession**: カート情報と顧客情報の永続化

### 特徴
- カートの状態に応じた画面遷移制御
- セッション管理によるカート情報と顧客情報の統合
- 商品情報の動的取得と設定
- 注文確認データの統合作成
- 適切なエラーハンドリング 