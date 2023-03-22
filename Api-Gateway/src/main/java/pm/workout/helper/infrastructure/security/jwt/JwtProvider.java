package pm.workout.helper.infrastructure.security.jwt;

import org.springframework.security.core.Authentication;
import pm.workout.helper.infrastructure.security.UserPrincipal;

import javax.servlet.http.HttpServletRequest;

public interface JwtProvider {
    String generateJwtToken(UserPrincipal auth, Long userId);
    Authentication getAuthentication(HttpServletRequest request);
    boolean isTokenValid(HttpServletRequest request);
}
