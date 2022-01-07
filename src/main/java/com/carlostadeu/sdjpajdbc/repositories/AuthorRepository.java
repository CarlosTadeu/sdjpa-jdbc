package com.carlostadeu.sdjpajdbc.repositories;

import com.carlostadeu.sdjpajdbc.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
