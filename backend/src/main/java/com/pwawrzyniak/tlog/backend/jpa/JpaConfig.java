package com.pwawrzyniak.tlog.backend.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl", modifyOnCreate = false)
public class JpaConfig {
}