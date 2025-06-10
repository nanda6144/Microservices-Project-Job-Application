package com.Application.Controller;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.Application.Model.Application;
import com.Application.Repository.ApplicationRepository;
import com.Application.Service.JobServiceClient;
import com.example.common.model.Job;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;


@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobServiceClient jobServiceClient;

    @PostMapping
    @CircuitBreaker(name = "jobservice", fallbackMethod = "departmentServiceFallBackMethod")
    @ResponseStatus(HttpStatus.CREATED)
    @Retry(name = "jobservice")
    @TimeLimiter(name = "jobservice")
    public CompletableFuture<Application> applyForJob(@RequestBody Application application) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Job job = jobServiceClient.getJobById(application.getJobId());
                if (job != null) {
                    application.setJobId(job.getId());
                    return applicationRepository.save(application);
                } else {
                    throw new JobNotFoundException("Job not found for ID: " + application.getJobId());
                }
            } catch (Exception e) {
                logger.error("Error occurred while calling job service: {}", e.getMessage());
                throw new RuntimeException("Error while applying for job.");
            }
        });
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CompletableFuture<String> departmentServiceFallBackMethod(Application application, Throwable t) {
        //logger.warn("Fallback triggered: {}", e.getMessage());
    	return CompletableFuture.supplyAsync(()-> "Job Service is down and it is taking longer than expected. Please try again later.");
    }

    public static class JobNotFoundException extends RuntimeException {
        public JobNotFoundException(String message) {
            super(message);
        }
    }
}