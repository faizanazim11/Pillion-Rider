package com.pillion.rider.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pillion.rider.repository.InMemoryRequestRepository;
import com.pillion.rider.service.UserService;
import com.pillion.rider.util.CustomOAuthAuthenticationHandler;
import com.pillion.rider.util.TokenFilter;
import com.pillion.rider.util.TokenStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class UserConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserService userService;

    @Autowired
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    private final ObjectMapper mapper;
    private final TokenFilter tokenFilter;
    private final TokenStore tokenStore;

    public UserConfig(ObjectMapper mapper, TokenFilter tokenFilter, TokenStore tokenStore)
    {
        this.mapper = mapper;
        this.tokenFilter = tokenFilter;
        this.tokenStore = tokenStore;
    }

    @Autowired
    private CustomOAuthAuthenticationHandler customOAuthAuthenticationHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http.cors().and()
         .httpBasic()
         .and()
         .authorizeRequests()
         .antMatchers("/oauth2/**", "/login**").permitAll()
         .anyRequest().authenticated()
         .and()
         .oauth2Login()
         .userInfoEndpoint()
         .and().authorizationEndpoint()
         .authorizationRequestRepository(new InMemoryRequestRepository())
         .and()
         .successHandler(this::successHandler)
         .and()
         .exceptionHandling()
         .authenticationEntryPoint(this::authenticationEntryPoint)
         .and()
         .csrf().disable();

        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private void successHandler(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws JsonProcessingException, IOException {
        DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();
        userService.processOAuthPostLogin(user);
        System.out.println("Success Handler");
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
        String accessToken = client.getAccessToken().getTokenValue();
        tokenStore.generateToken(accessToken, authentication);
        userService.addLoggedIn(accessToken, user.getEmail());
        response.getWriter().write(mapper.writeValueAsString(Collections.singletonMap("accessToken", accessToken)));
    }

    private void authenticationEntryPoint(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws JsonProcessingException, IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(mapper.writeValueAsString(Collections.singletonMap("error", "Unauthenticated")));
    }
}
