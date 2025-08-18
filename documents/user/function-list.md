# 機能一覧

## 表1. 機能一覧

|ID|大分類（サブシステム）|中分類（機能分類）|機能名|コントローラ|メソッド|画面入力|画面照会|帳票出力|バッチ処理|内容|
|---|---|---|---|---|---|---|---|---|---|---|
|1|ユーザー機能|商品表示|商品一覧表示|UserProductController|productList|/|/products| | |商品情報を一覧表示し、カートに追加できる機能|
|2|ユーザー機能|商品表示|カテゴリ別商品表示|UserProductController|productListByCategory|/products/category/{category}|/products/category/{category}| | |指定カテゴリの商品を一覧表示する機能|
|3|ユーザー機能|カート管理|カート内容表示|UserCartController|cart|/cart|/cart| | |カート内の商品一覧と合計金額を表示する機能|
|4|ユーザー機能|カート管理|カート商品追加|UserCartController|addToCart|/cart/add| | | |Ajaxでカートに商品を追加する機能|
|5|ユーザー機能|カート管理|カート商品削除|UserCartController|removeFromCart|/cart/remove| | | |カートから商品を削除する機能|
|6|ユーザー機能|カート管理|カート商品数量更新|UserCartController|updateCartItemQuantity|/cart/update| | | |カート内の商品数量を更新する機能|
|7|ユーザー機能|カート管理|カートクリア|UserCartController|clearCart|/cart/clear| | | |カート内の全商品を削除する機能|
|8|ユーザー機能|顧客情報|顧客情報入力|UserCustomerController|customerInput|/customer/input|/customer/input| | |顧客情報を入力する機能|
|9|ユーザー機能|顧客情報|顧客情報確認|UserCustomerController|customerConfirm|/customer/confirm|/customer/confirm| | |入力された顧客情報を確認する機能|
|10|ユーザー機能|注文|注文確認|UserOrderController|checkout|/order/checkout|/order/checkout| | |注文内容を確認する機能|
|11|ユーザー機能|注文|注文確定|UserOrderController|placeOrder|/order/place| | | |注文を確定し、データベースに保存する機能|
|12|ユーザー機能|注文|注文完了|UserOrderController|orderComplete|/order/complete|/order/complete| | |注文完了画面を表示する機能|

----