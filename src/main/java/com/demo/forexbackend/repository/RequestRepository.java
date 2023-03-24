package com.demo.forexbackend.repository;

import com.demo.forexbackend.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
