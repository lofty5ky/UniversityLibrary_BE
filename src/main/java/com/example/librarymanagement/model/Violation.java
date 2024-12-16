package com.example.librarymanagement.model;

import com.example.librarymanagement.enums.ViolationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "violations")
public class Violation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "violation_id")
    private Long violationId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "violation_date", nullable = false)
    private LocalDate violationDate;

    @Column(name = "description")
    private String description;

    @Column(name = "resolved", nullable = false)
    private Boolean resolved;

    @Column(name = "resolve_at")
    private LocalDate resolvedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "violation_type")
    private ViolationType violationType;
}