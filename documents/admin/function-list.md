# 機能一覧

大分類：管理者機能

## 表1. 機能一覧

|ID|中分類（機能分類）|機能名|コントローラ|メソッド|画面入力|画面照会|内容|
|---|---|---|---|---|---|---|---|
|13|認証|[管理者ログイン](function-specifications/showLoginForm.md)|AdminAuthController|showLoginForm|/admin/login|/admin/login|管理者ログイン画面を表示する機能|
|14|ダッシュボード|[管理者ダッシュボード](function-specifications/dashboard.md)|AdminController|dashboard|/admin|/admin|管理者ダッシュボードを表示する機能|
|15|商品管理|[商品一覧表示](function-specifications/productList.md)|AdminProductController|productList|/admin/products|/admin/products|管理者用商品一覧を表示する機能|
|16|商品管理|[商品新規作成](function-specifications/productNew.md)|AdminProductController|productNew|/admin/products/new|/admin/products/new|商品新規作成画面を表示する機能|
|17|商品管理|[商品編集](function-specifications/productEdit.md)|AdminProductController|productEdit|/admin/products/{id}/edit|/admin/products/{id}/edit|商品編集画面を表示する機能|
|18|商品管理|[商品保存](function-specifications/productSave.md)|AdminProductController|productSave|/admin/products/save||商品情報を保存する機能|
|19|商品管理|[商品削除確認](function-specifications/productDeleteConfirm.md)|AdminProductController|productDeleteConfirm|/admin/products/{id}/delete|/admin/products/{id}/delete|商品削除確認画面を表示する機能|
|20|商品管理|[商品削除](function-specifications/productDelete.md)|AdminProductController|productDelete|/admin/products/{id}/delete||商品を削除する機能|
|21|商品管理|[在庫不足商品表示](function-specifications/lowStockProducts.md)|AdminProductController|lowStockProducts|/admin/products/low-stock|/admin/products/low-stock|在庫不足商品を一覧表示する機能|
|22|注文管理|[注文一覧表示](function-specifications/orderList.md)|AdminOrderController|orderList|/admin/orders|/admin/orders|管理者用注文一覧を表示する機能|
|23|注文管理|[注文詳細表示](function-specifications/orderDetail.md)|AdminOrderController|orderDetail|/admin/orders/{id}|/admin/orders/{id}|注文詳細を表示する機能|
|24|注文管理|[注文ステータス更新](function-specifications/updateOrderStatus.md)|AdminOrderController|updateOrderStatus|/admin/orders/{id}/status||注文ステータスを更新する機能|
|25|顧客管理|[顧客一覧表示](function-specifications/customerList.md)|AdminCustomerController|customerList|/admin/customers|/admin/customers|管理者用顧客一覧を表示する機能|
|26|顧客管理|[顧客新規作成](function-specifications/customerNew.md)|AdminCustomerController|customerNew|/admin/customers/new|/admin/customers/new|顧客新規作成画面を表示する機能|
|27|顧客管理|[顧客編集](function-specifications/customerEdit.md)|AdminCustomerController|customerEdit|/admin/customers/{id}/edit|/admin/customers/{id}/edit|顧客編集画面を表示する機能|
|28|顧客管理|[顧客保存](function-specifications/customerSave.md)|AdminCustomerController|customerSave|/admin/customers/save||顧客情報を保存する機能|
|29|顧客管理|[顧客削除確認](function-specifications/customerDeleteConfirm.md)|AdminCustomerController|customerDeleteConfirm|/admin/customers/{id}/delete|/admin/customers/{id}/delete|顧客削除確認画面を表示する機能|
|30|顧客管理|[顧客削除](function-specifications/customerDelete.md)|AdminCustomerController|customerDelete|/admin/customers/{id}/delete||顧客を削除する機能|
|---|---|---|---|---|---|---|xxxxxxxxxxxxxxxxxxxxxxx|



----
