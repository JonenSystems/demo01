// 注文確認画面用JavaScript

// 注文確定確認
function confirmOrder() {
    return confirm('この内容で注文を確定しますか？');
}

// DOMContentLoadedイベントリスナー
document.addEventListener('DOMContentLoaded', function() {
    // 注文確定ボタンの確認ダイアログ設定
    const confirmButtons = document.querySelectorAll('form[action*="/order/confirm"] button[type="submit"]');
    confirmButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirmOrder()) {
                e.preventDefault();
            }
        });
    });
});
