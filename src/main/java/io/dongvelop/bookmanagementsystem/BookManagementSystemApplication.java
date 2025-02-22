package io.dongvelop.bookmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "io.dongvelop")
@EnableJpaRepositories(basePackages = "io.dongvelop.bookmanagementsystem.repository")
@EntityScan("io.dongvelop.bookmanagementsystem.entity")
public class BookManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookManagementSystemApplication.class, args);
    }
}
