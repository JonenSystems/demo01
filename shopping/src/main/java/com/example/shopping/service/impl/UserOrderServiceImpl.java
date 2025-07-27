package com.example.shopping.service.impl;

import com.example.shopping.model.dto.UserCartItemDto;
import com.example.shopping.model.dto.UserCustomerDto;
import com.example.shopping.model.dto.UserOrderDto;
import com.example.shopping.model.entity.Customer;
import com.example.shopping.model.entity.Order;
import com.example.shopping.model.entity.OrderDetail;
import com.example.shopping.model.entity.Product;
import com.example.shopping.repository.UserCartRepository;
import com.example.shopping.repository.UserCustomerRepository;
import com.example.shopping.repository.UserOrderDetailRepository;
import com.example.shopping.repository.UserOrderRepository;
import com.example.shopping.service.UserOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ユーザー注文Service実装クラス
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserOrderServiceImpl implements UserOrderService {

    private final UserOrderRepository userOrderRepository;
    private final UserCustomerRepository userCustomerRepository;
    private final UserCartRepository userCartRepository;
    private final UserOrderDetailRepository userOrderDetailRepository;

    @Override
    public UserOrderDto getOrderConfirmData() {
        log.debug("Getting order confirm data");
        // セッション操作はController層で行うため、ここではビジネスロジックのみ
        return null; // Controller層で実装
    }

    @Override
    public boolean saveCustomerInfo(UserCustomerDto customerDto) {
        if (customerDto == null) {
            log.warn("CustomerDto is null");
            return false;
        }

        log.debug("Saving customer info: name={}, email={}", customerDto.getName(), customerDto.getEmail());

        // バリデーション
        if (customerDto.getName() == null || customerDto.getName().trim().isEmpty()) {
            log.warn("Customer name is required");
            return false;
        }

        if (customerDto.getEmail() == null || customerDto.getEmail().trim().isEmpty()) {
            log.warn("Customer email is required");
            return false;
        }

        log.debug("Customer info validation successful");
        return true;
    }

    @Override
    public UserOrderDto createOrder() {
        log.debug("Creating order");
        // セッション操作はController層で行うため、ここではビジネスロジックのみ
        return null; // Controller層で実装
    }

    @Override
    public UserOrderDto getOrderByOrderNumber(String orderNumber) {
        log.debug("Getting order by order number: {}", orderNumber);

        if (orderNumber == null || orderNumber.trim().isEmpty()) {
            log.warn("Order number is required");
            return null;
        }

        Optional<Order> orderOpt = userOrderRepository.findByOrderNumber(orderNumber);
        if (orderOpt.isEmpty()) {
            log.warn("Order not found: orderNumber={}", orderNumber);
            return null;
        }

        Order order = orderOpt.get();
        return UserOrderDto.fromEntity(order);
    }

    @Override
    public List<UserOrderDto> getOrderHistoryByCustomerId(Long customerId) {
        log.debug("Getting order history by customer id: {}", customerId);

        if (customerId == null) {
            log.warn("Customer ID is required");
            return List.of();
        }

        List<Order> orders = userOrderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
        return orders.stream()
                .map(UserOrderDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public String generateOrderNumber() {
        log.debug("Generating order number");
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public UserOrderDto createOrderConfirmData(List<UserCartItemDto> cartItems, UserCustomerDto customerDto) {
        log.debug("Creating order confirm data: cartItems={}, customer={}",
                cartItems != null ? cartItems.size() : 0, customerDto != null ? customerDto.getName() : "null");

        if (cartItems == null || cartItems.isEmpty()) {
            log.warn("Cart items are required");
            return null;
        }

        if (customerDto == null) {
            log.warn("Customer data is required");
            return null;
        }

        // 注文確認データを作成
        UserOrderDto orderDto = new UserOrderDto();
        orderDto.setOrderNumber(generateOrderNumber());
        orderDto.setCustomer(customerDto);
        orderDto.setOrderItems(cartItems);
        orderDto.setTotalAmount(calculateTotalAmount(cartItems));
        orderDto.setStatus("PENDING");
        orderDto.setCreatedAt(LocalDateTime.now());

        return orderDto;
    }

    @Override
    @Transactional
    public UserOrderDto saveOrderToDatabase(List<UserCartItemDto> cartItems, UserCustomerDto customerDto) {
        log.debug("Saving order to database: cartItems={}, customer={}",
                cartItems != null ? cartItems.size() : 0, customerDto != null ? customerDto.getName() : "null");

        if (cartItems == null || cartItems.isEmpty()) {
            log.warn("Cart items are required");
            return null;
        }

        if (customerDto == null) {
            log.warn("Customer data is required");
            return null;
        }

        try {
            // 顧客情報を保存または更新
            Customer customer = saveOrUpdateCustomer(customerDto);

            // 注文を作成
            Order order = new Order();
            order.setOrderNumber(generateOrderNumber());
            order.setCustomer(customer);
            order.setTotalAmount(calculateTotalAmount(cartItems));
            order.setStatus(Order.OrderStatus.PENDING);
            order.setCreatedAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());

            Order savedOrder = userOrderRepository.save(order);

            // 注文詳細を作成
            List<OrderDetail> orderDetails = cartItems.stream()
                    .map(cartItem -> createOrderDetail(savedOrder, cartItem))
                    .collect(Collectors.toList());

            userOrderDetailRepository.saveAll(orderDetails);

            log.debug("Order saved successfully: orderId={}", savedOrder.getId());
            return UserOrderDto.fromEntity(savedOrder);

        } catch (Exception e) {
            log.error("Error saving order to database", e);
            return null;
        }
    }

    /**
     * 合計金額を計算
     */
    private BigDecimal calculateTotalAmount(List<UserCartItemDto> cartItems) {
        return cartItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 注文詳細を作成
     */
    private OrderDetail createOrderDetail(Order order, UserCartItemDto cartItem) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        // 商品IDから商品を取得
        Optional<Product> productOpt = userCartRepository.findById(cartItem.getProductId());
        if (productOpt.isEmpty()) {
            throw new RuntimeException("Product not found: " + cartItem.getProductId());
        }
        orderDetail.setProduct(productOpt.get());
        orderDetail.setQuantity(cartItem.getQuantity());
        orderDetail.setUnitPrice(cartItem.getUnitPrice());
        orderDetail.setSubtotal(cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        orderDetail.setCreatedAt(LocalDateTime.now());
        orderDetail.setUpdatedAt(LocalDateTime.now());
        return orderDetail;
    }

    /**
     * 顧客情報を保存または更新
     */
    private Customer saveOrUpdateCustomer(UserCustomerDto customerDto) {
        // 既存の顧客を検索
        Optional<Customer> existingCustomerOpt = userCustomerRepository.findByEmail(customerDto.getEmail());

        if (existingCustomerOpt.isPresent()) {
            // 既存の顧客情報を更新
            Customer existingCustomer = existingCustomerOpt.get();
            existingCustomer.setName(customerDto.getName());
            existingCustomer.setPhone(customerDto.getPhone());
            existingCustomer.setAddress(customerDto.getAddress());
            existingCustomer.setUpdatedAt(LocalDateTime.now());
            return userCustomerRepository.save(existingCustomer);
        } else {
            // 新しい顧客を作成
            Customer newCustomer = customerDto.toEntity();
            newCustomer.setCreatedAt(LocalDateTime.now());
            newCustomer.setUpdatedAt(LocalDateTime.now());
            return userCustomerRepository.save(newCustomer);
        }
    }
}