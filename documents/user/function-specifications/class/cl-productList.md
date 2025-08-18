# クラス図

## 商品一覧表示

```mermaid
classDiagram
    class UserProductController {
        -UserProductService userProductService
        -SessionCart sessionCart
        +productList(category, model, session) String
    }
    
    class UserProductService {
        <<interface>>
        +getAllProducts() List~UserProductDto~
        +getProductsByCategory(category) List~UserProductDto~
    }
    
    class UserProductServiceImpl {
        -UserProductRepository userProductRepository
        +getAllProducts() List~UserProductDto~
        +getProductsByCategory(category) List~UserProductDto~
        -convertToDto(product) UserProductDto
    }
    
    class UserProductRepository {
        <<interface>>
        +findAvailableProducts() List~Product~
        +findAvailableProductsByCategory(category) List~Product~
    }
    
    class UserProductListForm {
        -List~UserProductDto~ products
        -String selectedCategory
        -List~String~ categories
        +hasProducts() boolean
    }
    
    class UserProductDto {
        -Long id
        -String name
        -String description
        -BigDecimal price
        -String category
        -Integer stockQuantity
        -String imagePath
        +fromEntity(product) UserProductDto
        +toEntity() Product
    }
    
    class SessionCart {
        <<interface>>
        +getCartItemCount(session) int
    }
    
    class Product {
        -Long id
        -String name
        -String description
        -BigDecimal price
        -String category
        -Integer stockQuantity
        -String imagePath
    }

    UserProductController --> UserProductService : uses
    UserProductController --> SessionCart : uses
    UserProductController --> UserProductListForm : creates
    UserProductService <|.. UserProductServiceImpl : implements
    UserProductServiceImpl --> UserProductRepository : uses
    UserProductServiceImpl --> UserProductDto : creates
    UserProductListForm --> UserProductDto : contains
    UserProductDto --> Product : converts
```

## クラス図の解説

### クラス間の関係

1. **UserProductController**
   - `UserProductService`を使用して商品情報を取得
   - `SessionCart`を使用してカート情報を取得
   - `UserProductListForm`を作成してビューに渡す

2. **UserProductService**
   - 商品情報取得のビジネスロジックを定義するインターフェース
   - `UserProductServiceImpl`が実装を提供

3. **UserProductServiceImpl**
   - `UserProductRepository`を使用してデータベースから商品情報を取得
   - `Product`エンティティを`UserProductDto`に変換

4. **UserProductRepository**
   - 商品情報のデータアクセスを定義するインターフェース
   - 利用可能な商品の取得メソッドを提供

5. **UserProductListForm**
   - 商品一覧画面で使用するフォームクラス
   - `UserProductDto`のリストを保持

6. **UserProductDto**
   - 商品情報を転送するためのDTOクラス
   - `Product`エンティティとの相互変換メソッドを提供

7. **SessionCart**
   - セッション内のカート情報を管理するインターフェース
   - カートアイテム数の取得メソッドを提供

8. **Product**
   - 商品エンティティクラス
   - データベースの商品テーブルに対応

### 処理フロー

1. ユーザーが商品一覧画面にアクセス
2. `UserProductController.productList()`が呼び出される
3. `UserProductService.getAllProducts()`または`getProductsByCategory()`で商品情報を取得
4. `UserProductServiceImpl`が`UserProductRepository`からデータを取得
5. `Product`エンティティを`UserProductDto`に変換
6. `UserProductListForm`を作成して商品リストを設定
7. `SessionCart.getCartItemCount()`でカート情報を取得
8. `product-list.html`テンプレートを返す 