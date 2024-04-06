package io.cmartinezs.authboot.app.filters;

import io.cmartinezs.authboot.core.command.user.GetUserCmd;
import io.cmartinezs.authboot.core.entity.domain.user.User;
import io.cmartinezs.authboot.core.port.service.TokenServicePort;
import io.cmartinezs.authboot.core.port.service.UserServicePort;
import io.cmartinezs.authboot.infra.security.AppUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";
    private final UserServicePort userService;
    private final TokenServicePort tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {

        var requestHeader = request.getHeader("Authorization");

        if (requestHeader != null && requestHeader.startsWith(BEARER)) {
            var authToken = requestHeader.substring(BEARER.length());
            String username = tokenService
                    .getUsername(authToken)
                    .orElseThrow(() -> new ServletException("Username not found in token"));

            User user = userService.getUser(new GetUserCmd(username));

            if (!tokenService.validate(authToken, user)) {
                throw new ServletException("Token is not valid");
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
