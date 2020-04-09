package ai.sper.helloworld001.configs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
public class MySecurityConfigurer {


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
                .pathMatchers("/**").permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .httpBasic()
                .and()
                .formLogin()
                .and()
                .logout()
        ;

        // this should be integrated with tht UI in order to have it enabled
        http
                .csrf()
                .disable();

        return http.build();
    }


}
