package org.example.hospital_admission_project.repo;

import org.example.hospital_admission_project.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
}