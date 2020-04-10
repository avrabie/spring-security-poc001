package ai.sper.helloworld001.services;

import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

@Log4j2
class JwtTokenUtilTest {

    private final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    private final UserDetails user1 = User
            .withUsername("user")
            .password("password")
//                .roles("USER")
            .authorities("ROLE_INNA", "ROLE_USER")
            .build();

    @Test
    void generateTokenPrimary() {
        String jwt = jwtTokenUtil.generateToken(user1);
        Assertions.assertNotNull(jwt);
        Assertions.assertTrue(jwt.length() > 0);
        String claimFromToken = jwtTokenUtil.getClaimFromToken(jwt, Claims::getSubject);
        Assertions.assertEquals("user", claimFromToken);
    }

    @Test
    void generateToken() {
        String jwt = jwtTokenUtil.generateToken("Eric");
        log.info(jwt);
        Assertions.assertNotNull(jwt);
        Assertions.assertTrue(jwt.length() > 0);
        String claimFromToken = jwtTokenUtil.getClaimFromToken(jwt, Claims::getSubject);

        List<Map<String, String>> scopes = jwtTokenUtil.getClaimFromToken(jwt, claims -> claims.get("scopes", List.class));
        Map<String, String> map = (Map<String, String>) scopes.get(0);
        List<Map<String, String>> projects = jwtTokenUtil.getClaimFromToken(jwt, claims -> claims.get("projects", List.class));
        Map<String, String> projectLinkedMap = (Map<String, String>) projects.get(1);
        String projectName = (String) projectLinkedMap.get("projectName");
        String right = (String) projectLinkedMap.get("right");

        Assertions.assertEquals("Eric", claimFromToken);
        Assertions.assertEquals("ROLE_ADMIN", map.get("authority"));
        Assertions.assertEquals("Prj2", projectName);
        Assertions.assertEquals("READ", right);

    }


}