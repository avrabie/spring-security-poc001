package ai.sper.helloworld001.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;


@EnableWebFluxSecurity
public class MySecurityConfigurer {

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user1 = User
                .withUsername("user")
                .password(passwordEncoder().encode("password"))
//                .roles("USER")
                .authorities("ROLE_INNA", "ROLE_USER")
                .build();
        UserDetails user2 = User
                .withUsername("company")
                .password(passwordEncoder().encode("password"))
                .roles("COMPANY")
                .build();
        UserDetails user3 = User
                .withUsername("admin")
                .password(passwordEncoder().encode("password"))
                .roles("ADMIN", "SUPER_ADMIN", "USER")
                .build();
        return new MapReactiveUserDetailsService(user1, user2, user3);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http
                .authorizeExchange()
                .pathMatchers("/").permitAll()
//                .pathMatchers("/admin").hasAuthority("ROLE_ADMIN")
                .pathMatchers("/user").hasAuthority("ROLE_INNA")
//                .pathMatchers("/company").hasAuthority("ROLE_SUPER_ADMIN")
                .pathMatchers("/company").hasAnyAuthority("ROLE_USER", "ROLE_COMPANY")
                .anyExchange()
                .authenticated()
                .and()
                .httpBasic()
                .and()
                .formLogin()
                .and()
                .logout()
                ;
        http
                .csrf()
                .disable();

        return http.build();
    }


}
