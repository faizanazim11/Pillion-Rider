package com.pillion.rider.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import com.pillion.rider.service.UserService;
import com.pillion.rider.util.CustomOAuthAuthenticationHandler;
import com.pillion.rider.util.TokenFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class UserConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserService userService;

    @Autowired
    private TokenFilter tokenFilter;

    @Autowired
    private CustomOAuthAuthenticationHandler customOAuthAuthenticationHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http
         .httpBasic()
         .and()
         .authorizeRequests()
         .antMatchers("/user**").hasAnyRole("ADMIN", "USER").anyRequest().authenticated()
         .and()
         .oauth2Login()
         .userInfoEndpoint()
         .oidcUserService(this.oidcUserService())
         .and()
         .successHandler(customOAuthAuthenticationHandler)
         .and()
         .csrf().disable();

        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
    
    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService()
    {
        OidcUserService delegate = new OidcUserService();

        return (userRequest) -> {
            OidcUser oidcUser = delegate.loadUser(userRequest);
            Collection<? extends GrantedAuthority> mappedAuthorities;
            if(userService.existsByEmail(oidcUser.getEmail()))
            {
                mappedAuthorities = userService.loadUserByUsername(oidcUser.getEmail()).getAuthorities();
            }
            else
            {
                mappedAuthorities = Arrays.stream("ROLE_USER".split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            }
            oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
            return oidcUser;
        };
    }
}
