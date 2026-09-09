package fr.diginamic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1) Je veux HTTP Basic avec le paramétrage par défaut
        http.httpBasic(Customizer.withDefaults());

         // 2) Toute requête doit être authentifiée
        http.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/**").hasRole("USER").anyRequest().hasRole("ADMIN"));

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder getEncoder(){
        return new BCryptPasswordEncoder();
    }

}