package com.example.shopping.config;

import com.example.shopping.common.AdminUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                // CSRF保護の設定
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/h2-console/**") // H2コンソールは除外
                                )

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

                                // ヘッダー設定
                                .headers(headers -> headers
                                                .frameOptions(frame -> frame.sameOrigin()) // H2コンソール用
                                );

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder(10); // 強度10でBCryptエンコーダーを作成
        }
}