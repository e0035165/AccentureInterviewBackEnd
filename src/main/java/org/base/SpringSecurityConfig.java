package org.base;


import org.filter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private CustomJWTFilter customJWTFilter;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow all endpoints
                .allowedOrigins("http://localhost:5100") // Allow specific origin (frontend URL)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed methods
                .allowedHeaders("Authorization", "Content-Type") // Allow specific headers
                .exposedHeaders("Authorization") // Allow client to read Authorization header in the response
                .allowCredentials(true); // Allow credentials (cookies, Authorization header)
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder bldr = http.getSharedObject(AuthenticationManagerBuilder.class);
        bldr.authenticationProvider(authenticationProvider);
        AuthenticationManager manager = bldr.build();
        http.cors(cor->cor.configurationSource(corsConfigurationSource));
        http.authorizeHttpRequests(req->req.requestMatchers("/v1/personalAccount/**").authenticated()
                .requestMatchers("/v1/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll());

//        http.formLogin(Customizer.withDefaults()).logout(
//                logout->logout.logoutUrl("/v1/personalAccount/logout")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                        .permitAll()
//        );

        http.authenticationManager(manager).addFilterBefore(customJWTFilter, UsernamePasswordAuthenticationFilter.class);
        //http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(headers->headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        return http.build();
    }


}
