package com.example.shopping.common.impl;

import com.example.shopping.common.SessionOrder;
import com.example.shopping.common.SessionUtils;
import com.example.shopping.model.dto.UserOrderDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 注文セッション管理実装クラス
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SessionOrderImpl implements SessionOrder {

    private final SessionUtils sessionUtils;

    @Override
    public UserOrderDto getOrder(HttpSession session) {
        return sessionUtils.getSessionObject(
                session,
                SessionUtils.SessionKeys.ORDER_SESSION,
                null);
    }

    @Override
    public void setOrder(UserOrderDto order, HttpSession session) {
        if (order == null) {
            log.warn("Order is null");
            return;
        }

        sessionUtils.setSessionObject(session, SessionUtils.SessionKeys.ORDER_SESSION, order);
        log.debug("Order set in session: orderNumber={}", order.getOrderNumber());
    }

    @Override
    public void clearOrder(HttpSession session) {
        sessionUtils.removeSessionObject(session, SessionUtils.SessionKeys.ORDER_SESSION);
        log.debug("Order cleared from session");
    }

    @Override
    public boolean hasOrder(HttpSession session) {
        UserOrderDto order = getOrder(session);
        return order != null;
    }
}