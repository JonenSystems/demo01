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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * AdminProductRepositoryImplの単体テスト
 */
@ExtendWith(MockitoExtension.class)
class AdminProductRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Product> productQuery;

    @Mock
    private TypedQuery<Long> longQuery;

    @InjectMocks
    private AdminProductRepositoryImpl adminProductRepository;

    private Product testProduct1;
    private Product testProduct2;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        testProduct1 = new Product();
        testProduct1.setId(1L);
        testProduct1.setName("テスト商品1");
        testProduct1.setPrice(new BigDecimal("1000"));
        testProduct1.setCategory("電子機器");
        testProduct1.setStockQuantity(10);

        testProduct2 = new Product();
        testProduct2.setId(2L);
        testProduct2.setName("テスト商品2");
        testProduct2.setPrice(new BigDecimal("2000"));
        testProduct2.setCategory("電子機器");
        testProduct2.setStockQuantity(5);

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void UT_Repository_0016_findByNameContainingIgnoreCaseOrderByIdDesc_正常系() {
        // モックの設定
        List<Product> productList = Arrays.asList(testProduct1, testProduct2);
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.setParameter(anyString(), any())).thenReturn(productQuery);
        when(productQuery.setFirstResult(anyInt())).thenReturn(productQuery);
        when(productQuery.setMaxResults(anyInt())).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(productList);

        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(2L);

        // テスト実行
        Page<Product> result = adminProductRepository.findByNameContainingIgnoreCaseOrderByIdDesc("テスト商品", pageable);

        // 検証
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals("テスト商品1", result.getContent().get(0).getName());
        assertEquals("テスト商品2", result.getContent().get(1).getName());

        verify(entityManager).createQuery(
                "SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY p.id DESC",
                Product.class);
        verify(productQuery).setParameter("name", "テスト商品");
        verify(productQuery).setFirstResult(0);
        verify(productQuery).setMaxResults(10);
        verify(entityManager).createQuery(
                "SELECT COUNT(p) FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))", Long.class);
        verify(longQuery).setParameter("name", "テスト商品");
    }

    @Test
    void UT_Repository_0017_findByNameContainingIgnoreCaseOrderByIdDesc_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.setParameter(anyString(), any())).thenReturn(productQuery);
        when(productQuery.setFirstResult(anyInt())).thenReturn(productQuery);
        when(productQuery.setMaxResults(anyInt())).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(Collections.emptyList());

        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(0L);

        // テスト実行
        Page<Product> result = adminProductRepository.findByNameContainingIgnoreCaseOrderByIdDesc("存在しない商品", pageable);

        // 検証
        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());

        verify(entityManager).createQuery(
                "SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY p.id DESC",
                Product.class);
        verify(productQuery).setParameter("name", "存在しない商品");
        verify(entityManager).createQuery(
                "SELECT COUNT(p) FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))", Long.class);
        verify(longQuery).setParameter("name", "存在しない商品");
    }

    @Test
    void UT_Repository_0018_findByCategoryOrderByIdDesc_正常系() {
        // モックの設定
        List<Product> productList = Arrays.asList(testProduct1, testProduct2);
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.setParameter(anyString(), any())).thenReturn(productQuery);
        when(productQuery.setFirstResult(anyInt())).thenReturn(productQuery);
        when(productQuery.setMaxResults(anyInt())).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(productList);

        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(2L);

        // テスト実行
        Page<Product> result = adminProductRepository.findByCategoryOrderByIdDesc("電子機器", pageable);

        // 検証
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals("電子機器", result.getContent().get(0).getCategory());
        assertEquals("電子機器", result.getContent().get(1).getCategory());

        verify(entityManager).createQuery("SELECT p FROM Product p WHERE p.category = :category ORDER BY p.id DESC",
                Product.class);
        verify(productQuery).setParameter("category", "電子機器");
        verify(productQuery).setFirstResult(0);
        verify(productQuery).setMaxResults(10);
        verify(entityManager).createQuery("SELECT COUNT(p) FROM Product p WHERE p.category = :category", Long.class);
        verify(longQuery).setParameter("category", "電子機器");
    }

    @Test
    void UT_Repository_0019_findByCategoryOrderByIdDesc_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.setParameter(anyString(), any())).thenReturn(productQuery);
        when(productQuery.setFirstResult(anyInt())).thenReturn(productQuery);
        when(productQuery.setMaxResults(anyInt())).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(Collections.emptyList());

        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(0L);

        // テスト実行
        Page<Product> result = adminProductRepository.findByCategoryOrderByIdDesc("存在しないカテゴリ", pageable);

        // 検証
        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());

        verify(entityManager).createQuery("SELECT p FROM Product p WHERE p.category = :category ORDER BY p.id DESC",
                Product.class);
        verify(productQuery).setParameter("category", "存在しないカテゴリ");
        verify(entityManager).createQuery("SELECT COUNT(p) FROM Product p WHERE p.category = :category", Long.class);
        verify(longQuery).setParameter("category", "存在しないカテゴリ");
    }

    @Test
    void UT_Repository_0020_findByNameContainingIgnoreCaseAndCategoryOrderByIdDesc_正常系() {
        // モックの設定
        List<Product> productList = Arrays.asList(testProduct1, testProduct2);
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.setParameter(anyString(), any())).thenReturn(productQuery);
        when(productQuery.setFirstResult(anyInt())).thenReturn(productQuery);
        when(productQuery.setMaxResults(anyInt())).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(productList);

        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(2L);

        // テスト実行
        Page<Product> result = adminProductRepository.findByNameContainingIgnoreCaseAndCategoryOrderByIdDesc("テスト商品",
                "電子機器", pageable);

        // 検証
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals("テスト商品1", result.getContent().get(0).getName());
        assertEquals("電子機器", result.getContent().get(0).getCategory());
        assertEquals("テスト商品2", result.getContent().get(1).getName());
        assertEquals("電子機器", result.getContent().get(1).getCategory());

        verify(entityManager).createQuery(
                "SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.category = :category ORDER BY p.id DESC",
                Product.class);
        verify(productQuery).setParameter("name", "テスト商品");
        verify(productQuery).setParameter("category", "電子機器");
        verify(productQuery).setFirstResult(0);
        verify(productQuery).setMaxResults(10);
        verify(entityManager).createQuery(
                "SELECT COUNT(p) FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.category = :category",
                Long.class);
        verify(longQuery).setParameter("name", "テスト商品");
        verify(longQuery).setParameter("category", "電子機器");
    }

    @Test
    void UT_Repository_0021_findByNameContainingIgnoreCaseAndCategoryOrderByIdDesc_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.setParameter(anyString(), any())).thenReturn(productQuery);
        when(productQuery.setFirstResult(anyInt())).thenReturn(productQuery);
        when(productQuery.setMaxResults(anyInt())).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(Collections.emptyList());

        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(0L);

        // テスト実行
        Page<Product> result = adminProductRepository.findByNameContainingIgnoreCaseAndCategoryOrderByIdDesc("存在しない商品",
                "存在しないカテゴリ", pageable);

        // 検証
        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());

        verify(entityManager).createQuery(
                "SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.category = :category ORDER BY p.id DESC",
                Product.class);
        verify(productQuery).setParameter("name", "存在しない商品");
        verify(productQuery).setParameter("category", "存在しないカテゴリ");
        verify(entityManager).createQuery(
                "SELECT COUNT(p) FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.category = :category",
                Long.class);
        verify(longQuery).setParameter("name", "存在しない商品");
        verify(longQuery).setParameter("category", "存在しないカテゴリ");
    }

    @Test
    void UT_Repository_0022_findByStockQuantityLessThanEqualOrderByIdDesc_正常系() {
        // モックの設定
        List<Product> productList = Arrays.asList(testProduct1, testProduct2);
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.setParameter(anyString(), any())).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(productList);

        // テスト実行
        List<Product> result = adminProductRepository.findByStockQuantityLessThanEqualOrderByIdDesc(10);

        // 検証
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getStockQuantity() <= 10);
        assertTrue(result.get(1).getStockQuantity() <= 10);

        verify(entityManager).createQuery(
                "SELECT p FROM Product p WHERE p.stockQuantity <= :stockQuantity ORDER BY p.id DESC", Product.class);
        verify(productQuery).setParameter("stockQuantity", 10);
    }

    @Test
    void UT_Repository_0023_findByStockQuantityLessThanEqualOrderByIdDesc_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.setParameter(anyString(), any())).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(Collections.emptyList());

        // テスト実行
        List<Product> result = adminProductRepository.findByStockQuantityLessThanEqualOrderByIdDesc(0);

        // 検証
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(entityManager).createQuery(
                "SELECT p FROM Product p WHERE p.stockQuantity <= :stockQuantity ORDER BY p.id DESC", Product.class);
        verify(productQuery).setParameter("stockQuantity", 0);
    }

    @Test
    void UT_Repository_0024_findAllByOrderByIdDesc_正常系() {
        // モックの設定
        List<Product> productList = Arrays.asList(testProduct1, testProduct2);
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.setFirstResult(anyInt())).thenReturn(productQuery);
        when(productQuery.setMaxResults(anyInt())).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(productList);

        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(2L);

        // テスト実行
        Page<Product> result = adminProductRepository.findAllByOrderByIdDesc(pageable);

        // 検証
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());

        verify(entityManager).createQuery("SELECT p FROM Product p ORDER BY p.id DESC", Product.class);
        verify(productQuery).setFirstResult(0);
        verify(productQuery).setMaxResults(10);
        verify(entityManager).createQuery("SELECT COUNT(p) FROM Product p", Long.class);
    }

    @Test
    void UT_Repository_0025_findAllByOrderByIdDesc_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.setFirstResult(anyInt())).thenReturn(productQuery);
        when(productQuery.setMaxResults(anyInt())).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(Collections.emptyList());

        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(0L);

        // テスト実行
        Page<Product> result = adminProductRepository.findAllByOrderByIdDesc(pageable);

        // 検証
        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());

        verify(entityManager).createQuery("SELECT p FROM Product p ORDER BY p.id DESC", Product.class);
        verify(productQuery).setFirstResult(0);
        verify(productQuery).setMaxResults(10);
        verify(entityManager).createQuery("SELECT COUNT(p) FROM Product p", Long.class);
    }

    @Test
    void UT_Repository_0026_findById_正常系() {
        // モックの設定
        when(entityManager.find(Product.class, 1L)).thenReturn(testProduct1);

        // テスト実行
        Optional<Product> result = adminProductRepository.findById(1L);

        // 検証
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("テスト商品1", result.get().getName());

        verify(entityManager).find(Product.class, 1L);
    }

    @Test
    void UT_Repository_0027_findById_正常系() {
        // モックの設定
        when(entityManager.find(Product.class, 999L)).thenReturn(null);

        // テスト実行
        Optional<Product> result = adminProductRepository.findById(999L);

        // 検証
        assertFalse(result.isPresent());

        verify(entityManager).find(Product.class, 999L);
    }

    @Test
    void UT_Repository_0028_save_正常系() {
        // 新規商品の設定
        Product newProduct = new Product();
        newProduct.setName("新規商品");
        newProduct.setPrice(new BigDecimal("1500"));
        newProduct.setCategory("新カテゴリ");
        newProduct.setStockQuantity(20);

        // モックの設定
        doAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            product.setId(3L);
            return null;
        }).when(entityManager).persist(any(Product.class));

        // テスト実行
        Product result = adminProductRepository.save(newProduct);

        // 検証
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("新規商品", result.getName());

        verify(entityManager).persist(any(Product.class));
        verify(entityManager, never()).merge(any(Product.class));
    }

    @Test
    void UT_Repository_0029_save_正常系() {
        // 既存商品の設定
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("更新商品");
        existingProduct.setPrice(new BigDecimal("2000"));
        existingProduct.setCategory("更新カテゴリ");
        existingProduct.setStockQuantity(15);

        // モックの設定
        when(entityManager.merge(any(Product.class))).thenReturn(existingProduct);

        // テスト実行
        Product result = adminProductRepository.save(existingProduct);

        // 検証
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("更新商品", result.getName());

        verify(entityManager).merge(any(Product.class));
        verify(entityManager, never()).persist(any(Product.class));
    }

    @Test
    void UT_Repository_0030_deleteById_正常系() {
        // モックの設定
        when(entityManager.find(Product.class, 1L)).thenReturn(testProduct1);

        // テスト実行
        adminProductRepository.deleteById(1L);

        // 検証
        verify(entityManager).find(Product.class, 1L);
        verify(entityManager).remove(testProduct1);
    }

    @Test
    void UT_Repository_0031_deleteById_正常系() {
        // モックの設定
        when(entityManager.find(Product.class, 999L)).thenReturn(null);

        // テスト実行
        adminProductRepository.deleteById(999L);

        // 検証
        verify(entityManager).find(Product.class, 999L);
        verify(entityManager, never()).remove(any(Product.class));
    }

    @Test
    void UT_Repository_0032_existsById_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(1L);

        // テスト実行
        boolean result = adminProductRepository.existsById(1L);

        // 検証
        assertTrue(result);

        verify(entityManager).createQuery("SELECT COUNT(p) FROM Product p WHERE p.id = :id", Long.class);
        verify(longQuery).setParameter("id", 1L);
        verify(longQuery).getSingleResult();
    }

    @Test
    void UT_Repository_0033_existsById_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(0L);

        // テスト実行
        boolean result = adminProductRepository.existsById(999L);

        // 検証
        assertFalse(result);

        verify(entityManager).createQuery("SELECT COUNT(p) FROM Product p WHERE p.id = :id", Long.class);
        verify(longQuery).setParameter("id", 999L);
        verify(longQuery).getSingleResult();
    }

    @Test
    void UT_Repository_0034_findAll_正常系() {
        // モックの設定
        List<Product> productList = Arrays.asList(testProduct1, testProduct2);
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(productList);

        // テスト実行
        List<Product> result = adminProductRepository.findAll();

        // 検証
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(entityManager).createQuery("SELECT p FROM Product p", Product.class);
    }

    @Test
    void UT_Repository_0035_findAll_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(productQuery);
        when(productQuery.getResultList()).thenReturn(Collections.emptyList());

        // テスト実行
        List<Product> result = adminProductRepository.findAll();

        // 検証
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(entityManager).createQuery("SELECT p FROM Product p", Product.class);
    }
}