package com.xlight.security.controllers;

import com.xlight.security.config.ApplicationAuditAware;
import com.xlight.security.Repository.UserRepository;
import com.xlight.security.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/demo-controller")
public class DemoController {
  private final ApplicationAuditAware auditAware;
  private final UserRepository userRepository;

  public DemoController(ApplicationAuditAware auditAware, UserRepository userRepository) {
    this.auditAware = auditAware;
    this.userRepository = userRepository;
  }

  @GetMapping
  @PreAuthorize("hasAnyAuthority('ADMIN')")
  public ResponseEntity<String> sayHello() {
    try {
      Optional<Integer> userIdOpt = auditAware.getCurrentAuditor();
      if (userIdOpt.isPresent()) {
        Integer userId = userIdOpt.get();
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
          User user = userOpt.get();
          return ResponseEntity.ok("Hello User ID: " + userId + " with role: " + user.getRole() + " from secured endpoint");
        } else {
          return ResponseEntity.status(HttpStatus.NOT_FOUND)
                  .body("User not found.");
        }
      } else {
        return ResponseEntity.ok("Hello from secured endpoint");
      }
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("An error occurred: " + ex.getMessage());
    }
  }
}
