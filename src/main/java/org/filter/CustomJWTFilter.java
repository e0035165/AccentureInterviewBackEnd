package org.filter;

import org.entities.CustomUserDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.services.CustomUserDetailsService;
import org.utilities.jwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
//gunawan.ding@accenture.com

@Component
public class CustomJWTFilter extends OncePerRequestFilter {
    @Autowired
    private jwtService aes_service;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;


//    public JsonNode getBody(InputStream reader) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        StringBuffer buffer = new StringBuffer();
//        int i = 0;
//        while(i< reader.available())
//            buffer.append((char) reader.read());
//        return mapper.readTree(buffer.toString());
//    }

    public Map<String,Object> getTokenDecrypt(String token) {
        return aes_service.extractAllClaims(token);
    }

    public String getUsernameFromToken(String token) {
        return aes_service.getUsernameFromToken(token);
    }

    public String getPasswordFromToken(String token) {
        return aes_service.getPasswordFromToken(token);
    }

    public boolean isTokenValid(String token, String username, String password) {
        String currentUsername = getUsernameFromToken(token);
        String currentPassword = getPasswordFromToken(token);
        return currentUsername.equals(username)
                && currentPassword.equals(password)
                && aes_service.extractClaim(token,aes_service::isTokenExpired);
    }


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*"); // Allow all origins
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        if(request.getRequestURI().contains("/v1/personalAccount") ||
                request.getRequestURI().contains("/v1/payment")) {
            System.out.println(request.getHeader("Authorization"));
            String authToken = request.getHeader("Authorization").substring(7);
            Map<String,Object>credentials = getTokenDecrypt(authToken);
            if(credentials.get("username")!=null && SecurityContextHolder.getContext().getAuthentication() == null) {
                CustomUserDetails details = (CustomUserDetails) customUserDetailsService
                        .loadUserByUsername(credentials.get("username").toString());
                if(passwordEncoder.matches(credentials.get("password").toString(), details.getPassword())) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            details, null, details.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    System.out.println("Invalid credentials");
                }
            }

        }
        filterChain.doFilter(request, response);
    }
}
