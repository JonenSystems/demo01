package com.example.shopping.repository.impl;

import com.example.shopping.model.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * UserProductRepositoryImplの単体テスト
 */
@ExtendWith(MockitoExtension.class)
class UserProductRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Product> productQuery;

    @Mock
    private TypedQuery<Long> longQuery;

    @InjectMocks
    private UserProductRepositoryImpl userProductRepository;

    private Product testProduct1;
    private Product testProduct2;

    @BeforeEach
    void setUp() {
        testProduct1 = new Product();
        testProduct1.setId(1L);
        testProduct1.setName("テスト商品1");
        testProduct1.setDescription("テスト商品1の説明");
        testProduct1.setPrice(new BigDecimal("1000"));
        testProduct1.setStockQuantity(10);
        testProduct1.setCategory("電子機器");

        testProduct2 = new Product();
        testProduct2.setId(2L);
        testProduct2.setName("テスト商品2");
        testProduct2.setDescription("テスト商品2の説明");
        testProduct2.setPrice(new BigDecimal("2000"));
        testProduct2.setStockQuantity(5);
        testProduct2.setCategory("電子機器");
    }

    @Test
    void UT_Repository_0085_findAllByOrderByIdAsc_正常系() {
        // モックの設定
        List<Product> productList = Arrays.asList(testProduct1, testProduct2);
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(productList);

        // テスト実行
        List<Product> result = userProductRepository.findAllByOrderByIdAsc();

        // 検証
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(entityManager).createQuery(
                "SELECT p FROM Product p ORDER BY p.id ASC", Product.class);
    }

    @Test
    void UT_Repository_0086_findAllByOrderByIdAsc_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(Collections.emptyList());

        // テスト実行
        List<Product> result = userProductRepository.findAllByOrderByIdAsc();

        // 検証
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(entityManager).createQuery(
                "SELECT p FROM Product p ORDER BY p.id ASC", Product.class);
    }

    @Test
    void UT_Repository_0087_findByCategoryOrderByIdAsc_正常系() {
        // モックの設定
        List<Product> productList = Arrays.asList(testProduct1, testProduct2);
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.setParameter(anyString(), any())).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(productList);

        // テスト実行
        List<Product> result = userProductRepository.findByCategoryOrderByIdAsc("電子機器");

        // 検証
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("電子機器", result.get(0).getCategory());
        assertEquals("電子機器", result.get(1).getCategory());

        verify(entityManager).createQuery(
                "SELECT p FROM Product p WHERE p.category = :category ORDER BY p.id ASC", Product.class);
        verify(productQuery).setParameter("category", "電子機器");
    }

    @Test
    void UT_Repository_0088_findByCategoryOrderByIdAsc_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.setParameter(anyString(), any())).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(Collections.emptyList());

        // テスト実行
        List<Product> result = userProductRepository.findByCategoryOrderByIdAsc("存在しないカテゴリ");

        // 検証
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(entityManager).createQuery(
                "SELECT p FROM Product p WHERE p.category = :category ORDER BY p.id ASC", Product.class);
        verify(productQuery).setParameter("category", "存在しないカテゴリ");
    }

    @Test
    void UT_Repository_0089_findAvailableProducts_正常系() {
        // モックの設定
        List<Product> productList = Arrays.asList(testProduct1, testProduct2);
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(productList);

        // テスト実行
        List<Product> result = userProductRepository.findAvailableProducts();

        // 検証
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getStockQuantity() > 0);
        assertTrue(result.get(1).getStockQuantity() > 0);

        verify(entityManager).createQuery(
                "SELECT p FROM Product p WHERE p.stockQuantity > 0 ORDER BY p.id ASC", Product.class);
    }

    @Test
    void UT_Repository_0090_findAvailableProducts_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(Collections.emptyList());

        // テスト実行
        List<Product> result = userProductRepository.findAvailableProducts();

        // 検証
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(entityManager).createQuery(
                "SELECT p FROM Product p WHERE p.stockQuantity > 0 ORDER BY p.id ASC", Product.class);
    }

    @Test
    void UT_Repository_0091_findAvailableProductsByCategory_正常系() {
        // モックの設定
        List<Product> productList = Arrays.asList(testProduct1, testProduct2);
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.setParameter(anyString(), any())).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(productList);

        // テスト実行
        List<Product> result = userProductRepository.findAvailableProductsByCategory("電子機器");

        // 検証
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("電子機器", result.get(0).getCategory());
        assertEquals("電子機器", result.get(1).getCategory());
        assertTrue(result.get(0).getStockQuantity() > 0);
        assertTrue(result.get(1).getStockQuantity() > 0);

        verify(entityManager).createQuery(
                "SELECT p FROM Product p WHERE p.category = :category AND p.stockQuantity > 0 ORDER BY p.id ASC",
                Product.class);
        verify(productQuery).setParameter("category", "電子機器");
    }

    @Test
    void UT_Repository_0092_findAvailableProductsByCategory_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.setParameter(anyString(), any())).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(Collections.emptyList());

        // テスト実行
        List<Product> result = userProductRepository.findAvailableProductsByCategory("存在しないカテゴリ");

        // 検証
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(entityManager).createQuery(
                "SELECT p FROM Product p WHERE p.category = :category AND p.stockQuantity > 0 ORDER BY p.id ASC",
                Product.class);
        verify(productQuery).setParameter("category", "存在しないカテゴリ");
    }

    @Test
    void UT_Repository_0093_findById_正常系() {
        // モックの設定
        when(entityManager.find(Product.class, 1L)).thenReturn(testProduct1);

        // テスト実行
        Optional<Product> result = userProductRepository.findById(1L);

        // 検証
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("テスト商品1", result.get().getName());

        verify(entityManager).find(Product.class, 1L);
    }

    @Test
    void UT_Repository_0094_findById_正常系() {
        // モックの設定
        when(entityManager.find(Product.class, 999L)).thenReturn(null);

        // テスト実行
        Optional<Product> result = userProductRepository.findById(999L);

        // 検証
        assertFalse(result.isPresent());

        verify(entityManager).find(Product.class, 999L);
    }

    @Test
    void UT_Repository_0095_save_正常系() {
        // 新規商品の作成
        Product newProduct = new Product();
        newProduct.setName("新規商品");
        newProduct.setDescription("新規商品の説明");
        newProduct.setPrice(new BigDecimal("3000"));
        newProduct.setStockQuantity(15);
        newProduct.setCategory("食品");

        // モックの設定
        doAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            product.setId(3L);
            return null;
        }).when(entityManager).persist(any(Product.class));

        // テスト実行
        Product result = userProductRepository.save(newProduct);

        // 検証
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("新規商品", result.getName());

        verify(entityManager).persist(newProduct);
        verify(entityManager, never()).merge(any(Product.class));
    }

    @Test
    void UT_Repository_0096_save_正常系() {
        // 既存商品の更新
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("更新商品");
        existingProduct.setDescription("更新商品の説明");
        existingProduct.setPrice(new BigDecimal("4000"));
        existingProduct.setStockQuantity(20);
        existingProduct.setCategory("衣類");

        // モックの設定
        when(entityManager.merge(existingProduct)).thenReturn(existingProduct);

        // テスト実行
        Product result = userProductRepository.save(existingProduct);

        // 検証
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("更新商品", result.getName());

        verify(entityManager).merge(existingProduct);
        verify(entityManager, never()).persist(any(Product.class));
    }

    @Test
    void UT_Repository_0097_deleteById_正常系() {
        // モックの設定
        when(entityManager.find(Product.class, 1L)).thenReturn(testProduct1);

        // テスト実行
        userProductRepository.deleteById(1L);

        // 検証
        verify(entityManager).find(Product.class, 1L);
        verify(entityManager).remove(testProduct1);
    }

    @Test
    void UT_Repository_0098_deleteById_正常系() {
        // モックの設定
        when(entityManager.find(Product.class, 999L)).thenReturn(null);

        // テスト実行
        userProductRepository.deleteById(999L);

        // 検証
        verify(entityManager).find(Product.class, 999L);
        verify(entityManager, never()).remove(any(Product.class));
    }

    @Test
    void UT_Repository_0099_existsById_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(1L);

        // テスト実行
        boolean result = userProductRepository.existsById(1L);

        // 検証
        assertTrue(result);

        verify(entityManager).createQuery(
                "SELECT COUNT(p) FROM Product p WHERE p.id = :id", Long.class);
        verify(longQuery).setParameter("id", 1L);
    }

    @Test
    void UT_Repository_0100_existsById_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(0L);

        // テスト実行
        boolean result = userProductRepository.existsById(999L);

        // 検証
        assertFalse(result);

        verify(entityManager).createQuery(
                "SELECT COUNT(p) FROM Product p WHERE p.id = :id", Long.class);
        verify(longQuery).setParameter("id", 999L);
    }

    @Test
    void UT_Repository_0101_findAll_正常系() {
        // モックの設定
        List<Product> productList = Arrays.asList(testProduct1, testProduct2);
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(productList);

        // テスト実行
        List<Product> result = userProductRepository.findAll();

        // 検証
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("テスト商品1", result.get(0).getName());
        assertEquals("テスト商品2", result.get(1).getName());

        verify(entityManager).createQuery(
                "SELECT p FROM Product p", Product.class);
    }

    @Test
    void UT_Repository_0102_findAll_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(Collections.emptyList());

        // テスト実行
        List<Product> result = userProductRepository.findAll();

        // 検証
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(entityManager).createQuery(
                "SELECT p FROM Product p", Product.class);
    }
}