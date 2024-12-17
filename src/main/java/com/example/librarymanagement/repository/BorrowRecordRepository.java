package com.example.librarymanagement.repository;

import com.example.librarymanagement.enums.BorrowStatus;
import com.example.librarymanagement.model.BorrowRecord;
import com.example.librarymanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long>, JpaSpecificationExecutor<BorrowRecord> {
    List<BorrowRecord> findByDueDateBeforeAndReturnDateIsNullAndStatusNot(LocalDate dueDate, BorrowStatus status);

    long countByUserAndStatusNot(User user, BorrowStatus status);

    @Query("SELECT COUNT(br) FROM BorrowRecord br WHERE br.status <> 'RETURNED'")
    long countUnreturnedBooks();

    @Query("SELECT " +
            "MONTH(b.borrowDate) AS month, " +
            "COUNT(b) AS count " +
            "FROM BorrowRecord b " +
            "WHERE YEAR(b.borrowDate) = :year " +
            "GROUP BY MONTH(b.borrowDate) " +
            "ORDER BY MONTH(b.borrowDate)")
    List<Map<String, Object>> countBorrowedByMonth(@Param("year") int year);

    @Query("SELECT new map(b.bookId AS bookId, b.title AS title, b.image AS bookImage, GROUP_CONCAT(a.name) AS authors, COUNT(br) AS borrowCount) " +
            "FROM BorrowRecord br " +
            "JOIN br.copy c " +
            "JOIN c.book b " +
            "JOIN b.authors a " +
            "GROUP BY b.bookId, b.title, b.image " +
            "ORDER BY COUNT(br) DESC")
    List<Map<String, Object>> findTop5BorrowedBooks();

}
