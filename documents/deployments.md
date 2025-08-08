## 概要
各機能と環境設定は完全に分離しており、サービス起動時に指定するオプションにて開発環境または本番環境の設定ファイルが共通設定ファイルを上書きする。

## 環境ごとの方式及び設定の違い
| 分類名 | 項目名 | 開発環境 | 本番環境 |
|---|---|---|---|
| **サーバリリース** | サーバOS | Windows（ローカル）| Rockey Linux 9 |
| **サーバリリース** | Java | 17 | 17 |
| **サーバリリース** | deploy方式 | 手動 | Jenkinsによる自動deploy |
| **データベース** | データベース種類 | H2（インメモリ） | MySQL 8.0 |
| **データベース** | ドライバークラス | `org.h2.Driver` | `com.mysql.cj.jdbc.Driver` |
| **データベース** | プラットフォーム | `org.hibernate.dialect.H2Dialect` | `org.hibernate.dialect.MySQLDialect` |
| **データベース** | DDL自動生成 | `create-drop` | `validate` |
| **データベース** | SQL表示 | `true` | `false` |
| **データベース** | コネクションプール | 未設定 | `maximum-pool-size=10, minimum-idle=5, connection-timeout=30000` |
| **データベース** | H2コンソール | 有効（`/h2-console`） | 無効 |
| **データベース** | 初期データ読み込み | 自動（`always`） | 手動（コメントアウト） |
| **データベース** | 初期データファイル | `classpath:data-h2.sql` | `classpath:data-mysql.sql`（コメントアウト） |
| **テンプレート** | Thymeleafキャッシュ | `false` | `true` |
| **セッション** | Cookie Secure | `false` | `false` |
| **セッション** | SameSite | `lax` | `strict` |
| **セッション** | Max Age | `3600`秒 | `1800`秒 |
| **ログ** | ルートログレベル | `INFO` | `WARN` |
| **ログ** | アプリケーションログレベル | `DEBUG` | `INFO` |
| **ログ** | Spring Securityログレベル | `DEBUG` | `INFO` |
| **ログ** | ログファイルパス | `logs/application.log` | `/opt/shopping/logs/application.log` |
| **ログ** | ログパターン | `%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n` | `%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n` |
| **監視** | Actuatorエンドポイント | `health,info,h2-console` | `health,info` |
| **監視** | ヘルスチェック詳細 | `always` | `never` |
| **ファイル管理** | アップロードディレクトリ | `src/main/resources/static/images/product-images` | `/opt/shopping/static/images/product-images` |
| **ファイル管理** | ログディレクトリ | `logs` | `/opt/shopping/logs` |
| **セキュリティ** | 環境変数使用 | なし | `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` |
| **パフォーマンス** | 静的リソースキャッシュ | 無効 | 無効（`chain.cache=false`） |
| **開発支援** | 詳細ログ出力 | 多数のDEBUG/TRACEログ | 最小限のログ出力 |
