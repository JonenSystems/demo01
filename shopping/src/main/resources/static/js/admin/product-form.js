// 管理者商品登録・編集画面用JavaScript

// 画像エラーハンドリング
function handleImageError(img) {
    img.style.display = 'none';
    const placeholder = img.nextElementSibling;
    if (placeholder && placeholder.classList.contains('no-image-placeholder')) {
        placeholder.style.display = 'block';
    }
}

// DOMContentLoadedイベントリスナー
document.addEventListener('DOMContentLoaded', function() {
    // 画像エラーハンドリングの設定
    const productImages = document.querySelectorAll('img[alt="商品画像"]');
    productImages.forEach(img => {
        img.addEventListener('error', function() {
            handleImageError(this);
        });
    });
});
