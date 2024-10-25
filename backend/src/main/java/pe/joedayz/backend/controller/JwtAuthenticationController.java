package pe.joedayz.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pe.joedayz.backend.config.JwtUtil;
import pe.joedayz.backend.model.User;
import pe.joedayz.backend.repo.UserRepository;
import pe.joedayz.backend.service.JwtUserDetailsService;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

  private UserRepository repo;
  private JwtUserDetailsService jwtUserDetailsService;
  private JwtUtil jwtUtil;
  private AuthenticationManager authenticationManager;

  public JwtAuthenticationController(UserRepository repo,
      JwtUserDetailsService jwtUserDetailsService,
      JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
    this.repo = repo;
    this.jwtUserDetailsService = jwtUserDetailsService;
    this.jwtUtil = jwtUtil;
    this.authenticationManager = authenticationManager;
  }

 @GetMapping("/user")
  public ResponseEntity<User> getCurrentUser(HttpServletRequest request) {
   Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
   User user = repo.findByUsername(((UserDetails) principal).getUsername());
   return ResponseEntity.ok(user);
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> user) throws Exception {
    User savedUser = new User();
    User newUser = new User(
        (String) user.get("username"),
        (String) user.get("password"),
        (String) user.get("email"),
        (String) user.get("name"),
        (String) user.get("address"),
        (String) user.get("phone")
    );

    if (newUser.getUsername() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is missing.");
    }

    if (newUser.getEmail() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is missing.");
    }

    if (newUser.getPassword() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is missing.");
    } else if (newUser.getPassword().length() < 8) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password length must be 8+.");
    }

    if (newUser.getName() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name is missing.");
    }

    if (newUser.getAddress() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Address is missing.");
    }

    if (newUser.getPhone() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Phone is missing.");
    }

    try{
     savedUser = jwtUserDetailsService.save(newUser);
    } catch (DataIntegrityViolationException e) {
      if (e.getRootCause().getMessage().contains(newUser.getUsername())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is not available.");
      }

      if (e.getRootCause().getMessage().contains(newUser.getEmail())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is not available.");
      }
    }

    Map<String, Object> tokenResponse = new HashMap<>();

    final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(savedUser.getUsername());
    final String token = jwtUtil.generateToken(userDetails);

    tokenResponse.put("token", token);
    return ResponseEntity.status(HttpStatus.CREATED).body(tokenResponse);

  }


  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser (@RequestBody Map<String, String> user) throws Exception {
    authenticate(user.get("username"), user.get("password"));

    Map<String, Object> tokenResponse = new HashMap<>();
    final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(user.get("username"));
    final String token = jwtUtil.generateToken(userDetails);

    tokenResponse.put("token", token);
    return ResponseEntity.ok(tokenResponse);
  }

  private void authenticate(String username, String password) throws Exception {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new Exception("User disabled", e);
    } catch (BadCredentialsException e) {
      throw new Exception("Invalid credentials", e);
    }
  }
}
