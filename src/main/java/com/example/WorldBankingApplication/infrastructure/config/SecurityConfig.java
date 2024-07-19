package com.example.WorldBankingApplication.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.MatcherType.ant;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final  JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private  final JwtAuthenticationEntrypoint authenticationEntrypoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security)throws Exception{
        //To override Spring custom filter
        security.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        //These part takes care of the security, saying any url that matches the url here should be allowed
        // This permits any and all request that carries this pattern/url
        security.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/auth/**"),
                                antMatcher(HttpMethod.GET, "api/auth/confirm")
                                        )
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .exceptionHandling(exception-> exception.authenticationEntryPoint(authenticationEntrypoint))

                //Here can set a time for logout if the user is idel for a period set
                // Change the method put the time eg. 1000 * 60 * 15= 15mins an example was used in JwtService
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults());
        security.authenticationProvider(authenticationProvider);

        return security.build();
    }
}
