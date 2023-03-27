package com.plintus.sweetstore.service;

import com.plintus.sweetstore.domain.Role;
import com.plintus.sweetstore.domain.User;
import com.plintus.sweetstore.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private MailSender mailSender;
    @Autowired
    private UserRepository userRep;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRep.findByUsername(username);
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
        return userRep.findAll();
    }

    public boolean saveUser(User user) {
        User userFromDB = userRep.findByUsername(user.getUsername());
        if (userFromDB != null) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(user.getPassword());
        userRep.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Приветствую Вас, %s, в нашем магазине вкусностей!\n" +
                            "Для подтверждения перейдите по ссылке: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }

        return true;
    }

    public boolean updateUser(String firstName, String lastName,
                              String dadName, String phone) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRep.findByUsername(auth.getName());
        if (user != null) {
            boolean needToSave = false;
            String newCustFullame = UtilService
                    .getCustomerFullname(firstName, lastName, dadName);
            if (user.getCustFullname() == null
                    || !Objects.equals(user.getCustFullname(), newCustFullame)) {
                user.setCustFullname(newCustFullame);
                needToSave = true;
            }
            if (user.getPhone() == null) {
                user.setPhone(phone);
                needToSave = true;
            }
            if (needToSave) {
                userRep.save(user);
            }
            return true;
        }
        return false;
    }

    public User getUserByUsername(String name) {
        return userRep.findByUsername(name);
    }

    public boolean activateUser(String code) {
        User user = userRep.findByActivationCode(code);

        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        userRep.save(user);
        return true;
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRep.save(user);
    }

    public String[] getUserFullname() {
        User user = getCurrentAuthUser();
        return UtilService.getCustomerArrayFullname(user.getCustFullname());
    }

    public User getCurrentAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRep.findByUsername(auth.getName());
    }

    public String getUserPhone() {
        User user = getCurrentAuthUser();
        return user.getPhone();
    }
}
