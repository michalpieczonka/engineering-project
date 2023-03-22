package pm.workout.helper.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pm.workout.helper.infrastructure.security.SecurityUtils;
import pm.workout.helper.infrastructure.security.UserPrincipal;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtProviderImpl implements JwtProvider{

    @Value("${app.jwt.secret}")
    private String JWT_SECRET;

    @Value("${app.jwt.expiration-in-ms}")
    private Long JWT_EXPIRATION_TIME_MS;

    //Token jwt generowany na podstawie user info
    @Override
    public String generateJwtToken(UserPrincipal auth, Long userId){
        String authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        //Klucz symetryczny - ten sam klucz do zaszyfrowania i rozszyfrowania
        //Klucz asymetryczny - inny klucz do zaszyfrowania i inny klucz do zdeszyfrowania
        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(auth.getUsername()) //Username glowny identyfikator tokena JWT
                .claim("roles", authorities) //Zawiera tez role uzytkownika
                .claim("userId", userId) //Oraz id uzytkownika
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME_MS)) //czas wygasniecia
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }


    //Przeczytaj header z naglowka http "Authorization"  i stworz obiekt authentcation
    //Wykorzystuje klucz  symetryczny - ten sam algorytm do zaszyfrowania i zdeszyfrowania tokena
    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
       Claims claims = extractClaims(request);

       if(claims == null){
           return null;
       }

       String username = claims.getSubject();
       Long userId = claims.get("userId", Long.class);

        Set<GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
                .map(SecurityUtils::convertToAuthority)
                .collect(Collectors.toSet());

        UserDetails userDetails = UserPrincipal.builder()
                .username(username)
                .authorities(authorities)
                .id(userId)
                .build();

        if (username == null){
            return null;
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    private Claims extractClaims(HttpServletRequest request){
        String token =  SecurityUtils.extractAuthorizationTokenFromRequest(request);
        if (token == null){
            return null;
        }

        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Na endpointach waliduj token z headera

    @Override
    public boolean isTokenValid(HttpServletRequest request) {
        Claims claims = extractClaims(request);

        if(claims == null){
            return false;
        }

        return !claims.getExpiration().before(new Date());
    }
}
