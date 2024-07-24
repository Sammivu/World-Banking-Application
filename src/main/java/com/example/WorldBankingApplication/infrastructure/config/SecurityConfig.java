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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
                                antMatcher(HttpMethod.GET, "/api/auth/confirm"),
                                antMatcher(HttpMethod.GET, "/api/auth/confirm-forgot-password")

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

       // security.cors(customizer -> customizer.configurationSource(corsConfigurationSource()));

        return security.build();
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("*")); // Set your allowed origins here
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // Set your allowed HTTP methods
//        configuration.setAllowedHeaders(Arrays.asList("*")); // Set your allowed headers (e.g., Content-Type, Authorization)
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration); // Apply CORS configuration to all paths
//
//        return source;
//    }
}
