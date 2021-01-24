package com.project.Teampl.serviceImpl;

import com.project.Teampl.exception.ResourceNotFoundException;
import com.project.Teampl.model.user.User;
import com.project.Teampl.repository.UserRepository;
import com.project.Teampl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public List<User> findAll(){
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(e->users.add(e));
        return users;
    }

    @Override
    public User findById(Long useridx) {
        User user = userRepository.findById(useridx).orElseThrow(()-> new ResourceNotFoundException("User", "useridx", useridx));
        return user;
    }

    @Override
    public User findByUserid(String userid) {
        return userRepository.findByUserid(userid);
    }

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        User findUser = userRepository.findByUserid(userid);

//        User findUser = userRepository.findByUserid(userid)
//        if(findUser == null) {
//            throw new UsernameNotFoundException(userid);
//        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return findUser.getRole();
            }
        });

        return org.springframework.security.core.userdetails.User.builder()
                .username(findUser.getIdx().toString())
                .password(findUser.getUserpw())
                .authorities(authorities)
                .build();
//        return new org.springframework.security.core.userdetails.User(findUser.getUserid(), findUser.getUserpw(), authorities);
    }

    @Override
    @Transactional
    public void deleteById(Long useridx) {
        userRepository.deleteById(useridx);
    }

    @Override
    @Transactional
    public User save(User user) {
        user.setUserpw(encoder.encode(user.getUserpw()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    public void updateById(Long useridx, User user) {
        User localUser = userRepository.findById(useridx).orElseThrow(()-> new ResourceNotFoundException("User", "useridx",useridx));

        if(user.getRole().equals("admin")) {
            user.setRole("ROLE_ADMIN");
        } else if(user.getRole().equals("user")) {
            user.setRole("ROLE_USER");
        }

        localUser.setUsername(user.getUsername());
        localUser.setUserid(user.getUserid());
        localUser.setUserpw(encoder.encode(user.getUserpw()));
        localUser.setEmail(user.getEmail());
        localUser.setRole(user.getRole());

    }
}
