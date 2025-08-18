# シーケンス図_顧客削除確認

## シーケンス図

```mermaid
sequenceDiagram
    actor ユーザー
    participant view as View<br/>customer-delete-confirm.html
    participant AdminCustomerController as Controller<br/>AdminCustomerController
    participant AdminCustomerService as Service<br/>AdminCustomerService<br/>AdminCustomerServiceImpl
    participant AdminCustomerRepository as Repository<br/>AdminCustomerRepository<br/>AdminCustomerRepositoryImpl
    participant AdminCustomerForm as Form<br/>AdminCustomerForm
    participant AdminCustomerDto as DTO<br/>AdminCustomerDto
    participant Customer as Entity<br/>Customer
    participant DB as DB<br/>H2<br/>PUBLIC<br/>customers

    ユーザー->>view: 顧客削除確認画面表示要求
    view->>AdminCustomerController: GET /admin/customers/{customerId}/delete
    AdminCustomerController->>AdminCustomerService: getCustomerById(customerId)
    AdminCustomerService->>AdminCustomerRepository: findById(customerId)
    AdminCustomerRepository->>DB: SELECT * FROM customers WHERE id = ?
    DB-->>AdminCustomerRepository: 顧客データ
    AdminCustomerRepository-->>AdminCustomerService: Customer
    AdminCustomerService->>AdminCustomerService: convertToDto(Customer)
    AdminCustomerService-->>AdminCustomerController: AdminCustomerDto
    AdminCustomerController->>AdminCustomerForm: fromDto(AdminCustomerDto)
    AdminCustomerForm-->>AdminCustomerController: AdminCustomerForm
    AdminCustomerController->>view: model.addAttribute("customerForm", customerForm)
    view-->>ユーザー: 顧客削除確認画面表示

    Note over DB: データベース<br/>H2<br/>PUBLIC<br/>customers
```

## シーケンス図の解説

### 処理フロー
1. **ユーザーが顧客削除確認画面表示を要求**
   - ユーザーが特定の顧客の削除確認画面にアクセス

2. **ViewからControllerへのリクエスト**
   - `customer-delete-confirm.html`から`AdminCustomerController`の`customerDeleteConfirm`メソッドにGETリクエスト
   - 顧客IDをパスパラメータとして受け取る

3. **ControllerからServiceへの処理委譲**
   - `AdminCustomerController`が`AdminCustomerService`の`getCustomerById`メソッドを呼び出し
   - 指定された顧客IDで顧客データを取得

4. **ServiceからRepositoryへのデータ取得**
   - `AdminCustomerServiceImpl`が`AdminCustomerRepository`の`findById`メソッドを呼び出し
   - データベースから特定の顧客データを取得

5. **データベースアクセス**
   - `AdminCustomerRepositoryImpl`がH2データベースのcustomersテーブルにクエリを実行
   - 指定されたIDの顧客データを取得

6. **EntityからDTOへの変換**
   - 取得した`Customer`エンティティを`AdminCustomerDto`に変換

7. **DTOからFormへの変換**
   - `AdminCustomerController`が`AdminCustomerForm.fromDto`メソッドを呼び出し
   - DTOからフォームオブジェクトに変換

8. **Viewへのデータ設定**
   - `AdminCustomerController`がModelに`customerForm`を設定

9. **画面表示**
   - `customer-delete-confirm.html`テンプレートが顧客削除確認フォームを表示

### 主要なクラスとメソッド
- **AdminCustomerController.customerDeleteConfirm()**: 顧客削除確認画面表示のエントリーポイント
- **AdminCustomerService.getCustomerById()**: 顧客詳細取得のビジネスロジック
- **AdminCustomerRepository.findById()**: データベースからの特定顧客データ取得
- **AdminCustomerForm.fromDto()**: DTOからフォームへの変換メソッド 