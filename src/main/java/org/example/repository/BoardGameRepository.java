package org.example.repository;

import java.util.List;
import java.util.Optional;

import org.example.model.BoardGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {
    Optional<BoardGame> findBySlug(String slug);

    List<BoardGame> findByPublisherId(Long publisherId);

    boolean existsBySlug(String slug);
}
