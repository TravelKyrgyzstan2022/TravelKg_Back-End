package com.example.benomad.security.handler;

import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.util.JwtUtils;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AuthEntryPointHandler implements AuthenticationEntryPoint {

    private static final String AUTHORIZATION = "Authorization";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

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
        int status = 401;

        if (StringUtils.hasText(headerAuth)) {
            String token = headerAuth.substring(7);
            if(headerAuth.startsWith("Bearer ")){

                try {
                    Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
                    String email = jwtUtils.getUsernameFromJwtToken(token);
                    if(!userRepository.existsByEmail(email)){
                        throw new ContentNotFoundException();
                    }
                } catch (SignatureException e) {
                    message = ("Invalid JWT signature: " + e.getMessage());
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } catch (MalformedJwtException e) {
                    message = ("Invalid JWT token: " + e.getMessage());
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } catch (ExpiredJwtException e) {
                    message = ("JWT token is expired: " + e.getMessage());
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } catch (UnsupportedJwtException e) {
                    message = ("JWT token is unsupported: " + e.getMessage());
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } catch (IllegalArgumentException e) {
                    message = ("JWT claims string is empty: " + e.getMessage());
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } catch (ContentNotFoundException e){
                    message = ("Invalid JWT: User doesn't exist");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            }else{
                message = "Invalid JWT type: Bearer not found";
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }
        log.error("Unauthorized error: {}", authException.getMessage());

        final Map<String, Object> body = new HashMap<>();
        body.put("status", status);
        body.put("message", message);
        body.put("timestamp", formatter.format(LocalDateTime.now(ZoneId.of("Asia/Bishkek"))));

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
