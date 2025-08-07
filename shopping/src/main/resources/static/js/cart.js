// カート画面用JavaScript

// 画像エラーハンドリング
function handleImageError(img) {
    img.style.display = 'none';
    const placeholder = img.nextElementSibling;
    if (placeholder && placeholder.classList.contains('no-image-placeholder')) {
        placeholder.style.display = 'flex';
    }
}

// 商品削除確認
function confirmDelete() {
    return confirm('この商品をカートから削除しますか？');
}

// カートクリア確認
function confirmClear() {
    return confirm('カートをクリアしますか？');
}

// DOMContentLoadedイベントリスナー
document.addEventListener('DOMContentLoaded', function() {
    // 画像エラーハンドリングの設定
    const productImages = document.querySelectorAll('.product-image');
    productImages.forEach(img => {
        img.addEventListener('error', function() {
            handleImageError(this);
        });
    });
    
    // 削除ボタンの確認ダイアログ設定
    const deleteButtons = document.querySelectorAll('form[action*="/cart/remove"] button[type="submit"]');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirmDelete()) {
                e.preventDefault();
            }
        });
    });
    
    // クリアボタンの確認ダイアログ設定
    const clearButtons = document.querySelectorAll('form[action*="/cart/clear"] button[type="submit"]');
    clearButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirmClear()) {
                e.preventDefault();
            }
        });
    });
});
