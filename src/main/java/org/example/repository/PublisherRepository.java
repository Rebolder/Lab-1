package org.example.repository;

import org.example.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    boolean existsByEmail(String email);
}
