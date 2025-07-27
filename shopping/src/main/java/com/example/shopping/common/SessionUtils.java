package com.example.shopping.common;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * セッション操作共通ユーティリティクラス
 */
@Component
@Slf4j
public class SessionUtils {

    /**
     * セッションからオブジェクトを安全に取得し、存在しなければ初期化して返す
     * 
     * @param session      HttpSession
     * @param key          セッションキー
     * @param defaultValue デフォルト値
     * @param <T>          オブジェクトの型
     * @return セッションオブジェクトまたはデフォルト値
     */
    public <T> T getSessionObject(HttpSession session, String key, T defaultValue) {
        if (session == null) {
            log.warn("HttpSession is null for key: {}", key);
            return defaultValue;
        }

        try {
            @SuppressWarnings("unchecked")
            T value = (T) session.getAttribute(key);
            if (value == null) {
                log.debug("Session attribute not found for key: {}, setting default value", key);
                session.setAttribute(key, defaultValue);
                return defaultValue;
            }
            return value;
        } catch (Exception e) {
            log.error("Error getting session attribute for key: {}", key, e);
            return defaultValue;
        }
    }

    /**
     * 任意のオブジェクトを特定のキーで保存
     * 
     * @param session HttpSession
     * @param key     セッションキー
     * @param value   保存するオブジェクト
     */
    public void setSessionObject(HttpSession session, String key, Object value) {
        if (session == null) {
            log.warn("HttpSession is null for key: {}", key);
            return;
        }

        try {
            session.setAttribute(key, value);
            log.debug("Session attribute set for key: {}", key);
        } catch (Exception e) {
            log.error("Error setting session attribute for key: {}", key, e);
        }
    }

    /**
     * セッションから特定のキーのオブジェクトを削除
     * 
     * @param session HttpSession
     * @param key     セッションキー
     */
    public void removeSessionObject(HttpSession session, String key) {
        if (session == null) {
            log.warn("HttpSession is null for key: {}", key);
            return;
        }

        try {
            session.removeAttribute(key);
            log.debug("Session attribute removed for key: {}", key);
        } catch (Exception e) {
            log.error("Error removing session attribute for key: {}", key, e);
        }
    }

    /**
     * セッションを完全に無効化
     * 
     * @param session HttpSession
     */
    public void invalidateSession(HttpSession session) {
        if (session == null) {
            log.warn("HttpSession is null");
            return;
        }

        try {
            session.invalidate();
            log.debug("Session invalidated");
        } catch (Exception e) {
            log.error("Error invalidating session", e);
        }
    }

    /**
     * セッションキー定数
     */
    public static class SessionKeys {
        public static final String CART_SESSION = "CART_SESSION";
        public static final String CUSTOMER_SESSION = "CUSTOMER_SESSION";
        public static final String ORDER_SESSION = "ORDER_SESSION";
        public static final String USER_SESSION = "USER_SESSION";
    }
}