package com.example.shopping.repository.impl;

import com.example.shopping.model.entity.User;
import com.example.shopping.repository.AdminUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 管理者ユーザーリポジトリ実装クラス
 */
@Repository
@Transactional
@Slf4j
public class AdminUserRepositoryImpl implements AdminUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> findByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);

        try {
            User user = query.getSingleResult();
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findAdminByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username AND u.role = :role", User.class);
        query.setParameter("username", username);
        query.setParameter("role", User.UserRole.ADMIN);

        try {
            User user = query.getSingleResult();
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findEnabledAdminByUsername(String username) {
        log.debug("有効な管理者ユーザーを検索: username={}", username);
        
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username AND u.role = :role AND u.enabled = :enabled",
                User.class);
        query.setParameter("username", username);
        query.setParameter("role", User.UserRole.ADMIN);
        query.setParameter("enabled", true);

        try {
            User user = query.getSingleResult();
            log.debug("ユーザーが見つかりました: id={}, username={}, role={}, enabled={}", 
                    user.getId(), user.getUsername(), user.getRole(), user.isEnabled());
            return Optional.of(user);
        } catch (Exception e) {
            log.debug("ユーザーが見つかりませんでした: username={}, error={}", username, e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> updateLastLogin(Long userId) {
        // ユーザーを取得
        Optional<User> userOpt = Optional.ofNullable(entityManager.find(User.class, userId));

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLastLogin(LocalDateTime.now());
            entityManager.merge(user);
            return Optional.of(user);
        }

        return Optional.empty();
    }

    @Override
    public boolean existsByUsername(String username) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class);
        query.setParameter("username", username);

        Long count = query.getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existsAdminByUsername(String username) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.username = :username AND u.role = :role", Long.class);
        query.setParameter("username", username);
        query.setParameter("role", User.UserRole.ADMIN);

        Long count = query.getSingleResult();
        return count > 0;
    }
}