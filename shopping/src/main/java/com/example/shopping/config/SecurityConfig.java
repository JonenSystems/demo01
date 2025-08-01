package com.example.shopping.config;

import com.example.shopping.common.AdminUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security設定クラス
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final AdminUserDetailsService adminUserDetailsService;

        public SecurityConfig(AdminUserDetailsService adminUserDetailsService) {
                this.adminUserDetailsService = adminUserDetailsService;
        }

        @Bean
        @Profile("dev")
        public SecurityFilterChain devFilterChain(HttpSecurity http) throws Exception {
                // 開発環境用：H2コンソール有効、CSRF保護一部除外
                return http
                                // CSRF保護の設定（H2コンソールは除外）
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/h2-console/**"))

                                // 認証設定
                                .authorizeHttpRequests(authz -> authz
                                                // 管理者機能の保護
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                // 公開URLの設定
                                                .requestMatchers("/admin/login", "/images/**", "/css/**", "/js/**",
                                                                "/h2-console/**")
                                                .permitAll()
                                                // その他のURLは認証不要
                                                .anyRequest().permitAll())

                                // フォームログイン設定
                                .formLogin(form -> form
                                                .loginPage("/admin/login")
                                                .loginProcessingUrl("/admin/login")
                                                .defaultSuccessUrl("/admin/products", true)
                                                .failureUrl("/admin/login?error=true")
                                                .usernameParameter("username")
                                                .passwordParameter("password")
                                                .permitAll())

                                // UserDetailsServiceの設定
                                .userDetailsService(adminUserDetailsService)

                                // ログアウト設定
                                .logout(logout -> logout
                                                .logoutUrl("/admin/logout")
                                                .logoutSuccessUrl("/admin/login?logout=true")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())

                                // セッション管理設定
                                .sessionManagement(session -> session
                                                .maximumSessions(1) // 同一ユーザーの同時セッション数を1に制限
                                                .expiredUrl("/admin/login?expired=true"))

                                // ヘッダー設定（H2コンソール用）
                                .headers(headers -> headers
                                                .frameOptions(frame -> frame.sameOrigin()))

                                .build();
        }

        @Bean
        @Profile("prod")
        public SecurityFilterChain prodFilterChain(HttpSecurity http) throws Exception {
                // 本番環境用：セキュリティ強化
                return http
                                // CSRF保護の完全有効化（デフォルトで有効）

                                // 認証設定
                                .authorizeHttpRequests(authz -> authz
                                                // 管理者機能の保護
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                // 公開URLの設定
                                                .requestMatchers("/admin/login", "/images/**", "/css/**", "/js/**")
                                                .permitAll()
                                                // その他のURLは認証不要
                                                .anyRequest().permitAll())

                                // フォームログイン設定
                                .formLogin(form -> form
                                                .loginPage("/admin/login")
                                                .loginProcessingUrl("/admin/login")
                                                .defaultSuccessUrl("/admin/products", true)
                                                .failureUrl("/admin/login?error=true")
                                                .usernameParameter("username")
                                                .passwordParameter("password")
                                                .permitAll())

                                // UserDetailsServiceの設定
                                .userDetailsService(adminUserDetailsService)

                                // ログアウト設定
                                .logout(logout -> logout
                                                .logoutUrl("/admin/logout")
                                                .logoutSuccessUrl("/admin/login?logout=true")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())

                                // セッション管理設定
                                .sessionManagement(session -> session
                                                .maximumSessions(1) // 同一ユーザーの同時セッション数を1に制限
                                                .expiredUrl("/admin/login?expired=true"))

                                // セキュリティヘッダーの強化
                                .headers(headers -> headers
                                                .frameOptions(frame -> frame.deny()) // クリックジャッキング対策
                                                .contentSecurityPolicy("default-src 'self'") // CSP
                                )

                                .build();
        }

        @Bean
        @Profile("dev")
        public PasswordEncoder devPasswordEncoder() {
                return new BCryptPasswordEncoder(10); // 開発環境：強度10
        }

        @Bean
        @Profile("prod")
        public PasswordEncoder prodPasswordEncoder() {
                return new BCryptPasswordEncoder(12); // 本番環境：強度12
        }
}