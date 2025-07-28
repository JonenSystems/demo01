package com.example.shopping.service;

import com.example.shopping.model.dto.AdminCustomerDto;
import com.example.shopping.model.entity.Customer;
import com.example.shopping.model.form.AdminCustomerListForm;
import com.example.shopping.repository.AdminCustomerRepository;
import com.example.shopping.service.impl.AdminCustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 管理者顧客サービスのテストクラス
 */
@ExtendWith(MockitoExtension.class)
class AdminCustomerServiceImplTest {

    @Mock
    private AdminCustomerRepository adminCustomerRepository;

    @InjectMocks
    private AdminCustomerServiceImpl adminCustomerService;

    private Customer testCustomer1;
    private Customer testCustomer2;
    private Customer testCustomer3;
    private Customer testCustomer4;

    @BeforeEach
    void setUp() {
        // テスト用の顧客1
        testCustomer1 = new Customer();
        testCustomer1.setId(1L);
        testCustomer1.setName("田中太郎");
        testCustomer1.setEmail("tanaka@example.com");
        testCustomer1.setPhone("090-1234-5678");
        testCustomer1.setAddress("東京都渋谷区1-1-1");
        testCustomer1.setCreatedAt(LocalDateTime.now());
        testCustomer1.setUpdatedAt(LocalDateTime.now());

        // テスト用の顧客2
        testCustomer2 = new Customer();
        testCustomer2.setId(2L);
        testCustomer2.setName("佐藤花子");
        testCustomer2.setEmail("sato@example.com");
        testCustomer2.setPhone("090-9876-5432");
        testCustomer2.setAddress("大阪府大阪市2-2-2");
        testCustomer2.setCreatedAt(LocalDateTime.now());
        testCustomer2.setUpdatedAt(LocalDateTime.now());

        // テスト用の顧客3
        testCustomer3 = new Customer();
        testCustomer3.setId(3L);
        testCustomer3.setName("鈴木一郎");
        testCustomer3.setEmail("suzuki@example.com");
        testCustomer3.setPhone("090-5555-5555");
        testCustomer3.setAddress("愛知県名古屋市3-3-3");
        testCustomer3.setCreatedAt(LocalDateTime.now());
        testCustomer3.setUpdatedAt(LocalDateTime.now());

        // テスト用の顧客4（重複チェック用）
        testCustomer4 = new Customer();
        testCustomer4.setId(4L);
        testCustomer4.setName("田中次郎");
        testCustomer4.setEmail("tanaka2@example.com");
        testCustomer4.setPhone("090-1111-1111");
        testCustomer4.setAddress("東京都新宿区4-4-4");
        testCustomer4.setCreatedAt(LocalDateTime.now());
        testCustomer4.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void UT_Service_0040_getCustomerList_正常系() {
        // 検索条件なしで全顧客取得
        Pageable pageable = PageRequest.of(0, 10);
        List<Customer> customerList = Arrays.asList(testCustomer1, testCustomer2, testCustomer3);
        Page<Customer> customerPage = new PageImpl<>(customerList, pageable, 3);

        when(adminCustomerRepository.findAllByOrderByIdDesc(pageable))
                .thenReturn(customerPage);

        AdminCustomerListForm result = adminCustomerService.getCustomerList(null, null, pageable);

        assertNotNull(result);
        assertEquals(3, result.getCustomers().size());
        assertEquals(1, result.getTotalPages());
        assertEquals(3, result.getTotalElements());
        assertFalse(result.isHasNext());
        assertFalse(result.isHasPrevious());
        assertEquals(0, result.getCurrentPage());
        assertNull(result.getSearchName());
        assertNull(result.getSearchEmail());
        verify(adminCustomerRepository).findAllByOrderByIdDesc(pageable);
    }

    @Test
    void UT_Service_0041_getCustomerList_正常系() {
        // 顧客名で検索
        Pageable pageable = PageRequest.of(0, 10);
        List<Customer> customerList = Arrays.asList(testCustomer1, testCustomer4);
        Page<Customer> customerPage = new PageImpl<>(customerList, pageable, 2);

        when(adminCustomerRepository.findByNameContainingIgnoreCaseOrderByIdDesc("田中", pageable))
                .thenReturn(customerPage);

        AdminCustomerListForm result = adminCustomerService.getCustomerList("田中", null, pageable);

        assertNotNull(result);
        assertEquals(2, result.getCustomers().size());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getTotalElements());
        assertFalse(result.isHasNext());
        assertFalse(result.isHasPrevious());
        assertEquals(0, result.getCurrentPage());
        assertEquals("田中", result.getSearchName());
        assertNull(result.getSearchEmail());
        verify(adminCustomerRepository).findByNameContainingIgnoreCaseOrderByIdDesc("田中", pageable);
    }

    @Test
    void UT_Service_0042_getCustomerList_正常系() {
        // メールアドレスで検索
        Pageable pageable = PageRequest.of(0, 10);
        List<Customer> customerList = Arrays.asList(testCustomer1);
        Page<Customer> customerPage = new PageImpl<>(customerList, pageable, 1);

        when(adminCustomerRepository.findByEmailContainingIgnoreCaseOrderByIdDesc("test@example.com", pageable))
                .thenReturn(customerPage);

        AdminCustomerListForm result = adminCustomerService.getCustomerList(null, "test@example.com", pageable);

        assertNotNull(result);
        assertEquals(1, result.getCustomers().size());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getTotalElements());
        assertFalse(result.isHasNext());
        assertFalse(result.isHasPrevious());
        assertEquals(0, result.getCurrentPage());
        assertNull(result.getSearchName());
        assertEquals("test@example.com", result.getSearchEmail());
        verify(adminCustomerRepository).findByEmailContainingIgnoreCaseOrderByIdDesc("test@example.com", pageable);
    }

    @Test
    void UT_Service_0043_getCustomerList_正常系() {
        // 顧客名とメールアドレスで検索
        Pageable pageable = PageRequest.of(0, 10);
        List<Customer> customerList = Arrays.asList(testCustomer1);
        Page<Customer> customerPage = new PageImpl<>(customerList, pageable, 1);

        when(adminCustomerRepository.findByNameContainingIgnoreCaseAndEmailContainingIgnoreCaseOrderByIdDesc(
                "田中", "tanaka@example.com", pageable))
                .thenReturn(customerPage);

        AdminCustomerListForm result = adminCustomerService.getCustomerList("田中", "tanaka@example.com", pageable);

        assertNotNull(result);
        assertEquals(1, result.getCustomers().size());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getTotalElements());
        assertFalse(result.isHasNext());
        assertFalse(result.isHasPrevious());
        assertEquals(0, result.getCurrentPage());
        assertEquals("田中", result.getSearchName());
        assertEquals("tanaka@example.com", result.getSearchEmail());
        verify(adminCustomerRepository).findByNameContainingIgnoreCaseAndEmailContainingIgnoreCaseOrderByIdDesc(
                "田中", "tanaka@example.com", pageable);
    }

    @Test
    void UT_Service_0044_getCustomerList_正常系() {
        // 空文字の検索条件で検索
        Pageable pageable = PageRequest.of(0, 10);
        List<Customer> customerList = Arrays.asList(testCustomer1, testCustomer2, testCustomer3);
        Page<Customer> customerPage = new PageImpl<>(customerList, pageable, 3);

        when(adminCustomerRepository.findAllByOrderByIdDesc(pageable))
                .thenReturn(customerPage);

        AdminCustomerListForm result = adminCustomerService.getCustomerList("", "", pageable);

        assertNotNull(result);
        assertEquals(3, result.getCustomers().size());
        assertEquals(1, result.getTotalPages());
        assertEquals(3, result.getTotalElements());
        assertFalse(result.isHasNext());
        assertFalse(result.isHasPrevious());
        assertEquals(0, result.getCurrentPage());
        assertEquals("", result.getSearchName());
        assertEquals("", result.getSearchEmail());
        verify(adminCustomerRepository).findAllByOrderByIdDesc(pageable);
    }

    @Test
    void UT_Service_0045_getCustomerList_正常系() {
        // 2ページ目の取得
        Pageable pageable = PageRequest.of(1, 5);
        List<Customer> customerList = Arrays.asList(testCustomer3);
        Page<Customer> customerPage = new PageImpl<>(customerList, pageable, 6);

        when(adminCustomerRepository.findAllByOrderByIdDesc(pageable))
                .thenReturn(customerPage);

        AdminCustomerListForm result = adminCustomerService.getCustomerList(null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getCustomers().size());
        assertEquals(2, result.getTotalPages());
        assertEquals(6, result.getTotalElements());
        assertFalse(result.isHasNext());
        assertTrue(result.isHasPrevious());
        assertEquals(1, result.getCurrentPage());
        assertNull(result.getSearchName());
        assertNull(result.getSearchEmail());
        verify(adminCustomerRepository).findAllByOrderByIdDesc(pageable);
    }

    @Test
    void UT_Service_0046_getCustomerById_正常系() {
        // 存在する顧客IDで詳細取得
        when(adminCustomerRepository.findById(1L))
                .thenReturn(Optional.of(testCustomer1));

        AdminCustomerDto result = adminCustomerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("田中太郎", result.getName());
        assertEquals("tanaka@example.com", result.getEmail());
        verify(adminCustomerRepository).findById(1L);
    }

    @Test
    void UT_Service_0047_getCustomerById_異常系() {
        // 存在しない顧客IDで詳細取得失敗
        when(adminCustomerRepository.findById(999L))
                .thenReturn(Optional.empty());

        AdminCustomerDto result = adminCustomerService.getCustomerById(999L);

        assertNull(result);
        verify(adminCustomerRepository).findById(999L);
    }

    @Test
    void UT_Service_0048_getCustomerById_異常系() {
        // nullの顧客IDで詳細取得失敗
        when(adminCustomerRepository.findById(null))
                .thenReturn(Optional.empty());

        AdminCustomerDto result = adminCustomerService.getCustomerById(null);

        assertNull(result);
        verify(adminCustomerRepository).findById(null);
    }

    @Test
    void UT_Service_0049_saveCustomer_正常系() {
        // 新規顧客の保存
        AdminCustomerDto customerDto = new AdminCustomerDto();
        customerDto.setName("新顧客");
        customerDto.setEmail("new@example.com");
        customerDto.setPhone("090-0000-0000");
        customerDto.setAddress("東京都新宿区新規住所");

        when(adminCustomerRepository.findByEmail("new@example.com"))
                .thenReturn(null);
        when(adminCustomerRepository.save(any(Customer.class)))
                .thenReturn(testCustomer1);

        boolean result = adminCustomerService.saveCustomer(customerDto);

        assertTrue(result);
        verify(adminCustomerRepository).findByEmail("new@example.com");
        verify(adminCustomerRepository).save(any(Customer.class));
    }

    @Test
    void UT_Service_0050_saveCustomer_正常系() {
        // 既存顧客の更新
        AdminCustomerDto customerDto = new AdminCustomerDto();
        customerDto.setId(1L);
        customerDto.setName("更新顧客");
        customerDto.setEmail("update@example.com");
        customerDto.setPhone("090-0000-0000");
        customerDto.setAddress("東京都新宿区更新住所");

        when(adminCustomerRepository.findByEmail("update@example.com"))
                .thenReturn(null);
        when(adminCustomerRepository.save(any(Customer.class)))
                .thenReturn(testCustomer1);

        boolean result = adminCustomerService.saveCustomer(customerDto);

        assertTrue(result);
        verify(adminCustomerRepository).findByEmail("update@example.com");
        verify(adminCustomerRepository).save(any(Customer.class));
    }

    @Test
    void UT_Service_0051_saveCustomer_異常系() {
        // 重複メールアドレスで保存失敗
        AdminCustomerDto customerDto = new AdminCustomerDto();
        customerDto.setName("重複顧客");
        customerDto.setEmail("existing@example.com");
        customerDto.setPhone("090-0000-0000");
        customerDto.setAddress("東京都新宿区重複住所");

        when(adminCustomerRepository.findByEmail("existing@example.com"))
                .thenReturn(testCustomer2);

        boolean result = adminCustomerService.saveCustomer(customerDto);

        assertFalse(result);
        verify(adminCustomerRepository).findByEmail("existing@example.com");
        verify(adminCustomerRepository, never()).save(any(Customer.class));
    }

    @Test
    void UT_Service_0052_saveCustomer_異常系() {
        // 無効な顧客データで保存失敗
        // nullの場合はNullPointerExceptionが発生するため、例外を期待
        assertThrows(NullPointerException.class, () -> {
            adminCustomerService.saveCustomer(null);
        });
        verify(adminCustomerRepository, never()).save(any(Customer.class));
    }

    @Test
    void UT_Service_0053_saveCustomer_異常系() {
        // 顧客名が空で保存失敗
        AdminCustomerDto customerDto = new AdminCustomerDto();
        customerDto.setName("");
        customerDto.setEmail("test@example.com");
        customerDto.setPhone("090-0000-0000");
        customerDto.setAddress("東京都新宿区住所");

        when(adminCustomerRepository.findByEmail("test@example.com"))
                .thenReturn(null);
        when(adminCustomerRepository.save(any(Customer.class)))
                .thenThrow(new RuntimeException("Validation error"));

        boolean result = adminCustomerService.saveCustomer(customerDto);

        assertFalse(result);
        verify(adminCustomerRepository).findByEmail("test@example.com");
        verify(adminCustomerRepository).save(any(Customer.class));
    }

    @Test
    void UT_Service_0054_saveCustomer_異常系() {
        // 無効なメールアドレスで保存失敗
        AdminCustomerDto customerDto = new AdminCustomerDto();
        customerDto.setName("顧客");
        customerDto.setEmail("invalid-email");
        customerDto.setPhone("090-0000-0000");
        customerDto.setAddress("東京都新宿区住所");

        when(adminCustomerRepository.findByEmail("invalid-email"))
                .thenReturn(null);
        when(adminCustomerRepository.save(any(Customer.class)))
                .thenThrow(new RuntimeException("Validation error"));

        boolean result = adminCustomerService.saveCustomer(customerDto);

        assertFalse(result);
        verify(adminCustomerRepository).findByEmail("invalid-email");
        verify(adminCustomerRepository).save(any(Customer.class));
    }

    @Test
    void UT_Service_0055_deleteCustomer_正常系() {
        // 存在する顧客の削除
        when(adminCustomerRepository.existsById(1L))
                .thenReturn(true);
        doNothing().when(adminCustomerRepository).deleteById(1L);

        boolean result = adminCustomerService.deleteCustomer(1L);

        assertTrue(result);
        verify(adminCustomerRepository).existsById(1L);
        verify(adminCustomerRepository).deleteById(1L);
    }

    @Test
    void UT_Service_0056_deleteCustomer_異常系() {
        // 存在しない顧客の削除失敗
        when(adminCustomerRepository.existsById(999L))
                .thenReturn(false);

        boolean result = adminCustomerService.deleteCustomer(999L);

        assertFalse(result);
        verify(adminCustomerRepository).existsById(999L);
        verify(adminCustomerRepository, never()).deleteById(any(Long.class));
    }

    @Test
    void UT_Service_0057_deleteCustomer_異常系() {
        // nullの顧客IDで削除失敗
        when(adminCustomerRepository.existsById(null))
                .thenReturn(false);

        boolean result = adminCustomerService.deleteCustomer(null);

        assertFalse(result);
        verify(adminCustomerRepository).existsById(null);
        verify(adminCustomerRepository, never()).deleteById(any(Long.class));
    }

    @Test
    void UT_Service_0058_isEmailDuplicate_正常系() {
        // 重複するメールアドレス
        when(adminCustomerRepository.findByEmail("existing@example.com"))
                .thenReturn(testCustomer1);

        boolean result = adminCustomerService.isEmailDuplicate("existing@example.com", null);

        assertTrue(result);
        verify(adminCustomerRepository).findByEmail("existing@example.com");
    }

    @Test
    void UT_Service_0059_isEmailDuplicate_正常系() {
        // 重複しないメールアドレス
        when(adminCustomerRepository.findByEmail("new@example.com"))
                .thenReturn(null);

        boolean result = adminCustomerService.isEmailDuplicate("new@example.com", null);

        assertFalse(result);
        verify(adminCustomerRepository).findByEmail("new@example.com");
    }

    @Test
    void UT_Service_0060_isEmailDuplicate_正常系() {
        // 自分自身のメールアドレス（更新時）
        when(adminCustomerRepository.findByEmail("existing@example.com"))
                .thenReturn(testCustomer1);

        boolean result = adminCustomerService.isEmailDuplicate("existing@example.com", 1L);

        assertFalse(result);
        verify(adminCustomerRepository).findByEmail("existing@example.com");
    }
}