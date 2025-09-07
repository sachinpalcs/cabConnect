package com.project.uber.uberweb.configs;


import com.project.uber.uberweb.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private static final String[] PUBLIC_ROUTES = {"/auth/**"};

//    private static final String[] SWAGGER_WHITELIST = {  // this method also remove
//            "/swagger-ui/**",
//            "/v3/api-docs/**",
//            "/swagger-resources/**",
//            "/swagger-resources"
//    };

//    private static final String[] ACTUATOR_WHITELIST = {"/actuator/**"};  // this also remove

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .sessionManagement(sessionConfig -> sessionConfig
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrfConfig -> csrfConfig.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ROUTES).permitAll()
//                        .requestMatchers(SWAGGER_WHITELIST).permitAll()  // this line remove after check swagger api
//                        .requestMatchers(ACTUATOR_WHITELIST).permitAll()  // this line also remove
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//        oauth2 auth set here

        return httpSecurity.build();
    }
}
