package com.xlight.security.controllers;

import com.xlight.security.payload.AuthenticationRequest;
import com.xlight.security.payload.AuthenticationResponse;
import com.xlight.security.payload.RegisterRequest;
import com.xlight.security.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    try {
      AuthenticationResponse authenticationResponse = service.register(request);
      return ResponseEntity.ok(authenticationResponse);
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed email already exist.");
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
}
