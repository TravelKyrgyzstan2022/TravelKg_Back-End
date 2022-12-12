package com.example.benomad.security.filter;

import com.example.benomad.entity.User;
import com.example.benomad.exception.InvalidJwtException;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.impl.UserServiceImpl;
import com.example.benomad.util.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
public class JwtTokenFilter extends BasicAuthenticationFilter {

    public static final String AUTHORIZATION = "Authorization";

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    @Autowired
    private JwtUtils jwtUtils;

    public JwtTokenFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try{
            String jwt = parseJwt(request);
            if(jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUsernameFromJwtToken(jwt);
                UserDetails userDetails = userService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                User user = userRepository.findByEmail(username).orElse(null);
                if(user != null){
                    user.setLastVisitDate(LocalDateTime.now(ZoneId.of("Asia/Bishkek")));
                    userRepository.save(user);
                }
            }
        } catch (SignatureException e) {
            exceptionResolver.resolveException(request, response, null,
                    new InvalidJwtException("Invalid JWT signature: " + e.getMessage()));
        } catch (MalformedJwtException e) {
            exceptionResolver.resolveException(request, response, null,
                    new InvalidJwtException("Invalid JWT token: " + e.getMessage()));
        } catch (ExpiredJwtException e) {
            exceptionResolver.resolveException(request, response, null,
                    new InvalidJwtException("JWT token is expired: " + e.getMessage()));
        } catch (UnsupportedJwtException e) {
            exceptionResolver.resolveException(request, response, null,
                    new InvalidJwtException("JWT token is unsupported: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            exceptionResolver.resolveException(request, response, null,
                    new InvalidJwtException("JWT claims string is empty: " + e.getMessage()));
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }
}
