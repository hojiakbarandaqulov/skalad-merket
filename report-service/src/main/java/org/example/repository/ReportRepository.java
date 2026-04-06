package org.example.repository;

import org.example.entity.Report;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long>{

    Page<Report> findAll(Specification<Report> spec, Pageable pageable);

    Optional<Report> findByIdAndDeletedFalse(Long id);
}
