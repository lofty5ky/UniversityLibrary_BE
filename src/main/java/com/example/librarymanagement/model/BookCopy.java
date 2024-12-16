package com.example.librarymanagement.model;

import com.example.librarymanagement.enums.BookCondition;
import com.example.librarymanagement.enums.BookStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bookcopies") // Tên bảng trong cơ sở dữ liệu
public class BookCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "copy_id")
    private Long copyId;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_condition")
    private BookCondition bookCondition;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookStatus status;
}