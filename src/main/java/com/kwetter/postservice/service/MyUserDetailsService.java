package com.kwetter.postservice.service;

import com.kwetter.postservice.models.UserDetailsImpl;
import com.kwetter.postservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDetailsImpl loadUserByUsername(String jwt) throws UsernameNotFoundException {
        String username = jwtUtil.extractUsername(jwt);
        Long id = jwtUtil.extractId(jwt);
        ArrayList<String> roles = jwtUtil.extractRoles(jwt);

        return UserDetailsImpl.build(username, id, roles);
    }
}
