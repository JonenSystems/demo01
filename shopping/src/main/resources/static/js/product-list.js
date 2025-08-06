// 商品一覧画面用JavaScript

// カート追加機能（Ajax + トースト通知）
document.addEventListener('DOMContentLoaded', function() {
    const addToCartForms = document.querySelectorAll('.add-to-cart-form');
    
    addToCartForms.forEach(form => {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const formData = new FormData(form);
            const productId = formData.get('productId');
            const quantity = formData.get('quantity');
            const button = form.querySelector('.add-to-cart-btn');
            const originalText = button.textContent;
            
            // ボタンを無効化
            button.disabled = true;
            button.textContent = '追加中...';
            button.classList.add('loading');
            
            // Ajax送信
            fetch('/cart/add', {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showToast('カートに追加しました', 'success');
                } else {
                    showToast('追加に失敗しました', 'error');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showToast('追加に失敗しました', 'error');
            })
            .finally(() => {
                // ボタンを元に戻す
                button.disabled = false;
                button.textContent = originalText;
                button.classList.remove('loading');
            });
        });
    });
    
    // 画像エラーハンドリング
    const productImages = document.querySelectorAll('.product-image[data-has-image="true"]');
    productImages.forEach(img => {
        img.addEventListener('error', function() {
            handleImageError(this);
        });
    });
});

// トースト通知表示
function showToast(message, type) {
    const toast = document.getElementById('toast');
    if (toast) {
        toast.textContent = message;
        toast.className = `toast toast-${type}`;
        toast.style.display = 'block';
        
        // 3秒後に自動で消える
        setTimeout(() => {
            toast.style.display = 'none';
        }, 3000);
    }
}

// 画像エラーハンドリング
function handleImageError(img) {
    img.style.display = 'none';
    const placeholder = img.nextElementSibling;
    if (placeholder && placeholder.classList.contains('no-image-placeholder')) {
        placeholder.style.display = 'flex';
    }
}
