package com.randomprogramming.explorer.services;

import com.randomprogramming.explorer.entities.Person;
import com.randomprogramming.explorer.repositories.PersonRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    final private PersonRepository personRepository;

    public CustomUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Person person = personRepository.findByUsername(username);

        if (person == null) {
            throw new UsernameNotFoundException("User " + username + " was not found.");
        }

        List<GrantedAuthority> listRole = new ArrayList<GrantedAuthority>();

        listRole.add(new SimpleGrantedAuthority("USER")); // TODO: Fix/implement roles

        return User
                .withUsername(person.getUsername())
                .password(person.getPassword())
                .authorities(listRole)
                .disabled(!person.isEnabled())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .build();
    }
}
