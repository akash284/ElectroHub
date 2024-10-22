package com.lcwa.electronic.store.config;

import com.lcwa.electronic.store.security.JWTAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = true)
public class securityConfig {

    @Autowired
    private AuthenticationEntryPoint entryPoint;
    @Autowired
    private JWTAuthenticationFilter myFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {


        // custom configuartions
        // konse urls ko public rakhna,konse urls ko protected
        // konsa urls admin access kar sakta, konsa urls normal user


        // configuring urls
        // cors ko hum abhi k lie disable kie he
        // badmein smjege kya hota ye

        security.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());
//
//        security.cors(HttpSecurityCorsConfigurer ->
//                HttpSecurityCorsConfigurer.configurationSource(new CorsConfigurationSource() {
//                    @Override
//                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//
//                        CorsConfiguration corsConfiguration = new CorsConfiguration();
//
//                        // we can define
//                        // origins and methods here that are allowed to be accessed
//
//                       // corsConfiguration.addAllowedOrigin("http://localhost:4200"); // one origin
//                        // corsConfiguration.setAllowedOrigins(List.of("http:localhost:4200","http:localhost:3300","http:localhost:2300")); // multiple origins
//                        corsConfiguration.setAllowedOrigins(List.of("*")); // allowed all origins
//
//                        corsConfiguration.setAllowedOriginPatterns(List.of("*")); // can also be GET,PUT,POST
//                        corsConfiguration.setAllowCredentials(true);
//                        corsConfiguration.setAllowedHeaders(List.of("Authorization"));
//                        corsConfiguration.setAllowedHeaders(List.of("*")); // kyuki meko sare headers chahiye like AUTHORIZATION jisme tokens pass krege tabhi operation perform karpayge
//                        corsConfiguration.setMaxAge(3000L); //Configure how long, in seconds, the response from a pre-flight request can be cached by clients
//                        return corsConfiguration;
//                    }
//                }));
        // csrf ko bhi
        security.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());


        security.authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.DELETE, "/users/**").hasRole(AppConstants.ROLE_ADMIN)
                                .requestMatchers(HttpMethod.PUT,"/users/**").hasAnyRole("ADMIN","NORMAL")
                                .requestMatchers("/users/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/products").permitAll()
                                .requestMatchers("/products/**").hasRole(AppConstants.ROLE_ADMIN)
                                .requestMatchers(HttpMethod.GET,"/categories/**").permitAll()
                                .requestMatchers("/categories/**").hasRole(AppConstants.ROLE_ADMIN)
                                .requestMatchers(HttpMethod.POST,"/auth/generate-token","/auth/regenerate-token").permitAll()
                                .requestMatchers("/auth/**").authenticated()
                                .anyRequest().permitAll()

                );

        // kis type ki security use karege
        // hume harbar username and password bhejna hoga

     //   security.httpBasic(Customizer.withDefaults());

//     <--   ab jwt wala use karege -->

        // koi exceptions aati heto us case mein EntryPoint execute ho

        //entryPoint configuration
        security.exceptionHandling(ex->ex.authenticationEntryPoint(entryPoint));  //sets the authep if exception occurs if no authep is specified

        // session creation or management (SERVER PAR KOI DATA STORE NI KARNA HEIN)
        security.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // filter configure karna hein
        security.addFilterBefore(myFilter, UsernamePasswordAuthenticationFilter.class); // humare filter ko usernamepauth filter se pehle chalaiyega
        return security.build();
    }

//     password Encoder -->helps to store the encrypted password in the database
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}
