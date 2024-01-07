package com.example.gdscservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GdscServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GdscServiceApplication.class, args);
    }

}
