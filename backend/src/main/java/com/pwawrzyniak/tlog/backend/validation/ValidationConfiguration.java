package com.pwawrzyniak.tlog.backend.validation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

@Configuration
public class ValidationConfiguration {

  @Bean
  Validator validator() {
    return Validation.buildDefaultValidatorFactory().getValidator();
  }
}