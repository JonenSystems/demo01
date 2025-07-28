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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * AdminCustomerRepositoryImplの単体テスト
 */
@ExtendWith(MockitoExtension.class)
class AdminCustomerRepositoryImplTest {

        @Mock
        private EntityManager entityManager;

        @Mock
        private TypedQuery<Customer> customerQuery;

        @Mock
        private TypedQuery<Long> longQuery;

        @InjectMocks
        private AdminCustomerRepositoryImpl adminCustomerRepository;

        private Customer testCustomer1;
        private Customer testCustomer2;
        private Pageable pageable;

        @BeforeEach
        void setUp() {
                testCustomer1 = new Customer();
                testCustomer1.setId(1L);
                testCustomer1.setName("テスト太郎");
                testCustomer1.setEmail("test1@example.com");
                testCustomer1.setPhone("090-1234-5678");
                testCustomer1.setAddress("東京都渋谷区");

                testCustomer2 = new Customer();
                testCustomer2.setId(2L);
                testCustomer2.setName("テスト花子");
                testCustomer2.setEmail("test2@example.com");
                testCustomer2.setPhone("090-8765-4321");
                testCustomer2.setAddress("大阪府大阪市");

                pageable = PageRequest.of(0, 10);
        }

        @Test
        void UT_Repository_0036_findByNameContainingIgnoreCaseOrderByIdDesc_正常系() {
                // モックの設定
                List<Customer> customerList = Arrays.asList(testCustomer1, testCustomer2);
                when(entityManager.createQuery(anyString(), eq(Customer.class))).thenReturn(customerQuery);
                when(customerQuery.setParameter(anyString(), any())).thenReturn(customerQuery);
                when(customerQuery.setFirstResult(anyInt())).thenReturn(customerQuery);
                when(customerQuery.setMaxResults(anyInt())).thenReturn(customerQuery);
                when(customerQuery.getResultList()).thenReturn(customerList);

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(2L);

                // テスト実行
                Page<Customer> result = adminCustomerRepository.findByNameContainingIgnoreCaseOrderByIdDesc("テスト太郎",
                                pageable);

                // 検証
                assertNotNull(result);
                assertEquals(2, result.getContent().size());
                assertEquals(2, result.getTotalElements());
                assertEquals(1, result.getTotalPages());
                assertEquals("テスト太郎", result.getContent().get(0).getName());
                assertEquals("テスト花子", result.getContent().get(1).getName());

                verify(entityManager).createQuery(
                                "SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY c.id DESC",
                                Customer.class);
                verify(customerQuery).setParameter("name", "テスト太郎");
                verify(customerQuery).setFirstResult(0);
                verify(customerQuery).setMaxResults(10);
                verify(entityManager).createQuery(
                                "SELECT COUNT(c) FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))",
                                Long.class);
                verify(longQuery).setParameter("name", "テスト太郎");
        }

        @Test
        void UT_Repository_0037_findByNameContainingIgnoreCaseOrderByIdDesc_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Customer.class))).thenReturn(customerQuery);
                when(customerQuery.setParameter(anyString(), any())).thenReturn(customerQuery);
                when(customerQuery.setFirstResult(anyInt())).thenReturn(customerQuery);
                when(customerQuery.setMaxResults(anyInt())).thenReturn(customerQuery);
                when(customerQuery.getResultList()).thenReturn(Collections.emptyList());

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(0L);

                // テスト実行
                Page<Customer> result = adminCustomerRepository.findByNameContainingIgnoreCaseOrderByIdDesc("存在しない顧客",
                                pageable);

                // 検証
                assertNotNull(result);
                assertEquals(0, result.getContent().size());
                assertEquals(0, result.getTotalElements());
                assertEquals(0, result.getTotalPages());

                verify(entityManager).createQuery(
                                "SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY c.id DESC",
                                Customer.class);
                verify(customerQuery).setParameter("name", "存在しない顧客");
                verify(entityManager).createQuery(
                                "SELECT COUNT(c) FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))",
                                Long.class);
                verify(longQuery).setParameter("name", "存在しない顧客");
        }

        @Test
        void UT_Repository_0038_findByEmailContainingIgnoreCaseOrderByIdDesc_正常系() {
                // モックの設定
                List<Customer> customerList = Arrays.asList(testCustomer1, testCustomer2);
                when(entityManager.createQuery(anyString(), eq(Customer.class))).thenReturn(customerQuery);
                when(customerQuery.setParameter(anyString(), any())).thenReturn(customerQuery);
                when(customerQuery.setFirstResult(anyInt())).thenReturn(customerQuery);
                when(customerQuery.setMaxResults(anyInt())).thenReturn(customerQuery);
                when(customerQuery.getResultList()).thenReturn(customerList);

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(2L);

                // テスト実行
                Page<Customer> result = adminCustomerRepository.findByEmailContainingIgnoreCaseOrderByIdDesc(
                                "test@example.com",
                                pageable);

                // 検証
                assertNotNull(result);
                assertEquals(2, result.getContent().size());
                assertEquals(2, result.getTotalElements());
                assertEquals(1, result.getTotalPages());
                assertEquals("test1@example.com", result.getContent().get(0).getEmail());
                assertEquals("test2@example.com", result.getContent().get(1).getEmail());

                verify(entityManager).createQuery(
                                "SELECT c FROM Customer c WHERE LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%')) ORDER BY c.id DESC",
                                Customer.class);
                verify(customerQuery).setParameter("email", "test@example.com");
                verify(customerQuery).setFirstResult(0);
                verify(customerQuery).setMaxResults(10);
                verify(entityManager).createQuery(
                                "SELECT COUNT(c) FROM Customer c WHERE LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))",
                                Long.class);
                verify(longQuery).setParameter("email", "test@example.com");
        }

        @Test
        void UT_Repository_0039_findByEmailContainingIgnoreCaseOrderByIdDesc_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Customer.class))).thenReturn(customerQuery);
                when(customerQuery.setParameter(anyString(), any())).thenReturn(customerQuery);
                when(customerQuery.setFirstResult(anyInt())).thenReturn(customerQuery);
                when(customerQuery.setMaxResults(anyInt())).thenReturn(customerQuery);
                when(customerQuery.getResultList()).thenReturn(Collections.emptyList());

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(0L);

                // テスト実行
                Page<Customer> result = adminCustomerRepository
                                .findByEmailContainingIgnoreCaseOrderByIdDesc("nonexistent@example.com", pageable);

                // 検証
                assertNotNull(result);
                assertEquals(0, result.getContent().size());
                assertEquals(0, result.getTotalElements());
                assertEquals(0, result.getTotalPages());

                verify(entityManager).createQuery(
                                "SELECT c FROM Customer c WHERE LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%')) ORDER BY c.id DESC",
                                Customer.class);
                verify(customerQuery).setParameter("email", "nonexistent@example.com");
                verify(entityManager).createQuery(
                                "SELECT COUNT(c) FROM Customer c WHERE LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))",
                                Long.class);
                verify(longQuery).setParameter("email", "nonexistent@example.com");
        }

        @Test
        void UT_Repository_0040_findByNameContainingIgnoreCaseAndEmailContainingIgnoreCaseOrderByIdDesc_正常系() {
                // モックの設定
                List<Customer> customerList = Arrays.asList(testCustomer1, testCustomer2);
                when(entityManager.createQuery(anyString(), eq(Customer.class))).thenReturn(customerQuery);
                when(customerQuery.setParameter(anyString(), any())).thenReturn(customerQuery);
                when(customerQuery.setFirstResult(anyInt())).thenReturn(customerQuery);
                when(customerQuery.setMaxResults(anyInt())).thenReturn(customerQuery);
                when(customerQuery.getResultList()).thenReturn(customerList);

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(2L);

                // テスト実行
                Page<Customer> result = adminCustomerRepository
                                .findByNameContainingIgnoreCaseAndEmailContainingIgnoreCaseOrderByIdDesc("テスト太郎",
                                                "test@example.com",
                                                pageable);

                // 検証
                assertNotNull(result);
                assertEquals(2, result.getContent().size());
                assertEquals(2, result.getTotalElements());
                assertEquals(1, result.getTotalPages());
                assertEquals("テスト太郎", result.getContent().get(0).getName());
                assertEquals("test1@example.com", result.getContent().get(0).getEmail());
                assertEquals("テスト花子", result.getContent().get(1).getName());
                assertEquals("test2@example.com", result.getContent().get(1).getEmail());

                verify(entityManager).createQuery(
                                "SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%')) ORDER BY c.id DESC",
                                Customer.class);
                verify(customerQuery).setParameter("name", "テスト太郎");
                verify(customerQuery).setParameter("email", "test@example.com");
                verify(customerQuery).setFirstResult(0);
                verify(customerQuery).setMaxResults(10);
                verify(entityManager).createQuery(
                                "SELECT COUNT(c) FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))",
                                Long.class);
                verify(longQuery).setParameter("name", "テスト太郎");
                verify(longQuery).setParameter("email", "test@example.com");
        }

        @Test
        void UT_Repository_0041_findByNameContainingIgnoreCaseAndEmailContainingIgnoreCaseOrderByIdDesc_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Customer.class))).thenReturn(customerQuery);
                when(customerQuery.setParameter(anyString(), any())).thenReturn(customerQuery);
                when(customerQuery.setFirstResult(anyInt())).thenReturn(customerQuery);
                when(customerQuery.setMaxResults(anyInt())).thenReturn(customerQuery);
                when(customerQuery.getResultList()).thenReturn(Collections.emptyList());

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(0L);

                // テスト実行
                Page<Customer> result = adminCustomerRepository
                                .findByNameContainingIgnoreCaseAndEmailContainingIgnoreCaseOrderByIdDesc("存在しない顧客",
                                                "nonexistent@example.com", pageable);

                // 検証
                assertNotNull(result);
                assertEquals(0, result.getContent().size());
                assertEquals(0, result.getTotalElements());
                assertEquals(0, result.getTotalPages());

                verify(entityManager).createQuery(
                                "SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%')) ORDER BY c.id DESC",
                                Customer.class);
                verify(customerQuery).setParameter("name", "存在しない顧客");
                verify(customerQuery).setParameter("email", "nonexistent@example.com");
                verify(entityManager).createQuery(
                                "SELECT COUNT(c) FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))",
                                Long.class);
                verify(longQuery).setParameter("name", "存在しない顧客");
                verify(longQuery).setParameter("email", "nonexistent@example.com");
        }

        @Test
        void UT_Repository_0042_findAllByOrderByIdDesc_正常系() {
                // モックの設定
                List<Customer> customerList = Arrays.asList(testCustomer1, testCustomer2);
                when(entityManager.createQuery(anyString(), eq(Customer.class))).thenReturn(customerQuery);
                when(customerQuery.setFirstResult(anyInt())).thenReturn(customerQuery);
                when(customerQuery.setMaxResults(anyInt())).thenReturn(customerQuery);
                when(customerQuery.getResultList()).thenReturn(customerList);

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(2L);

                // テスト実行
                Page<Customer> result = adminCustomerRepository.findAllByOrderByIdDesc(pageable);

                // 検証
                assertNotNull(result);
                assertEquals(2, result.getContent().size());
                assertEquals(2, result.getTotalElements());
                assertEquals(1, result.getTotalPages());

                verify(entityManager).createQuery("SELECT c FROM Customer c ORDER BY c.id DESC", Customer.class);
                verify(customerQuery).setFirstResult(0);
                verify(customerQuery).setMaxResults(10);
                verify(entityManager).createQuery("SELECT COUNT(c) FROM Customer c", Long.class);
        }

        @Test
        void UT_Repository_0043_findAllByOrderByIdDesc_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Customer.class))).thenReturn(customerQuery);
                when(customerQuery.setFirstResult(anyInt())).thenReturn(customerQuery);
                when(customerQuery.setMaxResults(anyInt())).thenReturn(customerQuery);
                when(customerQuery.getResultList()).thenReturn(Collections.emptyList());

                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(0L);

                // テスト実行
                Page<Customer> result = adminCustomerRepository.findAllByOrderByIdDesc(pageable);

                // 検証
                assertNotNull(result);
                assertEquals(0, result.getContent().size());
                assertEquals(0, result.getTotalElements());
                assertEquals(0, result.getTotalPages());

                verify(entityManager).createQuery("SELECT c FROM Customer c ORDER BY c.id DESC", Customer.class);
                verify(customerQuery).setFirstResult(0);
                verify(customerQuery).setMaxResults(10);
                verify(entityManager).createQuery("SELECT COUNT(c) FROM Customer c", Long.class);
        }

        @Test
        void UT_Repository_0044_findByEmail_正常系() {
                // モックの設定
                List<Customer> customerList = Arrays.asList(testCustomer1);
                when(entityManager.createQuery(anyString(), eq(Customer.class))).thenReturn(customerQuery);
                when(customerQuery.setParameter(anyString(), any())).thenReturn(customerQuery);
                when(customerQuery.getResultList()).thenReturn(customerList);

                // テスト実行
                Customer result = adminCustomerRepository.findByEmail("test@example.com");

                // 検証
                assertNotNull(result);
                assertEquals("test1@example.com", result.getEmail());
                assertEquals("テスト太郎", result.getName());

                verify(entityManager).createQuery("SELECT c FROM Customer c WHERE c.email = :email", Customer.class);
                verify(customerQuery).setParameter("email", "test@example.com");
        }

        @Test
        void UT_Repository_0045_findByEmail_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Customer.class))).thenReturn(customerQuery);
                when(customerQuery.setParameter(anyString(), any())).thenReturn(customerQuery);
                when(customerQuery.getResultList()).thenReturn(Collections.emptyList());

                // テスト実行
                Customer result = adminCustomerRepository.findByEmail("nonexistent@example.com");

                // 検証
                assertNull(result);

                verify(entityManager).createQuery("SELECT c FROM Customer c WHERE c.email = :email", Customer.class);
                verify(customerQuery).setParameter("email", "nonexistent@example.com");
        }

        @Test
        void UT_Repository_0046_existsByEmailExcludingId_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(1L);

                // テスト実行
                boolean result = adminCustomerRepository.existsByEmailExcludingId("test@example.com", null);

                // 検証
                assertTrue(result);

                verify(entityManager).createQuery(
                                "SELECT COUNT(c) FROM Customer c WHERE c.email = :email AND c.id != :excludeId",
                                Long.class);
                verify(longQuery).setParameter("email", "test@example.com");
                verify(longQuery).setParameter("excludeId", null);
                verify(longQuery).getSingleResult();
        }

        @Test
        void UT_Repository_0047_existsByEmailExcludingId_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(0L);

                // テスト実行
                boolean result = adminCustomerRepository.existsByEmailExcludingId("unique@example.com", null);

                // 検証
                assertFalse(result);

                verify(entityManager).createQuery(
                                "SELECT COUNT(c) FROM Customer c WHERE c.email = :email AND c.id != :excludeId",
                                Long.class);
                verify(longQuery).setParameter("email", "unique@example.com");
                verify(longQuery).setParameter("excludeId", null);
                verify(longQuery).getSingleResult();
        }

        @Test
        void UT_Repository_0048_existsByEmailExcludingId_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(0L);

                // テスト実行
                boolean result = adminCustomerRepository.existsByEmailExcludingId("test@example.com", 1L);

                // 検証
                assertFalse(result);

                verify(entityManager).createQuery(
                                "SELECT COUNT(c) FROM Customer c WHERE c.email = :email AND c.id != :excludeId",
                                Long.class);
                verify(longQuery).setParameter("email", "test@example.com");
                verify(longQuery).setParameter("excludeId", 1L);
                verify(longQuery).getSingleResult();
        }

        @Test
        void UT_Repository_0049_findById_正常系() {
                // モックの設定
                when(entityManager.find(Customer.class, 1L)).thenReturn(testCustomer1);

                // テスト実行
                Optional<Customer> result = adminCustomerRepository.findById(1L);

                // 検証
                assertTrue(result.isPresent());
                assertEquals(1L, result.get().getId());
                assertEquals("テスト太郎", result.get().getName());

                verify(entityManager).find(Customer.class, 1L);
        }

        @Test
        void UT_Repository_0050_findById_正常系() {
                // モックの設定
                when(entityManager.find(Customer.class, 999L)).thenReturn(null);

                // テスト実行
                Optional<Customer> result = adminCustomerRepository.findById(999L);

                // 検証
                assertFalse(result.isPresent());

                verify(entityManager).find(Customer.class, 999L);
        }

        @Test
        void UT_Repository_0051_save_正常系() {
                // 新規顧客の設定
                Customer newCustomer = new Customer();
                newCustomer.setName("新規顧客");
                newCustomer.setEmail("new@example.com");
                newCustomer.setPhone("090-1111-2222");
                newCustomer.setAddress("東京都新宿区");

                // モックの設定
                doAnswer(invocation -> {
                        Customer customer = invocation.getArgument(0);
                        customer.setId(3L);
                        return null;
                }).when(entityManager).persist(any(Customer.class));

                // テスト実行
                Customer result = adminCustomerRepository.save(newCustomer);

                // 検証
                assertNotNull(result);
                assertEquals(3L, result.getId());
                assertEquals("新規顧客", result.getName());

                verify(entityManager).persist(any(Customer.class));
                verify(entityManager, never()).merge(any(Customer.class));
        }

        @Test
        void UT_Repository_0052_save_正常系() {
                // 既存顧客の設定
                Customer existingCustomer = new Customer();
                existingCustomer.setId(1L);
                existingCustomer.setName("更新顧客");
                existingCustomer.setEmail("updated@example.com");
                existingCustomer.setPhone("090-3333-4444");
                existingCustomer.setAddress("大阪府大阪市");

                // モックの設定
                when(entityManager.merge(any(Customer.class))).thenReturn(existingCustomer);

                // テスト実行
                Customer result = adminCustomerRepository.save(existingCustomer);

                // 検証
                assertNotNull(result);
                assertEquals(1L, result.getId());
                assertEquals("更新顧客", result.getName());

                verify(entityManager).merge(any(Customer.class));
                verify(entityManager, never()).persist(any(Customer.class));
        }

        @Test
        void UT_Repository_0053_deleteById_正常系() {
                // モックの設定
                when(entityManager.find(Customer.class, 1L)).thenReturn(testCustomer1);

                // テスト実行
                adminCustomerRepository.deleteById(1L);

                // 検証
                verify(entityManager).find(Customer.class, 1L);
                verify(entityManager).remove(testCustomer1);
        }

        @Test
        void UT_Repository_0054_deleteById_正常系() {
                // モックの設定
                when(entityManager.find(Customer.class, 999L)).thenReturn(null);

                // テスト実行
                adminCustomerRepository.deleteById(999L);

                // 検証
                verify(entityManager).find(Customer.class, 999L);
                verify(entityManager, never()).remove(any(Customer.class));
        }

        @Test
        void UT_Repository_0055_existsById_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(1L);

                // テスト実行
                boolean result = adminCustomerRepository.existsById(1L);

                // 検証
                assertTrue(result);

                verify(entityManager).createQuery("SELECT COUNT(c) FROM Customer c WHERE c.id = :id", Long.class);
                verify(longQuery).setParameter("id", 1L);
                verify(longQuery).getSingleResult();
        }

        @Test
        void UT_Repository_0056_existsById_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
                when(longQuery.setParameter(anyString(), any())).thenReturn(longQuery);
                when(longQuery.getSingleResult()).thenReturn(0L);

                // テスト実行
                boolean result = adminCustomerRepository.existsById(999L);

                // 検証
                assertFalse(result);

                verify(entityManager).createQuery("SELECT COUNT(c) FROM Customer c WHERE c.id = :id", Long.class);
                verify(longQuery).setParameter("id", 999L);
                verify(longQuery).getSingleResult();
        }

        @Test
        void UT_Repository_0057_findAll_正常系() {
                // モックの設定
                List<Customer> customerList = Arrays.asList(testCustomer1, testCustomer2);
                when(entityManager.createQuery(anyString(), eq(Customer.class))).thenReturn(customerQuery);
                when(customerQuery.getResultList()).thenReturn(customerList);

                // テスト実行
                List<Customer> result = adminCustomerRepository.findAll();

                // 検証
                assertNotNull(result);
                assertEquals(2, result.size());

                verify(entityManager).createQuery("SELECT c FROM Customer c", Customer.class);
        }

        @Test
        void UT_Repository_0058_findAll_正常系() {
                // モックの設定
                when(entityManager.createQuery(anyString(), eq(Customer.class))).thenReturn(customerQuery);
                when(customerQuery.getResultList()).thenReturn(Collections.emptyList());

                // テスト実行
                List<Customer> result = adminCustomerRepository.findAll();

                // 検証
                assertNotNull(result);
                assertEquals(0, result.size());

                verify(entityManager).createQuery("SELECT c FROM Customer c", Customer.class);
        }
}