package com.pillion.rider.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pillion.rider.service.UserService;
import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
// import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuthAuthenticationHandler
        implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {
        DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();
        userService.processOAuthPostLogin(user);
        System.out.println("Success Handler");
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
        String accessToken = client.getAccessToken().getTokenValue();
        response.getWriter().write(mapper.writeValueAsString(Collections.singletonMap("accessToken", accessToken)));
        userService.addLoggedIn(accessToken, user.getEmail(), authentication);

        // new DefaultRedirectStrategy().sendRedirect(request, response, "/user");
    }
}
