package com.example.benomad.security.config;

import com.example.benomad.security.filter.JwtTokenFilter;
import com.example.benomad.security.handler.AuthAccessDeniedHandler;
import com.example.benomad.security.handler.AuthEntryPointHandler;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig{

    private final AuthEntryPointHandler unauthorizedHandler;
    private final AuthAccessDeniedHandler accessDeniedHandler;
    private final AuthenticationConfiguration authenticationConfiguration;

    private final String[] PERMIT_ALL = {
            "/api/account/**",
            "/documentation/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/v3/api-docs.yaml",
            "/error"
    };

    private final String[] USER_COMMON = {
            "/api/v1/user/**",
    };

    private final String[] USER = {
            "/api/v1/places/**",
            "/api/v1/blogs/**"
    };

    private final String[] ADMIN_COMMON = {
            "/api/v1/admin/**",
    };

    private final String[] SUPER_ADMIN_ONLY = {
            "/api/superadmin/**"
    };

    private final String[] ADMIN_GET = {
            "/actuator/**",
    };

    private final String[] PERMIT_ALL_GET = {
            "/api/v1/**"
    };

    private final String[] ADMIN_POST = {
            "/api/v1/admin/**"
    };

    private final String[] ADMIN_PUT = {
            "/api/v1/admin/**"
    };

    private final String[] ADMIN_DELETE = {
            "/api/v1/admin/**"
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtTokenFilter authenticationJwtTokenFilter() throws Exception {
        return new JwtTokenFilter(authenticationManager(authenticationConfiguration));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors()
                .and()
                    .authorizeRequests()
                    .antMatchers(USER_COMMON).hasAnyRole("USER", "ADMIN", "SUPERADMIN")
                    .antMatchers(HttpMethod.POST, USER).hasAnyRole("USER", "ADMIN", "SUPERADMIN")
                    .antMatchers(HttpMethod.DELETE, USER).hasAnyRole("USER", "ADMIN", "SUPERADMIN")
                    .antMatchers(ADMIN_COMMON).hasAnyRole("ADMIN", "SUPERADMIN")
                    .antMatchers(SUPER_ADMIN_ONLY).hasRole("SUPERADMIN")
                    .antMatchers(PERMIT_ALL).permitAll()
                    .antMatchers(HttpMethod.GET, ADMIN_GET).hasAnyRole("ADMIN", "SUPERADMIN")
                    .antMatchers(HttpMethod.GET, PERMIT_ALL_GET).permitAll()
                    .antMatchers(HttpMethod.POST, ADMIN_POST).hasAnyRole("ADMIN", "SUPERADMIN")
                    .antMatchers(HttpMethod.DELETE, ADMIN_DELETE).hasAnyRole("ADMIN", "SUPERADMIN")
                    .antMatchers(HttpMethod.PUT, ADMIN_PUT).hasAnyRole("ADMIN", "SUPERADMIN")
                    .antMatchers("/api/v1/**").hasAnyRole("USER")
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