package com.project.Teampl.config.auth;

import com.project.Teampl.config.auth.PrincipalDetails;
import com.project.Teampl.exception.ResourceNotFoundException;
import com.project.Teampl.model.user.User;
import com.project.Teampl.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 메소드 호출 종료 시, '@AuthenticationPrincipal' 어노테이션이 생성된다!!
    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        User findUser = userRepository.findByUserid(userid);

        if(findUser == null) {
            throw new UsernameNotFoundException(userid);
        }

        return new PrincipalDetails(findUser);
    }
}
