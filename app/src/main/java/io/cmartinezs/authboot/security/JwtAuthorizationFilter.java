package io.cmartinezs.authboot.security;

import io.cmartinezs.authboot.core.entity.domain.user.User;
import io.cmartinezs.authboot.core.port.service.AuthServicePort;
import io.cmartinezs.authboot.core.port.service.TokenServicePort;
import io.cmartinezs.authboot.infra.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";
    private final AuthServicePort authServicePort;
    private final TokenServicePort tokenServicePort;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {

        var requestHeader = request.getHeader("Authorization");

        if (requestHeader != null && requestHeader.startsWith(BEARER)) {
            var authToken = requestHeader.substring(BEARER.length());
            String username = tokenServicePort
                    .getUsername(authToken)
                    .orElseThrow(() -> new ServletException("Username not found in token"));

            User user = authServicePort.getByUsername(username)
                    .orElseThrow(() -> new ServletException("User not found"));

            if(!tokenServicePort.validate(authToken, user)){
                throw new ServletException("User not found");
            }

            var appUser = new AppUserDetails(user);
            var authentication = new UsernamePasswordAuthenticationToken(
                    appUser
                    , null
                    , appUser.getAuthorities()
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } else {
            logger.warn("Couldn't find bearer string, will ignore the header");
        }

        filterChain.doFilter(request, response);
    }
}
