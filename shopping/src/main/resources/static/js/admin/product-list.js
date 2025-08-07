// 管理者商品一覧画面用JavaScript

// 画像エラーハンドリング
function handleImageError(img) {
    img.style.display = 'none';
    const placeholder = img.nextElementSibling;
    if (placeholder && placeholder.classList.contains('no-image-placeholder')) {
        placeholder.style.display = 'flex';
    }
}

// DOMContentLoadedイベントリスナー
document.addEventListener('DOMContentLoaded', function() {
    // 画像エラーハンドリングの設定
    const productImages = document.querySelectorAll('img[style*="object-fit: cover"]');
    productImages.forEach(img => {
        img.addEventListener('error', function() {
            handleImageError(this);
        });
    });
});
