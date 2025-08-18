# demo01（ショッピングサイト）
Spring Bootによるショッピングサイトの作例です。<br>
ユーザー機能（ログインなし）と管理者機能（ログインあり）があります。<br>
<br>
モダンで使いやすいデザイン表現とあわせ、AIコードエディタ使用により1～2週間での完成を想定した開発フローの検証を目的に製作いたしました。<br>
ソースコードはGitHubで管理し、変更がプッシュされるとJenkinsにより自動で公開されます。<br>
<br>
管理者機能へのログインは ID : admin / password : p@sswordです。<br>
既存データの変更、削除は問題ありませんが、個人データの書き込みにはご注意願います。<br>
<br>
## デモサイト
- ユーザー機能（ログイン不要）：http://133.18.166.208:8080/
- 管理者機能（要ログイン）：http://133.18.166.208:8080/admin/login

## 機能一覧、機能詳細説明書
- [ユーザー機能](documents/user/function-list.md)
- [管理者機能](documents/admin/function-list.md)

## 技術情報
- [技術スタック](documents/technology-stacks.md)
- [環境設定およびdeployに関する情報](documents/application-properties.md)

#### 実装ルール
- [基本的な実装ルール](documents/coding-rules-basic.md)
- [画面仕様書](documents/ui-specifications.md)
