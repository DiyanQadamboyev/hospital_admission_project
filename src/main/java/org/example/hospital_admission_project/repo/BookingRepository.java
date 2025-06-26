package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}