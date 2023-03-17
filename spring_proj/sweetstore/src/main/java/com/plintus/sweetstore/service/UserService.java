package com.plintus.sweetstore.service;

import com.plintus.sweetstore.domain.Role;
import com.plintus.sweetstore.domain.User;
import com.plintus.sweetstore.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository user_rep;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = user_rep.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь " + username + " не найден");
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles())
                .build();
    }

    public List<User> findAll() {
        return user_rep.findAll();
    }

    public boolean saveUser(User user) {
        User user_from_db = user_rep.findByUsername(user.getUsername());
        if (user_from_db != null) {
            return false;
        }
//        user.setRole("USER");
        user.setRoles(Collections.singleton(Role.USER));
        user_rep.save(user);
        return true;
    }
}
