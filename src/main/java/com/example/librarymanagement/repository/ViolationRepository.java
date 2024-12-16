package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.Violation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ViolationRepository extends JpaRepository<Violation, Long>, JpaSpecificationExecutor<Violation> {
    Page<Violation> findByUserUserId(Long userId, Pageable pageable);

    @Query("SELECT COUNT(v) FROM Violation v")
    long countTotalViolations();

    @Query("SELECT v.violationType AS type, COUNT(v) AS count " +
            "FROM Violation v " +
            "GROUP BY v.violationType")
    List<Map<String, Object>> countViolationsByType();
}
