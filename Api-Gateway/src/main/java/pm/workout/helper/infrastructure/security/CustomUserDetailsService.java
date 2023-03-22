package pm.workout.helper.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pm.workout.helper.domain.user.dto.AppUserDto;
import pm.workout.helper.infrastructure.services.UserService;

import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

//    @Autowired
//    private UserService userService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        AppUser user = userService.getUserByLogin(username).orElseThrow(() -> new UsernameNotFoundException("User with login "+username+" not found"));
//
//        Set<GrantedAuthority> authorities = Set.of(SecurityUtils.convertToAuthority(user.getUserAuthority().name()));
//        return UserPrincipal.builder()
//                .user(user)
//                .id(user.getId())
//                .username(username)
//                .password(user.getPassword())
//                .authorities(authorities)
//                .build();
//    }
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUserDto user = userService.signInUser(username);
        Set<GrantedAuthority> authorities = Set.of(SecurityUtils.convertToAuthority(user.getAuthority().name()));
        return UserPrincipal.builder()
                .user(user)
                .id(user.getUserId())
                .username(username)
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
