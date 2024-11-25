package com.example.demo.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class securityconfig {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }    

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(request-> request.requestMatchers("/").permitAll()
            .requestMatchers("/css/**", "/js/**", "/images/**","/register","/login","/reports/**").permitAll()
            .requestMatchers("/patient").hasRole("Patient")  // Only patients can access /patient
            .requestMatchers("/doctor/**").hasRole("Doctor")    // Only doctors can access /doctor
            .requestMatchers("/supplier/**").hasRole("Supplier") // Only suppliers can access /supplier
            .requestMatchers("/pharmacy/**").hasRole("Pharmacy")
            .anyRequest().authenticated())
            .csrf(AbstractHttpConfigurer::disable)
            .logout(LogoutConfigurer::permitAll)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .maximumSessions(1).expiredUrl("/login?expired=true"))
            .exceptionHandling(handler -> handler
                .authenticationEntryPoint(authenticationEntryPoint())
            );

        return http.build();
    }
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> response.sendRedirect("/login");
    }



}
