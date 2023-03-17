package com.plintus.sweetstore.config.manager;

import com.plintus.sweetstore.domain.User;
import com.plintus.sweetstore.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthManager implements AuthenticationManager, AuthenticationProvider {
    @Autowired
    private UserRepository user_rep;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String username = auth.getName();
        String password = auth.getCredentials().toString();

        User user = user_rep.findByUsername(username);

        if (user == null) {
            throw new BadCredentialsException("Неизвестный пользователь: " + username);
        }
        if (!password.equals(user.getPassword())) {
            throw new BadCredentialsException("Неверный пароль");
        }
        UserDetails principal = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles())
                .build();
        return new UsernamePasswordAuthenticationToken(
                principal, password, principal.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }


}
