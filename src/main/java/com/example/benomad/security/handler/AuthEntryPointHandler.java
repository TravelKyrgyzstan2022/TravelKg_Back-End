package com.example.benomad.security.handler;

import com.example.benomad.security.jwt.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AuthEntryPointHandler implements AuthenticationEntryPoint {

    private static final String AUTHORIZATION = "Authorization";
    @Autowired
    JwtUtils jwtUtils;

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String headerAuth = request.getHeader(AUTHORIZATION);
        String message = authException.getMessage();
        String error = "Unauthorized";

        if (StringUtils.hasText(headerAuth)) {
            String token = headerAuth.substring(7);
            if(headerAuth.startsWith("Bearer ")){
                try {
                    Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
                } catch (SignatureException e) {
                    message = ("Invalid JWT signature: " + e.getMessage());
                } catch (MalformedJwtException e) {
                    message = ("Invalid JWT token: " + e.getMessage());
                } catch (ExpiredJwtException e) {
                    message = ("JWT token is expired: " + e.getMessage());
                } catch (UnsupportedJwtException e) {
                    message = ("JWT token is unsupported: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    message = ("JWT claims string is empty: " + e.getMessage());
                }
            }else{
                message = "Invalid JWT type: Bearer not found";
            }
        }
//        log.error("Unauthorized error: {}", authException.getMessage());

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", error);
        body.put("message", message);
        body.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
