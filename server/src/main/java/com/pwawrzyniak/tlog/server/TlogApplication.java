package com.pwawrzyniak.tlog.server;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.pwawrzyniak.tlog")
@EnableVaadin("com.pwawrzyniak.tlog")
@EnableJpaRepositories("com.pwawrzyniak.tlog")
@EntityScan("com.pwawrzyniak.tlog")
public class TlogApplication {

  public static void main(String[] args) {
    SpringApplication.run(TlogApplication.class, args);
  }
}