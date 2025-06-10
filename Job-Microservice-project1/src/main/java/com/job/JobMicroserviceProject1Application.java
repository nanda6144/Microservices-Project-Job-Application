package com.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EntityScan(basePackages = "com.example.common.model")
@EnableDiscoveryClient
public class JobMicroserviceProject1Application {

	public static void main(String[] args) {
		SpringApplication.run(JobMicroserviceProject1Application.class, args);
	}

}
