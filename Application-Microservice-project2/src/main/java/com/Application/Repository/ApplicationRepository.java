package com.Application.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.Application.Model.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}

