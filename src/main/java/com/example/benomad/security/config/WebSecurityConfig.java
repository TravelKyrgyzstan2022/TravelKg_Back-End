package com.example.benomad.security.config;

import com.example.benomad.security.handler.AuthAccessDeniedHandler;
import com.example.benomad.security.handler.AuthEntryPointHandler;
import com.example.benomad.security.jwt.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AuthEntryPointHandler unauthorizedHandler;
    private final AuthAccessDeniedHandler accessDeniedHandler;
    private final String[] PERMIT_ALL_COMMON = {
            "/api/auth/**",
            "/documentation/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/v3/api-docs.yaml",
            "/error"
    };

    private final String[] ADMIN_GET = {
            "/actuator/**",
            "/api/v1/users",
            "/api/v1/users/**"
    };

    private final String[] PERMIT_ALL_GET = {
            "/api/v1/**"
    };

    private final String[] ADMIN_POST = {
            "/api/v1/**"
    };

    private final String[] ADMIN_PUT = {
            "/api/v1/**"
    };

    private final String[] ADMIN_DELETE = {
            "/api/v1/**"
    };


    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors()
                .and()
                    .authorizeRequests()
                    .antMatchers(PERMIT_ALL_COMMON).permitAll()
                    .antMatchers(HttpMethod.GET, ADMIN_GET).hasRole("ADMIN")
                    .antMatchers(HttpMethod.GET, PERMIT_ALL_GET).permitAll()
                    .antMatchers(HttpMethod.POST, ADMIN_POST).hasRole("ADMIN")
                    .antMatchers(HttpMethod.DELETE, ADMIN_DELETE).hasRole("ADMIN")
                    .antMatchers(HttpMethod.PUT, ADMIN_PUT).hasRole("ADMIN")
                    .anyRequest().authenticated()
                .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                    .accessDeniedHandler(accessDeniedHandler)
                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.
                addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*");
            }
        };
    }
}