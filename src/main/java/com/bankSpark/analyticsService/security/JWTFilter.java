package com.bankSpark.analyticsService.security;

import com.bankSpark.analyticsService.exception.ExtractJWTException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final CustomUserDetailsServiceImpl customUserDetailsService;

    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/analyst/reg",
            "/api/analyst/auth",
            "/swagger-ui.html",
            "/swagger-ui",
            "/v3/api-docs",
            "/swagger-resources",
            "/webjars",
            "/api-docs"
    );

    @Autowired
    public JWTFilter(JWTService jwtService, CustomUserDetailsServiceImpl customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String requestPath = request.getServletPath();

        if (isPublicEndpoint(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = getTokenFromRequest(request);

            if (token != null && jwtService.validateJwtToken(token)) {
                setUserDetailsToSecurityContextHolder(token);
            }

            filterChain.doFilter(request, response);

        } catch (ExtractJWTException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid JWT token");
        }

//        String token = getTokenFromRequest(request);
//
//        if(token != null && jwtService.validateJwtToken(token)) {
//            setUserDetailsToSecurityContextHolder(token);
//        }

    }

    public void setUserDetailsToSecurityContextHolder(String jwtToken){
        String login = jwtService.getLoginFromToken(jwtToken);
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(login);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }


    // Метод устанавливает пользователя токен с данными в контекст безопасности
//    public void setUserDetailsToSecurityContextHolder(String jwtToken){
//        String login = jwtService.getLoginFromToken(jwtToken);
//        String role = jwtService.getRoleFromToken(jwtToken);
//
//        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
//
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
////        UsernamePasswordAuthenticationToken authenticationToken =
////                new UsernamePasswordAuthenticationToken(login, null, authorities);
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//    }


    // Получит токен из запроса
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new ExtractJWTException();
    }

    private boolean isPublicEndpoint(String requestPath) {
        return PUBLIC_ENDPOINTS.stream().anyMatch(requestPath::startsWith);
    }

}