package com.book.book.security;

import com.book.book.Repo.UserRepo;
import com.book.book.filter.CustomAuthenticationFilter;
import com.book.book.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.*;

@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepo userRepo;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers(GET,"/confirm/**","/login/**", "/token/refresh/**","/user/signup/**","/user/confirm/**").permitAll();
        http.authorizeRequests().antMatchers(GET,"/book/**", "/user/**","/role/**","/bookstore/**").hasAnyAuthority("USER","ADMIN","SUPER_ADMIN");
        http.authorizeRequests().antMatchers(POST, "/user/save").hasAnyAuthority("ADMIN","SUPER_ADMIN");
        http.authorizeRequests().antMatchers(POST, "/role/save").hasAnyAuthority("ADMIN","SUPER_ADMIN");
        http.authorizeRequests().antMatchers(POST, "/book").hasAnyAuthority("SUPER_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/book/**").hasAnyAuthority("SUPER_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/book/**").hasAnyAuthority("SUPER_ADMIN");
        http.authorizeRequests().antMatchers(POST, "/role/addtouser").hasAnyAuthority("SUPER_ADMIN");
        http.authorizeRequests().antMatchers(POST, "/enable_disable").hasAnyAuthority("SUPER_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(userRepo), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}