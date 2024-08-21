package com.xlight.security.controllers;

import com.xlight.security.Exceptions.CustomExceptions;
import com.xlight.security.config.JwtService;
import com.xlight.security.config.LogoutService;
import com.xlight.security.payload.AuthenticationRequest;
import com.xlight.security.payload.AuthenticationResponse;
import com.xlight.security.payload.RegisterRequest;
import com.xlight.security.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;
  private final LogoutService logoutService;


  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    try {
      AuthenticationResponse authenticationResponse = service.register(request);
      return ResponseEntity.ok(authenticationResponse);
    } catch (CustomExceptions.UsernameAlreadyExistsException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    } catch (CustomExceptions.WeakPasswordException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed due to an unexpected error.");
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
    try {
      AuthenticationResponse authenticationResponse = service.authenticate(request);
      return ResponseEntity.ok(authenticationResponse);
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed .");
    }
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
    try {
      service.refreshToken(request, response);
      return ResponseEntity.ok("Token refreshed successfully.");
    } catch (IOException ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token refresh failed.");
    }
  }
  @PostMapping("/logout")
  public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    logoutService.logout(request, response, authentication);
    return ResponseEntity.ok("Successfully logged out");
  }
}
