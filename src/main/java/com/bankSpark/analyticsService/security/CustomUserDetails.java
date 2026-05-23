package com.bankSpark.analyticsService.security;

import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//Интерфейс для того чтобы внутренние механизмы брали пользователя не из users , а из analyst
public class CustomUserDetails implements UserDetails {

    private final Analyst analyst;

    public CustomUserDetails(Analyst analyst) {
        this.analyst = analyst;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(analyst != null && analyst.getRole() != null) {
            return List.of(new SimpleGrantedAuthority(analyst.getRole()));
        }

        return List.of();
    }

    @Override
    public String getPassword() {
        return analyst.getPassword();
    }

    @Override
    public String getUsername() {
        return analyst.getLogin();
    }

}