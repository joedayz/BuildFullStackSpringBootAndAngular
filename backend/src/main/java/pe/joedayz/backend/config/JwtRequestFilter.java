package pe.joedayz.backend.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pe.joedayz.backend.service.JwtUserDetailsService;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  private JwtUserDetailsService jwtUserDetailsService;
  private JwtUtil jwtUtil;

  public JwtRequestFilter(JwtUserDetailsService jwtUserDetailsService, JwtUtil jwtUtil) {
    this.jwtUserDetailsService = jwtUserDetailsService;
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      FilterChain filterChain) throws ServletException, IOException {

    final String requestTokenHeader = httpServletRequest.getHeader("Authorization");
    String username = null;
    String jwtToken = null;

    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
      jwtToken = requestTokenHeader.substring(7);
      try {
        username = jwtUtil.getUsernameFromToken(jwtToken);
      } catch (IllegalArgumentException e) {
        System.out.println("Unable to get JWT token.");
      } catch (ExpiredJwtException e) {
        System.out.println("JWT token has expired.");
      }
    }else{
      logger.warn("JWT Token does not begin with Bearer String");
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
      if (jwtUtil.validateToken(jwtToken, userDetails)) {
        UsernamePasswordAuthenticationToken uPAuthToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities()
        );
        uPAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        SecurityContextHolder.getContext().setAuthentication(uPAuthToken);
      }
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}
