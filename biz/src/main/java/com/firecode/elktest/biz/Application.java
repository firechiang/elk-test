package com.firecode.elktest.biz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author JIANG
 */
@SpringBootApplication
@EntityScan(basePackages={"com.firecode.elktest.biz.domain"})
@EnableJpaRepositories(basePackages={"com.firecode.elktest.biz.dao"})
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
