package pm.workout.helper.domain.user.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pm.workout.helper.api.external.authentication.request.SignInUserRequest;
import pm.workout.helper.domain.user.authentication.dto.UserAuthDetailsDto;
import pm.workout.helper.domain.user.authentication.exception.UserNotFoundException;
import pm.workout.helper.domain.user.dto.AppUserDto;
import pm.workout.helper.infrastructure.security.UserPrincipal;
import pm.workout.helper.infrastructure.security.jwt.JwtProvider;

@Service
class AuthenticationServiceImpl implements AuthenticationService{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserAuthDetailsDto signInAndReturnJwt(SignInUserRequest signInUserRequest){
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInUserRequest.getUsername(), signInUserRequest.getPassword()));
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String jwt = jwtProvider.generateJwtToken(userPrincipal, userPrincipal.getUser().getUserId());

            AppUserDto signedInUser = userPrincipal.getUser();
            if (!passwordEncoder.matches(signInUserRequest.getPassword(), signedInUser.getPassword())) {
                throw new IllegalStateException("Wrong passwor");
            }

            return new UserAuthDetailsDto(signedInUser.getUserId(), signedInUser.getEmail(),jwt, 10000l);
        } catch (AuthenticationException e){
            throw new UserNotFoundException(String.format("User %s not found", signInUserRequest.getUsername()));
        }

    }
}
