package pe.joedayz.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil implements Serializable {

  private static final long serialVersionUID = -2550185165626007488L;
  public static final long JWT_TOKEN_VALIDITY = 5*60*60; //5 horas = 18,000 segundos

  @Value("${jwt.secret}")
  private String secret;

  public String getUsernameFromToken (String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public Date getIssuedAtDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getIssuedAt);
  }

  public Date getExpirationDateFromToken (String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }
  public <T> T getClaimFromToken (String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    // Crear una clave secreta usando el valor de la clave secreta
    Key key = Keys.hmacShaKeyFor(secret.getBytes());

    // Usar el JwtParserBuilder en lugar de parser() directamente
    return Jwts.parser()
        .setSigningKey(key)  // Establecer la clave secreta
        .build()              // Construir el parser
        .parseClaimsJws(token) // Parsear el token
        .getBody();           // Obtener las Claims
  }
  private boolean isTokenExpired (String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  private boolean ignoreExpirationDate (String token) {
    return false;
  }
  public String generateToken (UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return doGenerateToken(claims, userDetails.getUsername());
  }

  private String doGenerateToken (Map<String, Object> claims, String subject) {
    return Jwts.builder().setClaims(claims).setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 100000))
        .signWith(SignatureAlgorithm.HS512, secret).compact();
  }

  public boolean canTokenBeRefreshed (String token) {
    return !isTokenExpired(token) || ignoreExpirationDate(token);
  }
  public boolean validateToken (String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

}
