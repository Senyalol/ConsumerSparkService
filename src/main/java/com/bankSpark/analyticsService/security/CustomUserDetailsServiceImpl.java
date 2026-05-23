package com.bankSpark.analyticsService.security;

import com.bankSpark.analyticsService.repository.AnalystRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final AnalystRepository analystRepository;

    @Autowired
    public CustomUserDetailsServiceImpl(AnalystRepository analystRepository) {
        this.analystRepository = analystRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CustomUserDetails(analystRepository.getAnalystByLogin(username).get());
    }
}