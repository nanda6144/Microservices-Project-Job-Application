package com.Application.Service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.common.model.Job;

@FeignClient(name = "job-service", url = "http://localhost:2026")
public interface JobServiceClient {
	
	@GetMapping("/jobs/{id}")
    Job getJobById(@PathVariable("id") Long id);

}
