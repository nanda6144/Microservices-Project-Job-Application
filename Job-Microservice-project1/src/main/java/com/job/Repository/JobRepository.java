package com.job.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.common.model.Job;


@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
}

