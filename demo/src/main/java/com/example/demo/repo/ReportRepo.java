package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Report;

@Repository
public interface ReportRepo extends JpaRepository<Report, Long> {
}
