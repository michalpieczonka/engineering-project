package pm.workout.helper.infrastructure.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class SecurityUtils {

    private static final String ROLE_PREFIX = "ROLE_";
    static final String AUTH_HEADER = "Authorization";
    static final String AUTH_TOKEN_TYPE = "Bearer";
    static final String AUTH_TOKEN_PREFIX = AUTH_TOKEN_TYPE + " ";

    public static String extractAuthorizationTokenFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTH_HEADER);

        if (StringUtils.hasLength(bearerToken) && bearerToken.startsWith(AUTH_TOKEN_PREFIX)){
            return bearerToken.substring(7); //Token to jest 7 liter bo Bearer ma 7 liter
        }
        return null;
    }

    public static SimpleGrantedAuthority convertToAuthority(String role){
        String formattedRole = role.startsWith(ROLE_PREFIX) ? role: ROLE_PREFIX+role;
        return new SimpleGrantedAuthority(formattedRole);
    }
}
