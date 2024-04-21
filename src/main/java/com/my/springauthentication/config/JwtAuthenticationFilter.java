package com.my.springauthentication.config;

import com.my.springauthentication.service.JWTService;
import com.my.springauthentication.service.UserService;
import com.my.springauthentication.utils.ConstantUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;

        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwt = authHeader.substring(7);
            email = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(ConstantUtils.BAD_TOKEN);
            return;
        }

        if (StringUtils.isNotEmpty(email) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(email);

            if(jwtService.isValidToken(jwt, userDetails)) {
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);
            }
        }
        filterChain.doFilter(request, response);
    }
}
