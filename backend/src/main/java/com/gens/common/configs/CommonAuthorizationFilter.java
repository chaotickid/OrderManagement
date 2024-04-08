package com.gens.common.configs;

import com.gens.common.constants.Constants;
import com.gens.common.model.GlobalTokenVM;
import com.gens.common.model.UserAuthorizationVM;
import com.gens.common.utils.JwtTokenProvider;
import com.gens.usermanagement.model.document.User;
import com.gens.usermanagement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class CommonAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtTokenProvider jwtTokenProvider;

    private Environment environment;

    private GlobalTokenDecoder globalTokenDecoder;

    private UserService userService;


    public CommonAuthorizationFilter(AuthenticationManager authenticationManager,
                                     JwtTokenProvider jwtTokenProvider,
                                     UserService userService,
                                     GlobalTokenDecoder globalTokenDecoder,
                                     Environment environment) {
        super(authenticationManager);

        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.globalTokenDecoder = globalTokenDecoder;
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String idToken = request.getHeader(Constants.ID_TOKEN);
        Authentication authentication = null;
        try {
            authentication = getAuthentication(request, response);
        } catch (Exception e) {
            log.debug("Captured the exception while authenticating request, Reason: {}", e.getMessage());
            authentication = null;
        }
        if (authentication == null) {
            chain.doFilter(request, response);
        } else {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }
    }

    private Authentication getAuthentication(HttpServletRequest request,
                                             HttpServletResponse response) {
        Authentication authentication = null;
        String securityType = environment.getProperty(Constants.SECURITY_SELECTION, Constants.INTERNAL);
        if (securityType.equalsIgnoreCase(Constants.INTERNAL)) {
            log.debug("Filter using internal filter");
            authentication = filterUsingInternal(request, response);
        }
        return authentication;
    }

    private Authentication filterUsingInternal(HttpServletRequest request,
                                               HttpServletResponse response) {
        UserAuthorizationVM userAuthorizationVM = new UserAuthorizationVM();
        GlobalTokenVM globalTokenVM = null;
        String idToken = request.getHeader(Constants.ID_TOKEN);
        PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken;
        User fetchedUserFromDb = null;
        try {
            log.debug("Validating idToken =>");
            jwtTokenProvider.validateToken(idToken);
            log.debug("Token validated successfully, now parsing and creating GlobalTokenVM object form idToken");
            globalTokenVM = globalTokenDecoder.parseToken(idToken, Constants.INTERNAL);
            log.debug("Parsed global tokenVM object: {}", globalTokenVM);
            fetchedUserFromDb = userService.getUser(globalTokenVM.getEmail());
            log.debug("Authenticating user and secret key");
            jwtTokenProvider.validateSecretKey(fetchedUserFromDb, globalTokenVM.getSecretKey());
            log.debug("User authenticated successfully, storing user in the security context holder");
            userAuthorizationVM.setEmail(globalTokenVM.getEmail());
            userAuthorizationVM.setSecretKey(globalTokenVM.getSecretKey());
            preAuthenticatedAuthenticationToken = new PreAuthenticatedAuthenticationToken(userAuthorizationVM, idToken, new ArrayList<>());
            return preAuthenticatedAuthenticationToken;
        } catch (Exception e) {
            log.debug("Exception captured while authenticating request: {}, methodType: {}, reason:{}",
                    request.getRequestURI(),
                    request.getMethod(), e.getMessage());
            return null;
        }
    }
}