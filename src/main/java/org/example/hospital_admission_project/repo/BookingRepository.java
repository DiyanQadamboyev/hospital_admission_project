package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.Booking;
import org.example.hospital_admission_project.entity.enums.ConsultationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Optional<Booking> findByUserIdAndDoctorId(Integer userId, Integer doctorId);

    List<Booking> findByUserId(Integer userId);

    List<Booking> findByStatus(ConsultationStatus consultationStatus);

}