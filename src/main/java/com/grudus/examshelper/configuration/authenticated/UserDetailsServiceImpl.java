package com.grudus.examshelper.configuration.authenticated;

import com.grudus.examshelper.users.User;
import com.grudus.examshelper.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsernameWithRoles(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user: " + username));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                generateAuthorities(user));
    }

    static List<GrantedAuthority> generateAuthorities(User user) {
        return user.getRoles().stream()
                .map(role -> "ROLE_" + role.getName().toString())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

    }
}
