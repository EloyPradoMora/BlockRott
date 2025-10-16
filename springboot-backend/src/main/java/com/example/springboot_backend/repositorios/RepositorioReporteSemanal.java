package com.example.springboot_backend.repositorios;

import com.example.springboot_backend.entidades.ReporteSemanal;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioReporteSemanal extends JpaRepository<ReporteSemanal, Long> {
}
