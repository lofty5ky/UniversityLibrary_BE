package com.example.librarymanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long feedbackId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "response")
    private String responseMessage;

    @Column(name = "is_response", nullable = false)
    private boolean isResponse;

    @Column(name = "submitted_at", nullable = false, updatable = false)
    private Timestamp submittedAt;

    @Column(name = "response_at")
    private Timestamp responseAt;
}
