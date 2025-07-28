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
 * UserCartRepositoryImplの単体テスト
 */
@ExtendWith(MockitoExtension.class)
class UserCartRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Product> productQuery;

    @Mock
    private TypedQuery<Long> longQuery;

    @InjectMocks
    private UserCartRepositoryImpl userCartRepository;

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
    void UT_Repository_0103_findByIdAndStockQuantityGreaterThan_正常系() {
        // モックの設定
        List<Product> productList = Arrays.asList(testProduct1);
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.setParameter(anyString(), any())).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(productList);

        // テスト実行
        Optional<Product> result = userCartRepository.findByIdAndStockQuantityGreaterThan(1L, 5);

        // 検証
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertTrue(result.get().getStockQuantity() > 5);

        verify(entityManager).createQuery(
                "SELECT p FROM Product p WHERE p.id = :id AND p.stockQuantity > :stockQuantity", Product.class);
        verify(productQuery).setParameter("id", 1L);
        verify(productQuery).setParameter("stockQuantity", 5);
    }

    @Test
    void UT_Repository_0104_findByIdAndStockQuantityGreaterThan_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.setParameter(anyString(), any())).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(Collections.emptyList());

        // テスト実行
        Optional<Product> result = userCartRepository.findByIdAndStockQuantityGreaterThan(1L, 100);

        // 検証
        assertFalse(result.isPresent());

        verify(entityManager).createQuery(
                "SELECT p FROM Product p WHERE p.id = :id AND p.stockQuantity > :stockQuantity", Product.class);
        verify(productQuery).setParameter("id", 1L);
        verify(productQuery).setParameter("stockQuantity", 100);
    }

    @Test
    void UT_Repository_0105_findByIdAndStockQuantityGreaterThan_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.setParameter(anyString(), any())).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(Collections.emptyList());

        // テスト実行
        Optional<Product> result = userCartRepository.findByIdAndStockQuantityGreaterThan(999L, 1);

        // 検証
        assertFalse(result.isPresent());

        verify(entityManager).createQuery(
                "SELECT p FROM Product p WHERE p.id = :id AND p.stockQuantity > :stockQuantity", Product.class);
        verify(productQuery).setParameter("id", 999L);
        verify(productQuery).setParameter("stockQuantity", 1);
    }

    @Test
    void UT_Repository_0106_findByIdInAndStockQuantityGreaterThanZero_正常系() {
        // モックの設定
        List<Product> productList = Arrays.asList(testProduct1, testProduct2);
        List<Long> productIds = Arrays.asList(1L, 2L, 3L);
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.setParameter(anyString(), any())).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(productList);

        // テスト実行
        List<Product> result = userCartRepository.findByIdInAndStockQuantityGreaterThanZero(productIds);

        // 検証
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getStockQuantity() > 0);
        assertTrue(result.get(1).getStockQuantity() > 0);

        verify(entityManager).createQuery(
                "SELECT p FROM Product p WHERE p.id IN :ids AND p.stockQuantity > 0", Product.class);
        verify(productQuery).setParameter("ids", productIds);
    }

    @Test
    void UT_Repository_0107_findByIdInAndStockQuantityGreaterThanZero_正常系() {
        // モックの設定
        List<Long> productIds = Arrays.asList(999L, 998L, 997L);
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.setParameter(anyString(), any())).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(Collections.emptyList());

        // テスト実行
        List<Product> result = userCartRepository.findByIdInAndStockQuantityGreaterThanZero(productIds);

        // 検証
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(entityManager).createQuery(
                "SELECT p FROM Product p WHERE p.id IN :ids AND p.stockQuantity > 0", Product.class);
        verify(productQuery).setParameter("ids", productIds);
    }

    @Test
    void UT_Repository_0108_findById_正常系() {
        // モックの設定
        when(entityManager.find(Product.class, 1L)).thenReturn(testProduct1);

        // テスト実行
        Optional<Product> result = userCartRepository.findById(1L);

        // 検証
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("テスト商品1", result.get().getName());

        verify(entityManager).find(Product.class, 1L);
    }

    @Test
    void UT_Repository_0109_findById_正常系() {
        // モックの設定
        when(entityManager.find(Product.class, 999L)).thenReturn(null);

        // テスト実行
        Optional<Product> result = userCartRepository.findById(999L);

        // 検証
        assertFalse(result.isPresent());

        verify(entityManager).find(Product.class, 999L);
    }

    @Test
    void UT_Repository_0110_save_正常系() {
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
        Product result = userCartRepository.save(newProduct);

        // 検証
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("新規商品", result.getName());

        verify(entityManager).persist(newProduct);
        verify(entityManager, never()).merge(any(Product.class));
    }

    @Test
    void UT_Repository_0111_save_正常系() {
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
        Product result = userCartRepository.save(existingProduct);

        // 検証
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("更新商品", result.getName());

        verify(entityManager).merge(existingProduct);
        verify(entityManager, never()).persist(any(Product.class));
    }

    @Test
    void UT_Repository_0112_deleteById_正常系() {
        // モックの設定
        when(entityManager.find(Product.class, 1L)).thenReturn(testProduct1);

        // テスト実行
        userCartRepository.deleteById(1L);

        // 検証
        verify(entityManager).find(Product.class, 1L);
        verify(entityManager).remove(testProduct1);
    }

    @Test
    void UT_Repository_0113_deleteById_正常系() {
        // モックの設定
        when(entityManager.find(Product.class, 999L)).thenReturn(null);

        // テスト実行
        userCartRepository.deleteById(999L);

        // 検証
        verify(entityManager).find(Product.class, 999L);
        verify(entityManager, never()).remove(any(Product.class));
    }

    @Test
    void UT_Repository_0114_existsById_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(1L);

        // テスト実行
        boolean result = userCartRepository.existsById(1L);

        // 検証
        assertTrue(result);

        verify(entityManager).createQuery(
                "SELECT COUNT(p) FROM Product p WHERE p.id = :id", Long.class);
        verify(longQuery).setParameter("id", 1L);
    }

    @Test
    void UT_Repository_0115_existsById_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(0L);

        // テスト実行
        boolean result = userCartRepository.existsById(999L);

        // 検証
        assertFalse(result);

        verify(entityManager).createQuery(
                "SELECT COUNT(p) FROM Product p WHERE p.id = :id", Long.class);
        verify(longQuery).setParameter("id", 999L);
    }

    @Test
    void UT_Repository_0116_findAll_正常系() {
        // モックの設定
        List<Product> productList = Arrays.asList(testProduct1, testProduct2);
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(productList);

        // テスト実行
        List<Product> result = userCartRepository.findAll();

        // 検証
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("テスト商品1", result.get(0).getName());
        assertEquals("テスト商品2", result.get(1).getName());

        verify(entityManager).createQuery(
                "SELECT p FROM Product p", Product.class);
    }

    @Test
    void UT_Repository_0117_findAll_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(Collections.emptyList());

        // テスト実行
        List<Product> result = userCartRepository.findAll();

        // 検証
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(entityManager).createQuery(
                "SELECT p FROM Product p", Product.class);
    }
}