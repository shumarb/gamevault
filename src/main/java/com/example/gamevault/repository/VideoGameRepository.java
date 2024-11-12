package com.example.gamevault.repository;

import com.example.gamevault.model.VideoGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoGameRepository extends JpaRepository<VideoGame, Long> {
    Optional<VideoGame> findByTitle(String title);
}
