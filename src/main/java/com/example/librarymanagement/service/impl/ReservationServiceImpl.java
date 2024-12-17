package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.dto.request.ReservationRequestDTO;
import com.example.librarymanagement.dto.request.ReservationUpdateRequestDTO;
import com.example.librarymanagement.dto.response.ReservationResponseDTO;
import com.example.librarymanagement.enums.BookStatus;
import com.example.librarymanagement.enums.BorrowStatus;
import com.example.librarymanagement.enums.ReservationStatus;
import com.example.librarymanagement.exception.ResourceNotFoundException;
import com.example.librarymanagement.model.*;
import com.example.librarymanagement.repository.*;
import com.example.librarymanagement.service.ReservationService;
import com.example.librarymanagement.specification.ReservationSpecification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final JavaMailSender mailSender;

    @Override
    public ReservationResponseDTO createReservation(ReservationRequestDTO reservationRequest) {
        User user = userRepository.findById(reservationRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với ID: " + reservationRequest.getUserId()));

        Book book = bookRepository.findById(reservationRequest.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sách với ID: " + reservationRequest.getBookId()));

        long borrowedBooksCount = borrowRecordRepository.countByUserAndStatusNot(user, BorrowStatus.RETURNED);
        if (borrowedBooksCount >= 5) {
            throw new IllegalStateException("Bạn đã mượn tối đa 5 cuốn sách. Vui lòng trả sách trước khi mượn thêm.");
        }

        long pendingReservationsCount = reservationRepository.countByUserAndStatus(user, ReservationStatus.PENDING);
        if (pendingReservationsCount >= 5) {
            throw new IllegalStateException("Bạn đã tạo tối đa 5 yêu cầu mượn sách. Vui lòng chờ phê duyệt.");
        }

        boolean alreadyReserved = reservationRepository.existsByUserAndBookAndStatus(user, book, ReservationStatus.PENDING);

        if (alreadyReserved) {
            throw new IllegalStateException("Bạn đã đặt sách này trước đó. Hãy chờ phê duyệt.");
        }

        Reservation reservation = Reservation.builder()
                .user(user)
                .book(book)
                .reservationDate(reservationRequest.getReservationDate())
                .status(ReservationStatus.PENDING)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);
        return convertToResponseDTO(savedReservation);
    }

    @Override
    public Optional<ReservationResponseDTO> findById(Long reservationId) {
        return reservationRepository.findById(reservationId).map(this::convertToResponseDTO);
    }

    @Override
    public Page<ReservationResponseDTO> findAllReservations(ReservationStatus status, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Specification<Reservation> spec = Specification.where(null);

        if (status != null) {
            spec = spec.and(ReservationSpecification.hasStatus(status));
        }
        if (startDate != null || endDate != null) {
            spec = spec.and(ReservationSpecification.isInDateRange(startDate, endDate));
        }

        Page<Reservation> reservations = reservationRepository.findAll(spec, pageable);
        return reservations.map(this::convertToResponseDTO);
    }

    @Override
    public Page<ReservationResponseDTO> findReservationsByUserId(Long userId, Pageable pageable) {
        Page<Reservation> reservations = reservationRepository.findByUserUserId(userId, pageable);
        return reservations.map(this::convertToResponseDTO);
    }

    @Override
    public ReservationResponseDTO updateReservation(Long reservationId, ReservationUpdateRequestDTO reservationRequest) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        if (reservationRequest.getStatus() != null) {
            reservation.setStatus(reservationRequest.getStatus());
        }

        if (reservationRequest.getApprovedCopyId() != null) {
            BookCopy bookCopy = bookCopyRepository.findById(reservationRequest.getApprovedCopyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Book copy not found"));

            if (!bookCopy.getStatus().equals(BookStatus.AVAILABLE)) {
                throw new IllegalStateException("Book copy is not available");
            }

            bookCopy.setStatus(BookStatus.BORROWED);
            bookCopyRepository.save(bookCopy);
            reservation.setApprovedCopy(bookCopy);
        }

        Reservation updatedReservation = reservationRepository.save(reservation);

        if (reservation.getStatus() == ReservationStatus.FULFILLED && reservation.getApprovedCopy() != null) {
            BorrowRecord borrowRecord = BorrowRecord.builder()
                    .user(reservation.getUser())
                    .copy(reservation.getApprovedCopy())
                    .borrowDate(LocalDate.now())
                    .dueDate(LocalDate.now().plusWeeks(2))
                    .status(BorrowStatus.BORROWED)
                    .build();
            borrowRecordRepository.save(borrowRecord);
        }

        // (Logic gửi email)
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("Thư Viện - Trường đại học Nguyễn Tất Thành <ductinnguyen251@gmail.com>");
            helper.setTo(reservation.getUser().getEmail());
            helper.setSubject("Thông báo về yêu cầu đặt mượn sách");
            helper.setText("Yêu cầu mượn sách với tựa đề " + reservation.getBook().getTitle() + " của bạn đã được chấp nhận!");

            mailSender.send(mimeMessage);
            log.info("Email approve has been sent");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return convertToResponseDTO(updatedReservation);
    }


    @Override
    public void deleteReservationById(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    @Override
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with ID: " + reservationId));

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        // (Logic gửi email)
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("Thư Viện - Trường đại học Nguyễn Tất Thành <ductinnguyen251@gmail.com>");
            helper.setTo(reservation.getUser().getEmail());
            helper.setSubject("Thông báo về yêu cầu đặt mượn sách");
            helper.setText("Yêu cầu mượn sách với tựa đề " + reservation.getBook().getTitle() + " của bạn không được chấp nhận!");

            mailSender.send(mimeMessage);

            log.info("Email has been sent");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private ReservationResponseDTO convertToResponseDTO(Reservation reservation) {
        return ReservationResponseDTO.builder()
                .id(reservation.getReservationId())
                .userName(reservation.getUser().getName())
                .email(reservation.getUser().getEmail())
                .bookTitle(reservation.getBook().getTitle())
                .bookId(reservation.getBook().getBookId())
                .approvedCopyId(reservation.getApprovedCopyId() != null ? reservation.getApprovedCopyId() : null)
                .reservationDate(reservation.getReservationDate())
                .status(reservation.getStatus().toString())
                .build();
    }
}

