package com.animeregistration.animes.config;

import com.animeregistration.animes.service.UserInfoDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


//@Configuration
@Log4j2
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserInfoDetailsService userInfoDetailsService;

    /**
     * BasicAuthenticationFilter
     * UsernamePasswordAuthenticationFilter
     * DefaultLoginPageGeneratingFilter
     * DefaultLogoutPageGeneratingFilter
     * FilterDecurityInterceptor
     * Authentication -> Authorization
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
//                  http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
////                 .and()
                .authorizeHttpRequests()
                .antMatchers("/users/admin/**").hasRole("ADMIN")
                .antMatchers("/animes/admin/**").hasRole("ADMIN")
                .antMatchers("/animes/**").hasRole("USER")
                .antMatchers("/actuator/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       PasswordEncoder passwordEncoder,
                                                       UserInfoDetailsService userInfoDetailsService)
                                                        throws Exception {

        log.info("Password encoded '{}'", passwordEncoder.encode("springessentials2"));
        log.info("Password 2 encoded '{}'", passwordEncoder.encode("test2"));
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userInfoDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

//    To use an 'InMemory' User:
    /*
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
          PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        log.info("Password encoded '{}'", encoder.encode("springessentials2"));
//        log.info("Password 2 encoded '{}'", encoder.encode("test2"));
        UserDetails user = User.withUsername("viniciusMemory")
                .password(encoder().encode("springessentials2"))
                .roles("ADMIN", "USER")
//                .authorities("ROLE_ADMIN", "ROLE_USER")
                .build();

        UserDetails user2 = User.withUsername("vinicius_test_memory")
                .password(encoder().encode("test2"))
                .roles("USER")
//                .authorities("ROLE_USER")
                .build();

        return new InMemoryUserDetailsManager(user, user2);
    }
    */
}
