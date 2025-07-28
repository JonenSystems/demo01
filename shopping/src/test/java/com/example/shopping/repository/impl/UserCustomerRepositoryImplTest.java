package com.example.shopping.repository.impl;

import com.example.shopping.model.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * UserCustomerRepositoryImplの単体テスト
 */
@ExtendWith(MockitoExtension.class)
class UserCustomerRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Customer> customerQuery;

    @Mock
    private TypedQuery<Long> longQuery;

    @InjectMocks
    private UserCustomerRepositoryImpl userCustomerRepository;

    private Customer testCustomer1;
    private Customer testCustomer2;

    @BeforeEach
    void setUp() {
        testCustomer1 = new Customer();
        testCustomer1.setId(1L);
        testCustomer1.setName("テスト太郎");
        testCustomer1.setEmail("test@example.com");
        testCustomer1.setPhone("090-1234-5678");
        testCustomer1.setAddress("東京都渋谷区テスト1-1-1");

        testCustomer2 = new Customer();
        testCustomer2.setId(2L);
        testCustomer2.setName("テスト花子");
        testCustomer2.setEmail("hanako@example.com");
        testCustomer2.setPhone("090-9876-5432");
        testCustomer2.setAddress("大阪府大阪市テスト2-2-2");
    }

    @Test
    void UT_Repository_0118_findByEmail_正常系() {
        // モックの設定
        List<Customer> customerList = Arrays.asList(testCustomer1);
        when(entityManager.createQuery(anyString(), eq(Customer.class))).thenReturn(customerQuery);
        when(customerQuery.setParameter(anyString(), any())).thenReturn(customerQuery);
        when(customerQuery.getResultList()).thenReturn(customerList);

        // テスト実行
        Optional<Customer> result = userCustomerRepository.findByEmail("test@example.com");

        // 検証
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("test@example.com", result.get().getEmail());

        verify(entityManager).createQuery(
                "SELECT c FROM Customer c WHERE c.email = :email", Customer.class);
        verify(customerQuery).setParameter("email", "test@example.com");
    }

    @Test
    void UT_Repository_0119_findByEmail_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Customer.class))).thenReturn(customerQuery);
        when(customerQuery.setParameter(anyString(), any())).thenReturn(customerQuery);
        when(customerQuery.getResultList()).thenReturn(Collections.emptyList());

        // テスト実行
        Optional<Customer> result = userCustomerRepository.findByEmail("nonexistent@example.com");

        // 検証
        assertFalse(result.isPresent());

        verify(entityManager).createQuery(
                "SELECT c FROM Customer c WHERE c.email = :email", Customer.class);
        verify(customerQuery).setParameter("email", "nonexistent@example.com");
    }

    @Test
    void UT_Repository_0120_existsByEmail_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(1L);

        // テスト実行
        boolean result = userCustomerRepository.existsByEmail("test@example.com");

        // 検証
        assertTrue(result);

        verify(entityManager).createQuery(
                "SELECT COUNT(c) FROM Customer c WHERE c.email = :email", Long.class);
        verify(longQuery).setParameter("email", "test@example.com");
    }

    @Test
    void UT_Repository_0121_existsByEmail_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(0L);

        // テスト実行
        boolean result = userCustomerRepository.existsByEmail("nonexistent@example.com");

        // 検証
        assertFalse(result);

        verify(entityManager).createQuery(
                "SELECT COUNT(c) FROM Customer c WHERE c.email = :email", Long.class);
        verify(longQuery).setParameter("email", "nonexistent@example.com");
    }

    @Test
    void UT_Repository_0122_findById_正常系() {
        // モックの設定
        when(entityManager.find(Customer.class, 1L)).thenReturn(testCustomer1);

        // テスト実行
        Optional<Customer> result = userCustomerRepository.findById(1L);

        // 検証
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("テスト太郎", result.get().getName());

        verify(entityManager).find(Customer.class, 1L);
    }

    @Test
    void UT_Repository_0123_findById_正常系() {
        // モックの設定
        when(entityManager.find(Customer.class, 999L)).thenReturn(null);

        // テスト実行
        Optional<Customer> result = userCustomerRepository.findById(999L);

        // 検証
        assertFalse(result.isPresent());

        verify(entityManager).find(Customer.class, 999L);
    }

    @Test
    void UT_Repository_0124_save_正常系() {
        // 新規顧客の作成
        Customer newCustomer = new Customer();
        newCustomer.setName("新規顧客");
        newCustomer.setEmail("new@example.com");
        newCustomer.setPhone("090-1111-2222");
        newCustomer.setAddress("神奈川県横浜市新規3-3-3");

        // モックの設定
        doAnswer(invocation -> {
            Customer customer = invocation.getArgument(0);
            customer.setId(3L);
            return null;
        }).when(entityManager).persist(any(Customer.class));

        // テスト実行
        Customer result = userCustomerRepository.save(newCustomer);

        // 検証
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("新規顧客", result.getName());

        verify(entityManager).persist(newCustomer);
        verify(entityManager, never()).merge(any(Customer.class));
    }

    @Test
    void UT_Repository_0125_save_正常系() {
        // 既存顧客の更新
        Customer existingCustomer = new Customer();
        existingCustomer.setId(1L);
        existingCustomer.setName("更新顧客");
        existingCustomer.setEmail("updated@example.com");
        existingCustomer.setPhone("090-3333-4444");
        existingCustomer.setAddress("愛知県名古屋市更新4-4-4");

        // モックの設定
        when(entityManager.merge(existingCustomer)).thenReturn(existingCustomer);

        // テスト実行
        Customer result = userCustomerRepository.save(existingCustomer);

        // 検証
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("更新顧客", result.getName());

        verify(entityManager).merge(existingCustomer);
        verify(entityManager, never()).persist(any(Customer.class));
    }

    @Test
    void UT_Repository_0126_deleteById_正常系() {
        // モックの設定
        when(entityManager.find(Customer.class, 1L)).thenReturn(testCustomer1);

        // テスト実行
        userCustomerRepository.deleteById(1L);

        // 検証
        verify(entityManager).find(Customer.class, 1L);
        verify(entityManager).remove(testCustomer1);
    }

    @Test
    void UT_Repository_0127_deleteById_正常系() {
        // モックの設定
        when(entityManager.find(Customer.class, 999L)).thenReturn(null);

        // テスト実行
        userCustomerRepository.deleteById(999L);

        // 検証
        verify(entityManager).find(Customer.class, 999L);
        verify(entityManager, never()).remove(any(Customer.class));
    }

    @Test
    void UT_Repository_0128_existsById_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(1L);

        // テスト実行
        boolean result = userCustomerRepository.existsById(1L);

        // 検証
        assertTrue(result);

        verify(entityManager).createQuery(
                "SELECT COUNT(c) FROM Customer c WHERE c.id = :id", Long.class);
        verify(longQuery).setParameter("id", 1L);
    }

    @Test
    void UT_Repository_0129_existsById_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(0L);

        // テスト実行
        boolean result = userCustomerRepository.existsById(999L);

        // 検証
        assertFalse(result);

        verify(entityManager).createQuery(
                "SELECT COUNT(c) FROM Customer c WHERE c.id = :id", Long.class);
        verify(longQuery).setParameter("id", 999L);
    }

    @Test
    void UT_Repository_0130_findAll_正常系() {
        // モックの設定
        List<Customer> customerList = Arrays.asList(testCustomer1, testCustomer2);
        when(entityManager.createQuery(anyString(), eq(Customer.class))).thenReturn(customerQuery);
        when(customerQuery.getResultList()).thenReturn(customerList);

        // テスト実行
        List<Customer> result = userCustomerRepository.findAll();

        // 検証
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("テスト太郎", result.get(0).getName());
        assertEquals("テスト花子", result.get(1).getName());

        verify(entityManager).createQuery(
                "SELECT c FROM Customer c", Customer.class);
    }

    @Test
    void UT_Repository_0131_findAll_正常系() {
        // モックの設定
        when(entityManager.createQuery(anyString(), eq(Customer.class))).thenReturn(customerQuery);
        when(customerQuery.getResultList()).thenReturn(Collections.emptyList());

        // テスト実行
        List<Customer> result = userCustomerRepository.findAll();

        // 検証
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(entityManager).createQuery(
                "SELECT c FROM Customer c", Customer.class);
    }
}