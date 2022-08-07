package com.kitap.kitap.security;

import com.kitap.kitap.Repo.UserRepo;
import com.kitap.kitap.filter.CustomAuthenticationFilter;
import com.kitap.kitap.filter.CustomAuthorizationFilter;
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
//TODO:"/SUPER_ADMIN/".hasAUTHORITY(SUPER_ADMIN)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/signup/**","/confirm/**").permitAll();
        http.authorizeRequests().antMatchers("/login/**", "/token/refresh/**").permitAll();
        http.authorizeRequests().antMatchers(GET,"/kitap/**", "/user/**").permitAll();
        http.authorizeRequests().antMatchers(POST, "/user/save").hasAnyAuthority("ADMIN","SUPER_ADMIN");
        http.authorizeRequests().antMatchers(POST, "/role/save").hasAnyAuthority("ADMIN","SUPER_ADMIN");
        http.authorizeRequests().antMatchers(POST, "/kitap").hasAnyAuthority("SUPER_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/kitap/**").hasAnyAuthority("SUPER_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/kitap").hasAnyAuthority("SUPER_ADMIN");
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