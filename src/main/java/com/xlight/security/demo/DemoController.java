package com.xlight.security.demo;

import com.xlight.security.auditing.ApplicationAuditAware;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/demo-controller")
@Hidden
public class DemoController {
  private final ApplicationAuditAware auditAware;

  @Autowired
  public DemoController(ApplicationAuditAware auditAware) {
    this.auditAware = auditAware;
  }
    @GetMapping
  public ResponseEntity<String> sayHello() {
      Optional<Integer> userIdOpt = auditAware.getCurrentAuditor();

      if (userIdOpt.isPresent()) {
        Integer userId = userIdOpt.get();
        // You can customize this to fetch more user details if needed
        return ResponseEntity.ok("Hello User ID: " + userId + " from secured endpoint");
      } else {
        return ResponseEntity.ok("Hello from secured endpoint");
      }
    }
  }


