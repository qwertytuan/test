package com.example.demo.Security;

import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return (UserDetailsService) userService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService((UserDetailsService) userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(httpForm -> {
                    httpForm.loginPage("/req/login").permitAll();
                    httpForm.defaultSuccessUrl("/index", true);
                })
                .rememberMe(rememberMeConfigurer -> {
                    rememberMeConfigurer
                            .tokenValiditySeconds(5)
                            .key("uniqueAndSecret")
                            .userDetailsService(userDetailsService());
                })
                .logout(logout -> {
                    logout
                            .logoutUrl("/req/logout")
                            .logoutSuccessUrl("/index")
                            .invalidateHttpSession(true)
                            .clearAuthentication(true)
                            .deleteCookies("JSESSIONID");
                })
                .sessionManagement(sessionManagementConfigurer -> {
                    sessionManagementConfigurer
                            .invalidSessionUrl("/req/login?session=expired")
                            .maximumSessions(1)
                            .expiredUrl("/req/login?session=expired");
                })
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/req/signup",
                                    "/static/**",
                                    "/css/**",
                                    "/js/**",
                                    "/index",
                                    "/",
                                    "/error",
                                    "/posts/**",
                                    "/api/**",
                                    "/uploads/**",
                                    "/user/posts/{id}")
                            .permitAll();
                    registry.requestMatchers("/admin").hasRole("ADMIN");
                    registry.anyRequest().authenticated();
                })
                .build();
    }
}