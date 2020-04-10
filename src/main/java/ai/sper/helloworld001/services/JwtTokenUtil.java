package ai.sper.helloworld001.services;

import ai.sper.helloworld001.models.SigProject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtTokenUtil {
    // TODO: 4/10/20 get from env properties
    private static final String SIGNING_KEY = "mySecret";

    // TODO: 4/10/20 Create a DB User and a Custom User that integrates (implements) UserDetails from security
    public String generateToken(UserDetails user) {
        final List authorities = user.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());

        // TODO: 4/10/20 authorities string must be extracted as a contract
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("authorities", authorities)
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 20 * 1000))
                .compact();
    }

    // TODO: 4/10/20 make a custom user to be used for the creation of a JWT
    public String generateToken(String user) {
        Claims claims = Jwts.claims().setSubject(user);
        claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        claims.put("projects", Arrays.asList(new SigProject("Prj1", SigProject.Right.WRITE),
                new SigProject("Prj2", SigProject.Right.READ)));

        return Jwts.builder()
                .setSubject(user)
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 20 * 1000)) // TODO: 4/10/20 externalize to env
                .compact();
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (
                username.equals(userDetails.getUsername())
                        && !isTokenExpired(token));
    }

    public List<String> getAuthoritiesFromToken(String token) {
        return getClaimFromToken(token, claims -> {
            return claims.get("authorities", List.class);
        });
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public <R> R getClaimFromToken(String token, Function<Claims, R> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

}
