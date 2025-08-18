# 機能一覧

大分類：ユーザー機能

## 表1. 機能一覧

|ID|中分類<br>（機能分類）|機能名|内容|コントローラ|メソッド|画面入力|画面照会|
|---|---|---|---|---|---|---|---|
|1|商品表示|[商品一覧表示](function-specifications/productList.md)|商品情報を一覧表示し、カートに追加できる機能|UserProductController|productList|/|/products|
|2|商品表示|[カテゴリ別商品表示](function-specifications/productListByCategory.md)|指定カテゴリの商品を一覧表示する機能|UserProductController|productListByCategory|/products/category/{category}|/products/category/{category}|
|3|カート管理|[カート内容表示](function-specifications/cart.md)|カート内の商品一覧と合計金額を表示する機能|UserCartController|cart|/cart|/cart|
|4|カート管理|[カート商品追加](function-specifications/addToCart.md)|Ajaxでカートに商品を追加する機能|UserCartController|addToCart|/cart/add||
|5|カート管理|[カート商品削除](function-specifications/removeFromCart.md)|カートから商品を削除する機能|UserCartController|removeFromCart|/cart/remove||
|6|カート管理|[カート商品数量更新](function-specifications/updateCartItemQuantity.md)|カート内の商品数量を更新する機能|UserCartController|updateCartItemQuantity|/cart/update||
|7|カート管理|[カートクリア](function-specifications/clearCart.md)|カート内の全商品を削除する機能|UserCartController|clearCart|/cart/clear||
|8|顧客情報|[顧客情報入力](function-specifications/customerInput.md)|顧客情報を入力する機能|UserCustomerController|customerInput|/customer/input|/customer/input|
|9|顧客情報|[顧客情報確認](function-specifications/customerConfirm.md)|入力された顧客情報を確認する機能|UserCustomerController|customerConfirm|/customer/confirm|/customer/confirm|
|10|注文|[注文確認](function-specifications/checkout.md)|注文内容を確認する機能|UserOrderController|checkout|/order/checkout|/order/checkout|
|11|注文|[注文確定](function-specifications/placeOrder.md)|注文を確定し、データベースに保存する機能|UserOrderController|placeOrder|/order/place||
|12|注文|[注文完了](function-specifications/orderComplete.md)|注文完了画面を表示する機能|UserOrderController|orderComplete|/order/complete|/order/complete|
|  |xxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|||||


----