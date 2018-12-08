package com.pwawrzyniak.tlog.backend.jpa;

import com.pwawrzyniak.tlog.backend.service.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of(userDetailsService.getLoggedInUser().getUsername());
  }
}