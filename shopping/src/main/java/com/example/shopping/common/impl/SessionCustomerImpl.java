package com.example.shopping.common.impl;

import com.example.shopping.common.SessionCustomer;
import com.example.shopping.common.SessionUtils;
import com.example.shopping.model.dto.UserCustomerDto;
import com.example.shopping.model.form.UserCustomerForm;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 顧客セッション管理実装クラス
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SessionCustomerImpl implements SessionCustomer {

    private final SessionUtils sessionUtils;

    @Override
    public UserCustomerDto getCustomer(HttpSession session) {
        return sessionUtils.getSessionObject(
                session,
                SessionUtils.SessionKeys.CUSTOMER_SESSION,
                null);
    }

    @Override
    public void setCustomer(UserCustomerDto customer, HttpSession session) {
        if (customer == null) {
            log.warn("Customer is null");
            return;
        }

        sessionUtils.setSessionObject(session, SessionUtils.SessionKeys.CUSTOMER_SESSION, customer);
        log.debug("Customer set in session: id={}, name={}", customer.getId(), customer.getName());
    }

    @Override
    public void clearCustomer(HttpSession session) {
        sessionUtils.removeSessionObject(session, SessionUtils.SessionKeys.CUSTOMER_SESSION);
        log.debug("Customer cleared from session");
    }

    @Override
    public boolean hasCustomer(HttpSession session) {
        UserCustomerDto customer = getCustomer(session);
        return customer != null;
    }

    @Override
    public UserCustomerForm getCustomerForm(HttpSession session) {
        UserCustomerDto customerDto = getCustomer(session);
        if (customerDto == null) {
            return null;
        }

        UserCustomerForm form = new UserCustomerForm();
        form.setName(customerDto.getName());
        form.setEmail(customerDto.getEmail());
        form.setPhone(customerDto.getPhone());
        form.setAddress(customerDto.getAddress());

        return form;
    }

    @Override
    public void setCustomerForm(UserCustomerForm customerForm, HttpSession session) {
        if (customerForm == null) {
            log.warn("CustomerForm is null");
            return;
        }

        UserCustomerDto customerDto = new UserCustomerDto();
        customerDto.setName(customerForm.getName());
        customerDto.setEmail(customerForm.getEmail());
        customerDto.setPhone(customerForm.getPhone());
        customerDto.setAddress(customerForm.getAddress());

        setCustomer(customerDto, session);
        log.debug("CustomerForm set in session: name={}, email={}", customerForm.getName(), customerForm.getEmail());
    }
}